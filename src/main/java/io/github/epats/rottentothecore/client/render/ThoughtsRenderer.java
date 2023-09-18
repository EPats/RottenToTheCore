package io.github.epats.rottentothecore.client.render;

import com.mojang.blaze3d.platform.Window;
import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.RottenToTheCore;
import io.github.epats.rottentothecore.client.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class ThoughtsRenderer {
    public static void render(Window window, GuiGraphics guiGraphics) {
        if (ClientData.displayTime > 0 && Config.ClientConfig.enableThoughts && !ClientData.isBlankThought()) {
            Minecraft mc = Minecraft.getInstance();
            if(mc.isPaused())
                return;

            List<MutableComponent> messages = ClientData.getMessageSplit();
            int fadeIn = 200;
            int fadeOut = 100;
            float f;
            int displayTime = ClientData.displayTime;
            if(displayTime == Config.ClientConfig.thoughtDisplayTicks) {
                f = 0.0F;
            } else if(displayTime < fadeOut) {
                    f = displayTime / ((float) fadeOut);
            } else if (Config.ClientConfig.thoughtDisplayTicks - displayTime < fadeIn) {
                f = (Config.ClientConfig.thoughtDisplayTicks - displayTime) / ((float) fadeIn);
            } else {
                f = 1.0F;
            }

            for (int i = 0; i < messages.size(); i++) {
                MutableComponent message = messages.get(i);
                int width = mc.font.width(message.getVisualOrderText()) / 2;
                int mid = window.getGuiScaledWidth() / 2;
                int alpha = Math.round(f * 255);
                RottenToTheCore.LOGGER.info(ClientData.displayTime + "; " + f + ". Alpha: " + alpha);
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
