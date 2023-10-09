package io.github.epats.rottentothecore;

import net.minecraftforge.fml.ModList;

public class RottenFamilyTree {

    public static final String ROTTEN_TO_THE_CORE = "rottentothecore";
    public static final String ROTTEN_NEVERMORE = "rottennevermore";

    private static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
