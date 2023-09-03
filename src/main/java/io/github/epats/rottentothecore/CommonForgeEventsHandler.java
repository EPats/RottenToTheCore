package io.github.epats.rottentothecore;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = RottenToTheCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEventsHandler {

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player && !player.level().isClientSide) {
            if (!player.getCapability(PlayerCapabilityProvider.playerCap).isPresent()) {
                event.addCapability(CapabilityCore.PLAYER_CAPABILITY, new PlayerCapabilityProvider());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();

            event.getOriginal().getCapability(PlayerCapabilityProvider.playerCap).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerCapabilityProvider.playerCap).ifPresent(newStore -> {
                    RottenToTheCore.LOGGER.info("hi");
                });
            });

            event.getOriginal().invalidateCaps();
        }
    }
}
