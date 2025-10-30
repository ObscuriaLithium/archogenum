package dev.obscuria.archogenum.fabric;

import dev.obscuria.archogenum.client.KeyMappings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class FabricArchogenumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyMappings.COLLECTION);
    }
}
