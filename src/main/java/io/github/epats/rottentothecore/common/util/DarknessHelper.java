package io.github.epats.rottentothecore.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class DarknessHelper {
    public static boolean isTrueDarkness(Level level, BlockPos pos) {
        if (!(level.dimension() == Level.OVERWORLD)) {
            return false;
        }
        float time = (float) (level.getSunAngle(1.0F) > Math.PI ? 2 * Math.PI - level.getSunAngle(1.0F)
                : level.getSunAngle(1.0F));
        boolean night = time >= 5 * Math.PI / 8;
        int skyLight = level.getLightEngine().skyEngine.getLightValue(pos);
        int blockLight = level.getLightEngine().blockEngine.getLightValue(pos);
        boolean fullDark = skyLight == 0 && blockLight == 0;
        boolean isLight = blockLight > 0 || (blockLight == 0 && skyLight > 16 - 16 * level.getMoonBrightness());
        return fullDark || (!isLight && night);
    }
}
