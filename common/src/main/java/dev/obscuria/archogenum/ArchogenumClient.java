package dev.obscuria.archogenum;

import dev.obscuria.archogenum.client.ModelLayers;
import dev.obscuria.archogenum.client.renderer.features.*;
import dev.obscuria.archogenum.client.renderer.XenotypeFeatureRegistry;
import dev.obscuria.archogenum.registry.ArchoItems;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import dev.obscuria.fragmentum.client.FragmentumClientRegistry;

public interface ArchogenumClient {

    static void init() {

        final var registrar = FragmentumClientRegistry.registrar(Archogenum.MODID);
        registrar.registerItemColor(EchoVesselItem::getOverlayColor, ArchoItems.ECHO_VESSEL);

        ModelLayers.register(registrar);
        XenotypeFeatureRegistry.register(Archogenum.key("pig_snout"), PigSnoutFeature::new);
        XenotypeFeatureRegistry.register(Archogenum.key("chicken_beak"), ChickenBeakFeature::new);
        XenotypeFeatureRegistry.register(Archogenum.key("strider_fuzz"), StriderFuzzFeature::new);
        XenotypeFeatureRegistry.register(Archogenum.key("energy_barrier"), EnergyBarrierFeature::new);
        XenotypeFeatureRegistry.register(Archogenum.key("power_aura"), PowerAuraFeature::new);
        XenotypeFeatureRegistry.register(Archogenum.key("vex_wings"), VexWingsFeature::new);
        XenotypeFeatureRegistry.register(Archogenum.key("warden_tendrils"), WardenTendrilsFeature::new);
    }
}
