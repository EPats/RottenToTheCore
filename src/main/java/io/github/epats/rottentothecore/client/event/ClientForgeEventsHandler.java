package io.github.epats.rottentothecore.client.event;

import io.github.epats.rottentothecore.RottenFamilyTree;
import io.github.epats.rottentothecore.client.ClientData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RottenFamilyTree.ROTTEN_TO_THE_CORE, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventsHandler {

    // Render thoughts over other elements
    @SubscribeEvent
    public static void postOverlayRender(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            ClientData.getInstance().tickThoughts(event.getWindow(), event.getGuiGraphics());
        }
    }
}
