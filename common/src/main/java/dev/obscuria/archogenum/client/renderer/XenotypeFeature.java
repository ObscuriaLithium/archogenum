package dev.obscuria.archogenum.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public abstract class XenotypeFeature<T extends LivingEntity> {

    public final FeatureModel<T> model;

    public static MeshDefinition createMesh() {
        return HumanoidModel.createMesh(CubeDeformation.NONE, 0f);
    }

    public XenotypeFeature(EntityRendererProvider.Context context, FeatureModel<T> model) {
        this.model = model;
        this.setupPartVisibility();
    }

    public abstract boolean isPartAllowed(HumanoidPart part);

    public abstract ResourceLocation getTexture(LivingEntity entity);

    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, LivingEntity entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        final var consumer = buffer.getBuffer(getRenderType(entity));
        this.model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }

    public void animate(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    public void setupPartVisibility() {
        this.model.head.visible = isPartAllowed(HumanoidPart.HEAD);
        this.model.hat.visible = isPartAllowed(HumanoidPart.HAT);
        this.model.body.visible = isPartAllowed(HumanoidPart.BODY);
        this.model.rightArm.visible = isPartAllowed(HumanoidPart.RIGHT_ARM);
        this.model.leftArm.visible = isPartAllowed(HumanoidPart.LEFT_ARM);
        this.model.rightLeg.visible = isPartAllowed(HumanoidPart.RIGHT_LEG);
        this.model.leftLeg.visible = isPartAllowed(HumanoidPart.LEFT_LEG);
    }

    public RenderType getRenderType(LivingEntity entity) {
        return this.model.renderType(getTexture(entity));
    }
}
