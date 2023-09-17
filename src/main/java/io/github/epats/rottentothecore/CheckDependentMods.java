package io.github.epats.rottentothecore;

import net.minecraftforge.fml.ModList;

public class CheckDependentMods {

    private static final String ROTTEN_NEVERMORE = "Rotten Nevermore";

    private static boolean hasMod(String modId) {
        return ModList.get().isLoaded(modId);
    }

    private static boolean hasRottenNevermore() {
        return hasMod(ROTTEN_NEVERMORE);
    }
}
