package io.github.epats.rottentothecore.client;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.List;

public class ClientData {
    private static MutableComponent message = Component.translatable("rottentothecore.thoughts.blank");
    public static int displayTime = 0;
    private static int defaultDisplayTime = 170;

    public static void changeThought(MutableComponent newMessage) {
        ClientData.message = newMessage;
        displayTime = defaultDisplayTime;
    }

    public static List<MutableComponent> getMessageSplit() {
        ImmutableList.Builder<MutableComponent> builder = ImmutableList.builder();
        String content = message.getString();
        Style style = message.getStyle();
        Pair<String, Style> pair = new Pair<String, Style>(content, style);
        pair = applyFormatIfSpecified(pair, "{b}", ChatFormatting.BOLD);
        pair = applyFormatIfSpecified(pair, "{i}", ChatFormatting.ITALIC);
        pair = applyFormatIfSpecified(pair, "{u}", ChatFormatting.UNDERLINE);
        pair = applyFormatIfSpecified(pair, "{st}", ChatFormatting.STRIKETHROUGH);
        pair = applyFormatIfSpecified(pair, "{ob}", ChatFormatting.OBFUSCATED);

        content = pair.getFirst();
        style = pair.getSecond();
        int i = content.indexOf("/");

        while (i > 0) {
            builder.add((MutableComponent) Component.translatable(content.substring(0, i)).setStyle(style));
            content = content.substring(i + 1);
            i = content.indexOf("/");
        }

        builder.add((MutableComponent) Component.translatable(content).setStyle(style));

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

        return new Pair<String, Style>(content, style);
    }

    public static String getMessageString() {
        return message.getString();
    }

    public static void reduceDisplayTime() {
        reduceDisplayTime(1);
    }

    public static void reduceDisplayTime(int amount) {
        displayTime -= amount;
        if (displayTime <= 0) {
            displayTime = 0;
            message = Component.translatable("rottentothecore.thoughts.blank");
        }
    }
}
