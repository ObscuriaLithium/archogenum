package dev.obscuria.archogenum.client.renderer.features;

import dev.obscuria.archogenum.client.ModelLayers;
import dev.obscuria.archogenum.client.renderer.FeatureModel;
import dev.obscuria.archogenum.client.renderer.HumanoidPart;
import dev.obscuria.archogenum.client.renderer.XenotypeFeature;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class VexWingsFeature<T extends LivingEntity> extends XenotypeFeature<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public VexWingsFeature(EntityRendererProvider.Context context) {
        super(context, new FeatureModel<>(context.bakeLayer(ModelLayers.VEX_WINGS)));
        this.leftWing = this.model.body.getChild("left_wing");
        this.rightWing = this.model.body.getChild("right_wing");
        final var scaleOffset = new Vector3f(0.5f, 0.5f, 0.5f);
        this.leftWing.offsetScale(scaleOffset);
        this.rightWing.offsetScale(scaleOffset);
    }

    public static LayerDefinition createLayer() {
        final var definition = XenotypeFeature.createMesh();
        final var root = definition.getRoot();
        final var body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, 2.0F));
        body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, 2.0F));
        return LayerDefinition.create(definition, 32, 32);
    }

    @Override
    public void animate(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        final var swing = ageInTicks * 10f + limbSwing * 30f;
        this.leftWing.yRot = 0.76f + Mth.cos(swing * ((float)Math.PI / 180F)) * ((float)Math.PI / 180F) * (16.2F + limbSwingAmount * 8f);
        this.rightWing.yRot = -this.leftWing.yRot;
        this.leftWing.xRot = 0.47123888F;
        this.leftWing.zRot = -0.2f;
        this.rightWing.xRot = 0.47123888F;
        this.rightWing.zRot = 0.2f;
    }

    @Override
    public boolean isPartAllowed(HumanoidPart part) {
        return part == HumanoidPart.BODY;
    }

    @Override
    public ResourceLocation getTexture(LivingEntity entity) {
        return TEXTURE;
    }

    @Override
    public RenderType getRenderType(LivingEntity entity) {
        return RenderType.entityTranslucentEmissive(getTexture(entity));
    }
}
