package dev.obscuria.archogenum.client.renderer.features;

import dev.obscuria.archogenum.client.ModelLayers;
import dev.obscuria.archogenum.client.renderer.FeatureModel;
import dev.obscuria.archogenum.client.renderer.HumanoidPart;
import dev.obscuria.archogenum.client.renderer.XenotypeFeature;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class WardenTendrilsFeature<T extends LivingEntity> extends XenotypeFeature<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/warden/warden.png");

    public WardenTendrilsFeature(EntityRendererProvider.Context context) {
        super(context, new FeatureModel<>(context.bakeLayer(ModelLayers.WARDEN_TENDRILS)));
    }

    public static LayerDefinition createLayer() {
        final var definition = XenotypeFeature.createMesh();
        final var root = definition.getRoot();
        final var head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("left_tendril", CubeListBuilder.create().texOffs(58, 0).addBox(0.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -7.0F, 0.0F));
        head.addOrReplaceChild("right_tendril", CubeListBuilder.create().texOffs(52, 32).addBox(-16.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -7.0F, 0.0F));
        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public boolean isPartAllowed(HumanoidPart part) {
        return part == HumanoidPart.HEAD;
    }

    @Override
    public ResourceLocation getTexture(LivingEntity entity) {
        return TEXTURE;
    }
}
