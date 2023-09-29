package io.github.epats.rottentothecore;

import net.minecraftforge.fml.ModList;

public class ModFamilyTree {

    public static final String ROTTEN_TO_THE_CORE = "rottentothecore";

    private static final String ROTTEN_NEVERMORE = "rottennevermore";

    private static boolean hasMod(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
