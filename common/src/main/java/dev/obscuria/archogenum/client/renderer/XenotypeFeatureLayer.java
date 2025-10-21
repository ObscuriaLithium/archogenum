package dev.obscuria.archogenum.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.archogenum.world.genetics.trait.CosmeticTrait;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class XenotypeFeatureLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final EntityRendererProvider.Context context;

    public XenotypeFeatureLayer(RenderLayerParent<T, M> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.context = context;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {

        for (var gene : XenotypeHandler.getXenotype(entity).genes()) {
            final var traits = gene.gene().value().traits();
            if (traits.isEmpty()) continue;
            for (var trait : traits.get()) {
                if (!(trait.getOrThrow().value() instanceof CosmeticTrait cosmetic)) continue;
                final @Nullable var featureProvider = XenotypeFeatureRegistry.get(cosmetic.feature());
                if (featureProvider == null) continue;
                final var feature = featureProvider.<T>create(context);
                this.getParentModel().copyPropertiesTo(feature.model);

                float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
                float f1 = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
                float f2 = f1 - f;
                if (entity.isPassenger() && entity.getVehicle() instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity)entity.getVehicle();
                    f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
                    f2 = f1 - f;
                    float f3 = Mth.wrapDegrees(f2);
                    if (f3 < -85.0F) {
                        f3 = -85.0F;
                    }

                    if (f3 >= 85.0F) {
                        f3 = 85.0F;
                    }

                    f = f1 - f3;
                    if (f3 * f3 > 2500.0F) {
                        f += f3 * 0.2F;
                    }

                    f2 = f1 - f;
                }

                float f6 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
                if (LivingEntityRenderer.isEntityUpsideDown(entity)) {
                    f6 *= -1.0F;
                    f2 *= -1.0F;
                }

                float f7 = (float)entity.tickCount + partialTicks;
                float f8 = 0.0F;
                float f5 = 0.0F;
                if (!entity.isPassenger() && entity.isAlive()) {
                    f8 = entity.walkAnimation.speed(partialTicks);
                    f5 = entity.walkAnimation.position(partialTicks);
                    if (entity.isBaby()) {
                        f5 *= 3.0F;
                    }

                    if (f8 > 1.0F) {
                        f8 = 1.0F;
                    }
                }

                feature.animate(entity, f5, f8, f7, f2, f6);
                feature.render(pose, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }
}
