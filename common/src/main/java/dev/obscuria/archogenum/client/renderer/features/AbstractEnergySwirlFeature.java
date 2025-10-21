package dev.obscuria.archogenum.client.renderer.features;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.client.renderer.FeatureModel;
import dev.obscuria.archogenum.client.renderer.HumanoidPart;
import dev.obscuria.archogenum.client.renderer.XenotypeFeature;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;

public abstract class AbstractEnergySwirlFeature<T extends LivingEntity> extends XenotypeFeature<T> {

    public AbstractEnergySwirlFeature(EntityRendererProvider.Context context, FeatureModel<T> model) {
        super(context, model);
    }

    public static LayerDefinition createLayer(int width, int height) {
        final var definition = XenotypeFeature.createMesh();
        final var root = definition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.2F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.2F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
        return LayerDefinition.create(definition, width, height);
    }

    abstract float xOffset(float timer);

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        final var timer = (float)entity.tickCount + partialTicks;
        final var consumer = buffer.getBuffer(RenderType.energySwirl(getTexture(entity), xOffset(timer) % 1f, timer * 0.01f % 1f));
        this.model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }

    @Override
    public boolean isPartAllowed(HumanoidPart part) {
        return part != HumanoidPart.HAT;
    }
}
