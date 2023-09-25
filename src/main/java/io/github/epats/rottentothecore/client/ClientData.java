package io.github.epats.rottentothecore.client;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.epats.rottentothecore.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.List;

public class ClientData {
    public static final MutableComponent BLANK_THOUGHT = Component.translatable("rottentothecore.thoughts.blank");
    private static MutableComponent message = BLANK_THOUGHT;
    public static int displayTime = 0;
    private static final int defaultDisplayTime = Config.ClientConfig.thoughtDisplayTicks;

    public static void changeThought(MutableComponent newMessage) {
        ClientData.message = newMessage;
        displayTime = defaultDisplayTime;
    }

    public static List<MutableComponent> getMessageSplit() {
        ImmutableList.Builder<MutableComponent> builder = ImmutableList.builder();
        String content = message.getString();
        Style style = message.getStyle();
        Pair<String, Style> pair = new Pair<>(content, style);
        pair = applyFormatIfSpecified(pair, "{b}", ChatFormatting.BOLD);
        pair = applyFormatIfSpecified(pair, "{i}", ChatFormatting.ITALIC);
        pair = applyFormatIfSpecified(pair, "{u}", ChatFormatting.UNDERLINE);
        pair = applyFormatIfSpecified(pair, "{st}", ChatFormatting.STRIKETHROUGH);
        pair = applyFormatIfSpecified(pair, "{ob}", ChatFormatting.OBFUSCATED);

        content = pair.getFirst();
        style = pair.getSecond();
        int i = content.indexOf("/");

        while (i > 0) {
            builder.add(Component.translatable(content.substring(0, i)).setStyle(style));
            content = content.substring(i + 1);
            i = content.indexOf("/");
        }

        builder.add(Component.translatable(content).setStyle(style));

        return builder.build();
    }

    private static Pair<String, Style> applyFormatIfSpecified(Pair<String, Style> pair, String prefix,
                                                              ChatFormatting format) {
        String content = pair.getFirst();
        Style style = pair.getSecond();
        if (content.startsWith(prefix)) {
            content = content.substring(prefix.length());
            style = style.applyFormat(format);
        }

        return new Pair<>(content, style);
    }

    public static String getMessageString() {
        return message.getString();
    }

    public static void reduceDisplayTime() {
        reduceDisplayTime(1);
    }

    public static void reduceDisplayTime(int amount) {
        if(displayTime == 0)
            return;
        displayTime = Math.max(displayTime - amount, 0);
        if (displayTime == 0)
            message = BLANK_THOUGHT;
    }

    public static boolean isBlankThought() {
        return message == BLANK_THOUGHT;
    }
}
