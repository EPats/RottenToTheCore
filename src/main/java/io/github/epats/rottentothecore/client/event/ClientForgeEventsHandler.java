package io.github.epats.rottentothecore.client.event;

import io.github.epats.rottentothecore.RottenToTheCore;
import io.github.epats.rottentothecore.client.ClientData;
import io.github.epats.rottentothecore.client.render.ThoughtsRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RottenToTheCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventsHandler {

    // Render thoughts over other elements
    @SubscribeEvent
    public static void postOverlayRender(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            ThoughtsRenderer.render(event.getWindow(), event.getGuiGraphics());
            RottenToTheCore.LOGGER.info(ClientData.getMessageString());
            //DEBUG
            if (ClientData.getMessageString() == "") {
                ClientData.changeThought(Component
                        .translatable(RottenToTheCore.MOD_ID + ".darkness.gone.message" + (1 + Minecraft.getInstance().level.random.nextInt(10)))
                        .withStyle(ChatFormatting.GOLD));
            }
        }
    }
}
