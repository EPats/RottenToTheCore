package io.github.epats.rottentothecore.client.render;

import com.mojang.blaze3d.platform.Window;
import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.client.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class ThoughtsRenderer {
    public static void render(Window window, GuiGraphics guiGraphics) {
        if (ClientData.displayTime > 0 && Config.ClientConfig.enableThoughts) {
            Minecraft mc = Minecraft.getInstance();
            List<MutableComponent> messages = ClientData.getMessageSplit();
            int fadeTime = 50;
            float f = ClientData.displayTime > fadeTime ? 1.0F : ClientData.displayTime / ((float) fadeTime);

            for (int i = 0; i < messages.size(); i++) {
                MutableComponent message = messages.get(i);
                int width = mc.font.width(message.getVisualOrderText()) / 2;
                int mid = window.getGuiScaledWidth() / 2;
                int alpha = Math.round(f * 255);

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0.0D, 0.0D, 50.0D);
                guiGraphics.pose().scale(1.0F, 1.0F, 1.0F);
                guiGraphics.drawString(mc.font, message.getVisualOrderText(), mid - width, 40 + 10 * i, 16777215 + (alpha << 24));
                guiGraphics.pose().popPose();

            }
            ClientData.reduceDisplayTime();
        }
    }
}
