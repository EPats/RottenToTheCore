package io.github.epats.rottentothecore;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = RottenToTheCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

//    private static final ForgeConfigSpec.BooleanValue ENABLE_THOUGHTS = BUILDER
//            .comment("Whether to render player thoughts")
//            .define("enablePlayerThoughts", true);
    static final ForgeConfigSpec SPEC = BUILDER.build();

//    public static boolean enableThoughts = true;
//
//    @SubscribeEvent
//    static void onLoad(final ModConfigEvent event)
//    {
//        enableThoughts = ENABLE_THOUGHTS.get();
//    }
}