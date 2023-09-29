package io.github.epats.rottentothecore.common.event;

import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.ModFamilyTree;
import io.github.epats.rottentothecore.RottenToTheCore;
import io.github.epats.rottentothecore.common.capability.CapabilityCore;
import io.github.epats.rottentothecore.common.capability.PlayerCapabilityProvider;
import io.github.epats.rottentothecore.common.message.ClientBoundPacketThoughts;
import io.github.epats.rottentothecore.common.message.MessageRegistry;
import io.github.epats.rottentothecore.client.render.Thought;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles various events on the Forge bus for the RottenToTheCore mod.
 * These events are primarily related to player capabilities and interactions.
 */
@Mod.EventBusSubscriber(modid = ModFamilyTree.ROTTEN_TO_THE_CORE, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEventsHandler {

    /**
     * Attaches the PlayerCapability to the Player entity when it is created.
     * This ensures that the capability is available for use with the player entity throughout its lifecycle.
     *
     * @param event The event providing details about the entity being created.
     */
    @SubscribeEvent
    public static void attachCapabilitiesToEntityEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player && !player.level().isClientSide) {
            if (!player.getCapability(PlayerCapabilityProvider.playerCap).isPresent()) {
                event.addCapability(CapabilityCore.PLAYER_CAPABILITY, new PlayerCapabilityProvider());
            }
        }
    }

    /**
     * Handles the event when a player respawns after death.
     * Copies the player capabilities from the original player entity to the new player entity.
     *
     * @param event The event providing details about the player respawn.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();

            event.getOriginal().getCapability(PlayerCapabilityProvider.playerCap).ifPresent(
                    oldCap -> event.getEntity().getCapability(PlayerCapabilityProvider.playerCap)
                            .ifPresent(newCap -> newCap.resetFromOld(oldCap)));

            event.getOriginal().invalidateCaps();
        }
    }


    /**
     * Debug event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(PlayerInteractEvent.RightClickBlock event) {
        Player playerEntity = event.getEntity();
        if(!playerEntity.level().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && playerEntity.isCrouching()) {
            MutableComponent message = Component.translatable(ModFamilyTree.ROTTEN_TO_THE_CORE +
                            ".thoughts.debug" + (1 + playerEntity.level().random.nextInt(3)))
                             .withStyle(ChatFormatting.YELLOW);
            Thought thought = new Thought(message, Config.ClientConfig.thoughtDisplayTicks, 100,
                    Config.ClientConfig.thoughtDisplayTicks - 100, Thought.ReplacementBehaviour.QUICK_FADE);
            MessageRegistry.sendToPlayer(new ClientBoundPacketThoughts(thought), (ServerPlayer) playerEntity);
            RottenToTheCore.LOGGER.debug("Sent message");
        }
    }

}
