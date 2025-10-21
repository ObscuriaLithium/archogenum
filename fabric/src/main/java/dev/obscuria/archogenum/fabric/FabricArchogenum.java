package dev.obscuria.archogenum.fabric;

import dev.obscuria.archogenum.Archogenum;
import net.fabricmc.api.ModInitializer;

public class FabricArchogenum implements ModInitializer {

    @Override
    public void onInitialize() {
        Archogenum.init();
    }
}