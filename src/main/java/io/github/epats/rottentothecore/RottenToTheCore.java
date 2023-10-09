package io.github.epats.rottentothecore;

import com.mojang.logging.LogUtils;
import io.github.epats.rottentothecore.common.message.MessageRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

/**
 * The main class for the "Rotten To The Core" mod.
 * Initializes configurations, sets up event listeners, and handles various mod lifecycle events.
 */
@Mod(RottenToTheCore.MOD_ID)
public class RottenToTheCore
{
    public static final String MOD_ID = RottenFamilyTree.ROTTEN_TO_THE_CORE;
    private static final String NAME = "Rotten To The Core";
    private static final String VERSION = "0.0.5a";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public RottenToTheCore()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // Server is synced to client - use this for most instances.
        // Client is used for display only values.
        // Common is noW synced so try not to use. Exception: things that need the world to be loaded first! Commands and world gen.
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.ServerConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.ClientConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CommonConfig.SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.CommonConfig.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.CommonConfig.magicNumberIntroduction + Config.CommonConfig.magicNumber);

        Config.CommonConfig.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        event.enqueueWork(MessageRegistry::register);

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            LOGGER.info("ENABLE THOUGHTS ENABLED?>> {}", Config.ClientConfig.enableThoughts);
        }
    }
}
