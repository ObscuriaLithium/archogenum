package dev.obscuria.archogenum.client.renderer.features;

import dev.obscuria.archogenum.client.ModelLayers;
import dev.obscuria.archogenum.client.renderer.FeatureModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class EnergyBarrierFeature<T extends LivingEntity> extends AbstractEnergySwirlFeature<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/wither/wither_armor.png");

    public EnergyBarrierFeature(EntityRendererProvider.Context context) {
        super(context, new FeatureModel<>(context.bakeLayer(ModelLayers.ENERGY_BARRIER)));
    }

    public static LayerDefinition createLayer() {
        return AbstractEnergySwirlFeature.createLayer(64, 64);
    }

    @Override
    public ResourceLocation getTexture(LivingEntity entity) {
        return TEXTURE;
    }

    @Override
    float xOffset(float timer) {
        return Mth.cos(timer * 0.02F) * 3.0F;
    }
}
