package dev.obscuria.archogenum.client.renderer.features;

import dev.obscuria.archogenum.client.ModelLayers;
import dev.obscuria.archogenum.client.renderer.FeatureModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class PowerAuraFeature<T extends LivingEntity> extends AbstractEnergySwirlFeature<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    public PowerAuraFeature(EntityRendererProvider.Context context) {
        super(context, new FeatureModel<>(context.bakeLayer(ModelLayers.POWER_AURA)));
    }

    public static LayerDefinition createLayer() {
        return AbstractEnergySwirlFeature.createLayer(64, 32);
    }

    @Override
    public ResourceLocation getTexture(LivingEntity entity) {
        return TEXTURE;
    }

    @Override
    float xOffset(float timer) {
        return timer * 0.01f;
    }
}
