package io.github.epats.rottentothecore;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Config
{
    @Mod.EventBusSubscriber(modid = RottenToTheCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ServerConfig {

        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        static final ForgeConfigSpec SPEC = BUILDER.build();

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event)
        {
            if(event.getConfig().getType() != ModConfig.Type.SERVER)
                return;
        }
    }


    @Mod.EventBusSubscriber(modid = RottenToTheCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientConfig {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        private static final ForgeConfigSpec.BooleanValue ENABLE_THOUGHTS = BUILDER
                .comment("Whether to render player thoughts")
                .define("enablePlayerThoughts", true);

        static final ForgeConfigSpec SPEC = BUILDER.build();

        public static boolean enableThoughts = true;

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
            if(event.getConfig().getType() != ModConfig.Type.CLIENT)
                return;
            enableThoughts = ENABLE_THOUGHTS.get();
            RottenToTheCore.LOGGER.info("ENABLE THOUGHTS >> {}", enableThoughts);
        }
    }


    @Mod.EventBusSubscriber(modid = RottenToTheCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonConfig {

        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
                .comment("Whether to log the dirt block on common setup")
                .define("logDirtBlock", true);

        private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
                .comment("A magic number")
                .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

        public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
                .comment("What you want the introduction message to be for the magic number")
                .define("magicNumberIntroduction", "The magic number is... ");

        // a list of strings that are treated as resource locations for items
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
                .comment("A list of items to log on common setup.")
                .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config.CommonConfig::validateItemName);

        static final ForgeConfigSpec SPEC = BUILDER.build();

        public static boolean logDirtBlock;
        public static int magicNumber;
        public static String magicNumberIntroduction;
        public static Set<Item> items;

        private static boolean validateItemName(final Object obj)
        {
            return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
            if(event.getConfig().getType() != ModConfig.Type.COMMON)
                return;
            RottenToTheCore.LOGGER.info("LOG DIRT BLOCK? >> {}", LOG_DIRT_BLOCK.get());
            logDirtBlock = LOG_DIRT_BLOCK.get();
            magicNumber = MAGIC_NUMBER.get();
            magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

            // convert the list of strings into a set of items
            items = ITEM_STRINGS.get().stream()
                    .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                    .collect(Collectors.toSet());
        }

    }

}
