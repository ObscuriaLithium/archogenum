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

public class PigSnoutFeature<T extends LivingEntity> extends XenotypeFeature<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/piglin/piglin.png");

    public PigSnoutFeature(EntityRendererProvider.Context context) {
        super(context, new FeatureModel<>(context.bakeLayer(ModelLayers.PIG_SNOUT)));
    }

    public static LayerDefinition createLayer() {
        final var definition = XenotypeFeature.createMesh();
        final var root = definition.getRoot();
        root.addOrReplaceChild(HumanoidPart.HEAD.partName, CubeListBuilder.create()
                        .texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(definition, 64, 64);
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
