package dev.obscuria.archogenum.mixin.client;

import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow @Nullable PostChain postEffect;
    @Unique private Xenotype archogenum$lastXenotype = Xenotype.EMPTY;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo info) {
        if (!(Minecraft.getInstance().getCameraEntity() instanceof LivingEntity entity)) return;
        final var xenotype = XenotypeHandler.getXenotype(entity);
        if (Objects.equals(xenotype, this.archogenum$lastXenotype)) return;
        this.archogenum$lastXenotype = xenotype;
        this.checkEntityPostEffect(entity);
    }

    @Inject(method = "checkEntityPostEffect", at = @At("TAIL"))
    private void applyXenotypePostEffects(Entity entity, CallbackInfo info) {
        if (postEffect != null || !(entity instanceof LivingEntity living)) return;
        XenotypeHandler.applyPostEffect(living, this::loadEffect);
    }

    @Shadow public abstract void checkEntityPostEffect(@Nullable Entity entity);

    @Shadow abstract void loadEffect(ResourceLocation location);
}
