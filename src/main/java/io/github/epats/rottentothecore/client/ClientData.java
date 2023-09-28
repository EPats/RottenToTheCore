package io.github.epats.rottentothecore.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Window;
import com.mojang.datafixers.util.Pair;
import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.RottenToTheCore;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * ClientData class to handle displaying thoughts on the screen.
 */
public class ClientData {
    // A blank/default thought message.
    private static final MutableComponent BLANK_THOUGHT = Component.translatable("rottentothecore.thoughts.blank");

    // The current message to be displayed.
    private static MutableComponent message = BLANK_THOUGHT;

    // Duration for which the message is displayed.
    private static int displayTime = 0;
    private static final int defaultDisplayTime = Config.ClientConfig.thoughtDisplayTicks;
    private static final List<MutableComponent> splitMessageParts = new ArrayList<>();

    /**
     * Change the current displayed thought to a new message.
     *
     * @param newMessage The new message to display.
     */
    public static void changeThought(MutableComponent newMessage) {
        displayTime = defaultDisplayTime;
        message = newMessage;
        splitMessageParts.clear();
        splitMessageParts.addAll(splitMessage(newMessage));
    }

    /**
     * Apply specified formatting to content if it starts with the given prefix.
     *
     * @param content The content string.
     * @param prefix The prefix indicating a specific format.
     * @param format The format to apply.
     * @param style The style to update.
     * @return Updated content after trimming the prefix.
     */
    private static String applyFormatAndTrim(String content, String prefix, ChatFormatting format, Style style) {
        if (content.startsWith(prefix)) {
            style.applyFormat(format);
            return content.substring(prefix.length());
        }
        return content;
    }

    /**
     * Split the message based on custom formatting and content.
     *
     * @return A list of message components after applying the formatting.
     */
    private static List<MutableComponent> splitMessage(MutableComponent newMessage) {
        ImmutableList.Builder<MutableComponent> builder = ImmutableList.builder();
        String content = newMessage.getString();
        Style style = newMessage.getStyle();

        // Apply formatting based on custom tags.
        content = applyFormatAndTrim(content, "{b}", ChatFormatting.BOLD, style);
        content = applyFormatAndTrim(content, "{i}", ChatFormatting.ITALIC, style);
        content = applyFormatAndTrim(content, "{u}", ChatFormatting.UNDERLINE, style);
        content = applyFormatAndTrim(content, "{st}", ChatFormatting.STRIKETHROUGH, style);
        content = applyFormatAndTrim(content, "{ob}", ChatFormatting.OBFUSCATED, style);

        // Split message based on '/'
        for (String part : content.split("/")) {
            builder.add(Component.translatable(part).setStyle(style));
        }

        return builder.build();
    }

    /**
     * Decrease the display time by a default value of 1.
     */
    public static void reduceDisplayTime() {
        reduceDisplayTime(1);
    }

    /**
     * Decrease the display time by the specified amount.
     *
     * @param amount The amount by which to decrease the display time.
     */
    public static void reduceDisplayTime(int amount) {
        if (displayTime == 0)
            return;
        displayTime = Math.max(displayTime - amount, 0);
        if (displayTime <= 0)
            message = BLANK_THOUGHT;
    }
    public static boolean isBlankThought() {
        return message == BLANK_THOUGHT;
    }

    /**
     * Render the thought message on the screen with fade-in and fade-out effects.
     *
     * @param window The window in which to render the message.
     * @param guiGraphics Graphics utility to render GUI elements.
     */
    public static void render(Window window, GuiGraphics guiGraphics) {
        if (displayTime > 0 && Config.ClientConfig.enableThoughts && !isBlankThought()) {

            Minecraft mc = Minecraft.getInstance();
            if (mc.isPaused())
                return;

            float opacity = calculateOpacity(displayTime, 200, 100);

            for (int i = 0; i < splitMessageParts.size(); i++) {
                renderMessage(splitMessageParts.get(i), opacity, window, guiGraphics, mc, i);
            }
            reduceDisplayTime();
        }
    }

    /**
     * Calculate the opacity based on display time and fade durations.
     *
     * @param displayTime The current display time.
     * @param fadeIn Duration for fade-in effect.
     * @param fadeOut Duration for fade-out effect.
     * @return Calculated opacity value between 0 and 1.
     */
    private static float calculateOpacity(int displayTime, int fadeIn, int fadeOut) {
        if (displayTime > defaultDisplayTime - fadeIn) {
            return (defaultDisplayTime - displayTime) / (float) fadeIn;
        } else if (displayTime < fadeOut) {
            return displayTime / (float) fadeOut;
        } else {
            return 1.0F;
        }
    }

    /**
     * Render individual message on the screen with specified opacity.
     *
     * @param message The message to render.
     * @param opacity The opacity to use for rendering.
     * @param window The window in which to render the message.
     * @param guiGraphics Graphics utility to render GUI elements.
     * @param mc The Minecraft instance.
     * @param index The index position of the message in the list.
     */
    private static void renderMessage(MutableComponent message, float opacity, Window window, GuiGraphics guiGraphics, Minecraft mc, int index) {
        int width = mc.font.width(message.getVisualOrderText()) / 2;
        int mid = window.getGuiScaledWidth() / 2;
        int alpha = Math.round(opacity * 255);
        //Guard as low alphas are rendering at full opacity for some reason!
        if(alpha < 10) return;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0D, 0.0D, 50.0D);
        guiGraphics.pose().scale(1.0F, 1.0F, 1.0F);
        guiGraphics.drawString(mc.font, message.getVisualOrderText(), mid - width, 40 + 10 * index, 16777215 + (alpha << 24));
        guiGraphics.pose().popPose();
    }
}
