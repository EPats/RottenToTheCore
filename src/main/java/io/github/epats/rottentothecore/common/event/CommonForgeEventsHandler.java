package io.github.epats.rottentothecore.common.event;

import io.github.epats.rottentothecore.common.capability.CapabilityCore;
import io.github.epats.rottentothecore.common.capability.PlayerCapabilityProvider;
import io.github.epats.rottentothecore.RottenToTheCore;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = RottenToTheCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

            event.getOriginal().getCapability(PlayerCapabilityProvider.playerCap).ifPresent(oldCap -> {
                event.getEntity().getCapability(PlayerCapabilityProvider.playerCap).ifPresent(newCap -> {
                    newCap.resetFromOld(oldCap);
                });
            });

            event.getOriginal().invalidateCaps();
        }
    }

}
