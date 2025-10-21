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
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class StriderFuzzFeature<T extends LivingEntity> extends XenotypeFeature<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/strider/strider.png");

    private final ModelPart leftTop;
    private final ModelPart leftMiddle;
    private final ModelPart leftBottom;
    private final ModelPart rightTop;
    private final ModelPart rightMiddle;
    private final ModelPart rightBottom;

    public StriderFuzzFeature(EntityRendererProvider.Context context) {
        super(context, new FeatureModel<>(context.bakeLayer(ModelLayers.STRIDER_FUZZ)));

        this.leftTop = this.model.head.getChild("hair_left_top");
        this.leftMiddle = this.model.head.getChild("hair_left_middle");
        this.leftBottom = this.model.head.getChild("hair_left_bottom");
        this.rightTop = this.model.head.getChild("hair_right_top");
        this.rightMiddle = this.model.head.getChild("hair_right_middle");
        this.rightBottom = this.model.head.getChild("hair_right_bottom");

        final var scaleOffset = new Vector3f(-0.5f, -0.5f, -0.5f);
        this.leftTop.offsetScale(scaleOffset);
        this.leftMiddle.offsetScale(scaleOffset);
        this.leftBottom.offsetScale(scaleOffset);
        this.rightTop.offsetScale(scaleOffset);
        this.rightMiddle.offsetScale(scaleOffset);
        this.rightBottom.offsetScale(scaleOffset);
    }

    public static LayerDefinition createLayer() {

        final var definition = XenotypeFeature.createMesh();
        final var root = definition.getRoot();
        final var head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        final var leftTop = head.addOrReplaceChild("hair_left_top", CubeListBuilder.create(), PartPose.offset(4.0F, -8.0F, 0.0F));
        leftTop.addOrReplaceChild("hair_left_top_rotation", CubeListBuilder.create().texOffs(4, 33).addBox(0.0F, 0.0F, -8.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));
        final var leftMiddle = head.addOrReplaceChild("hair_left_middle", CubeListBuilder.create(), PartPose.offset(4.0F, -5.0F, 0.0F));
        leftMiddle.addOrReplaceChild("hair_left_middle_rotation", CubeListBuilder.create().texOffs(4, 49).addBox(0.0F, 0.0F, -8.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));
        final var leftBottom = head.addOrReplaceChild("hair_left_bottom", CubeListBuilder.create(), PartPose.offset(4.0F, -2.0F, 0.0F));
        leftBottom.addOrReplaceChild("hair_left_bottom_rotation", CubeListBuilder.create().texOffs(4, 65).addBox(0.0F, 0.0F, -8.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        final var rightTop = head.addOrReplaceChild("hair_right_top", CubeListBuilder.create(), PartPose.offset(-4.0F, -8.0F, 0.0F));
        rightTop.addOrReplaceChild("hair_right_top_rotation", CubeListBuilder.create().texOffs(4, 33).mirror().addBox(-12.0F, 0.0F, -8.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));
        final var rightMiddle = head.addOrReplaceChild("hair_right_middle", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.0F, 0.0F));
        rightMiddle.addOrReplaceChild("hair_right_middle_rotation", CubeListBuilder.create().texOffs(4, 49).mirror().addBox(-12.0F, 0.0F, -8.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));
        final var rightBottom = head.addOrReplaceChild("hair_right_bottom", CubeListBuilder.create(), PartPose.offset(-4.0F, -2.0F, 0.0F));
        rightBottom.addOrReplaceChild("hair_right_bottom_rotation", CubeListBuilder.create().texOffs(4, 65).mirror().addBox(-12.0F, 0.0F, -8.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        return LayerDefinition.create(definition, 64, 128);
    }

    @Override
    public void animate(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        final var swing = Mth.cos(limbSwing * 0.5F + (float)Math.PI) * limbSwingAmount * 0.5f;
        this.rightBottom.zRot = swing * 1.3F;
        this.rightMiddle.zRot = swing * 1.2F;
        this.rightTop.zRot = swing * 0.6F;
        this.leftTop.zRot = swing * 0.6F;
        this.leftMiddle.zRot = swing * 1.2F;
        this.leftBottom.zRot = swing * 1.3F;

        this.rightBottom.zRot += 0.05F * Mth.sin(ageInTicks * 1.0F * -0.4F);
        this.rightMiddle.zRot += 0.1F * Mth.sin(ageInTicks * 1.0F * 0.2F);
        this.rightTop.zRot += 0.1F * Mth.sin(ageInTicks * 1.0F * 0.4F);
        this.leftTop.zRot += 0.1F * Mth.sin(ageInTicks * 1.0F * 0.4F);
        this.leftMiddle.zRot += 0.1F * Mth.sin(ageInTicks * 1.0F * 0.2F);
        this.leftBottom.zRot += 0.05F * Mth.sin(ageInTicks * 1.0F * -0.4F);
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
