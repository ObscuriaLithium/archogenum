package dev.obscuria.archogenum.mixin;

import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void modifyIsInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue()) return;
        final var self = (Entity) (Object) this;
        if (!(self instanceof LivingEntity entity)) return;
        if (!XenotypeHandler.isInvulnerableTo(entity, source)) return;
        info.setReturnValue(true);
    }
}
