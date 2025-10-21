package dev.obscuria.archogenum.client;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.client.renderer.features.*;
import dev.obscuria.fragmentum.client.ClientRegistrar;
import net.minecraft.client.model.geom.ModelLayerLocation;

public interface ModelLayers {

    ModelLayerLocation PIG_SNOUT = new ModelLayerLocation(Archogenum.key("pig_snout"), "main");
    ModelLayerLocation CHICKEN_BEAK = new ModelLayerLocation(Archogenum.key("chicken_beak"), "main");
    ModelLayerLocation STRIDER_FUZZ = new ModelLayerLocation(Archogenum.key("strider_fuzz"), "main");
    ModelLayerLocation ENERGY_BARRIER = new ModelLayerLocation(Archogenum.key("energy_barrier"), "main");
    ModelLayerLocation POWER_AURA = new ModelLayerLocation(Archogenum.key("power_aura"), "main");
    ModelLayerLocation VEX_WINGS = new ModelLayerLocation(Archogenum.key("vex_wings"), "main");
    ModelLayerLocation WARDEN_TENDRILS = new ModelLayerLocation(Archogenum.key("warden_tendrils"), "main");

    static void register(ClientRegistrar registrar) {
        registrar.registerModelLayer(PIG_SNOUT, PigSnoutFeature::createLayer);
        registrar.registerModelLayer(CHICKEN_BEAK, ChickenBeakFeature::createLayer);
        registrar.registerModelLayer(STRIDER_FUZZ, StriderFuzzFeature::createLayer);
        registrar.registerModelLayer(ENERGY_BARRIER, EnergyBarrierFeature::createLayer);
        registrar.registerModelLayer(POWER_AURA, PowerAuraFeature::createLayer);
        registrar.registerModelLayer(VEX_WINGS, VexWingsFeature::createLayer);
        registrar.registerModelLayer(WARDEN_TENDRILS, WardenTendrilsFeature::createLayer);
    }
}
