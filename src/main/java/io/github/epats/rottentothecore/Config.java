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

        private static final ForgeConfigSpec.BooleanValue ENABLE_MONSTER_IN_THE_DARK = BUILDER
                .comment("Whether to let loose the monster in the darkness")
                .define("letLooseMonsterInTheDark", true);
        private static final ForgeConfigSpec.IntValue DARKNESS_MAX = BUILDER
                .comment("")
                .comment("-----DARKNESS VALUES-----")
                .comment("")
                .comment("Darkness counter max value")
                .defineInRange("darkness_max", 1000, 0, 5000);

        // a list of strings that are treated as resource locations for items
        private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> DARKNESS_SCARE_MESSAGES = BUILDER
                .comment("A list of darkness tick counts at which to send scared thoughts")
                .defineListAllowEmpty("darkness_scare_", List.of(300, 600), num -> true);

        private static final ForgeConfigSpec.IntValue DARKNESS_EERIE_THRESHOLD = BUILDER
                .comment("---Thresholds: Set to above Max Darkness to never occur")
                .comment("Darkness threshold for eerie effects")
                .defineInRange("darkness_eerie_threshold", 80, 0, 5000);

        private static final ForgeConfigSpec.IntValue DARKNESS_SCARE_THRESHOLD = BUILDER
                .comment("Darkness threshold for scare effects")
                .defineInRange("darkness_scare_threshold", 400, 0, 5000);

        private static final ForgeConfigSpec.IntValue DARKNESS_ATTACK_THRESHOLD = BUILDER
                .comment("Darkness threshold for monster attacks")
                .defineInRange("darkness_attack_threshold", 800, 0, 5000);
        private static final ForgeConfigSpec.IntValue DARKNESS_EERIE_FREQUENCY = BUILDER
                .comment("Darkness frequency at which eeriness happen once threshold met")
                .defineInRange("darkness_eerie_frequency", 33, 0, 5000);
        private static final ForgeConfigSpec.IntValue DARKNESS_SCARE_FREQUENCY = BUILDER
                .comment("Darkness frequency at which scares happen once threshold met")
                .defineInRange("darkness_scare_frequency", 59, 0, 5000);
        private static final ForgeConfigSpec.IntValue DARKNESS_ATTACK_FREQUENCY = BUILDER
                .comment("Darkness frequency at which attacks happen once threshold met")
                .defineInRange("darkness_attack_frequency", 200, 0, 5000);

        private static final ForgeConfigSpec.IntValue SANITY_START = BUILDER
                .comment("")
                .comment("-----SANITY VALUES-----")
                .comment("")
                .comment("Sanity default starting value")
                .defineInRange("sanity_starting", 1000, 0, 5000);
        private static final ForgeConfigSpec.IntValue SANITY_MIN = BUILDER
                .comment("Sanity min value")
                .defineInRange("sanity_min", 0, 0, 5000);
        private static final ForgeConfigSpec.IntValue SANITY_MAX = BUILDER
                .comment("Sanity max value")
                .defineInRange("sanity_max", 1000, 0, 5000);

        static final ForgeConfigSpec SPEC = BUILDER.build();


        public static boolean enableMonsterInTheDarkness;
        public static int darknessMaxValue;
        public static Set<Integer> darknessScareMessagePoints;
        public static int darknessEerieThreshold;
        public static int darknessEerieFrequency;
        public static int darknessScaryThreshold;
        public static int darknessScaryFrequency;
        public static int darknessAttackThreshold;
        public static int darknessAttackFrequency;
        public static int sanityStartingValue;
        public static int sanityMinValue;
        public static int sanityMaxValue;



        @SubscribeEvent
        static void onLoad(final ModConfigEvent event)
        {
            if(event.getConfig().getType() != ModConfig.Type.SERVER)
                return;

            enableMonsterInTheDarkness = ENABLE_MONSTER_IN_THE_DARK.get();
            darknessMaxValue = DARKNESS_MAX.get();
            darknessScareMessagePoints = DARKNESS_SCARE_MESSAGES.get().stream().collect(Collectors.toSet());
            darknessEerieThreshold = DARKNESS_EERIE_THRESHOLD.get();
            darknessScaryThreshold = DARKNESS_SCARE_THRESHOLD.get();
            darknessAttackThreshold = DARKNESS_ATTACK_THRESHOLD.get();
            darknessEerieFrequency = DARKNESS_EERIE_FREQUENCY.get();
            darknessScaryFrequency = DARKNESS_SCARE_FREQUENCY.get();
            darknessAttackFrequency = DARKNESS_ATTACK_FREQUENCY.get();
            sanityStartingValue = SANITY_START.get();
            sanityMinValue = SANITY_MIN.get();
            sanityMaxValue = SANITY_MAX.get();
        }
    }


    @Mod.EventBusSubscriber(modid = RottenToTheCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientConfig {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        private static final ForgeConfigSpec.BooleanValue ENABLE_THOUGHTS = BUILDER
                .comment("Whether to render player thoughts")
                .define("enablePlayerThoughts", true);
        private static final ForgeConfigSpec.IntValue THOUGHT_DISPLAY_LENGTH = BUILDER
                .comment("Ticks to display a thought")
                .defineInRange("thoughtsLength", 300, 0, Integer.MAX_VALUE);


        static final ForgeConfigSpec SPEC = BUILDER.build();

        public static boolean enableThoughts;
        public static int thoughtDisplayTicks;

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
            if(event.getConfig().getType() != ModConfig.Type.CLIENT)
                return;
            enableThoughts = ENABLE_THOUGHTS.get();
            RottenToTheCore.LOGGER.info("ENABLE THOUGHTS >> {}", enableThoughts);
            thoughtDisplayTicks = THOUGHT_DISPLAY_LENGTH.get();
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
