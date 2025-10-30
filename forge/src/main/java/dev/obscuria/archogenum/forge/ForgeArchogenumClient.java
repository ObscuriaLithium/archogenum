package dev.obscuria.archogenum.forge;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.client.KeyMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ForgeArchogenumClient {

    @Mod.EventBusSubscriber(modid = Archogenum.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {

        @SubscribeEvent
        public static void registerKeyMappings(final RegisterKeyMappingsEvent event) {
            event.register(KeyMappings.COLLECTION);
        }
    }
}
