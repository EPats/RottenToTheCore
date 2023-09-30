package io.github.epats.rottentothecore.common.event;

import io.github.epats.rottentothecore.RottenFamilyTree;
import io.github.epats.rottentothecore.common.capability.PlayerCapability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles various events on the Mod bus for the RottenToTheCore mod.
 */
@Mod.EventBusSubscriber(modid = RottenFamilyTree.ROTTEN_TO_THE_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEventsHandler {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(PlayerCapability.class);
    }
}