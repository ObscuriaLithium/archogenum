package dev.obscuria.archogenum.mixin;

import dev.obscuria.archogenum.event.Hooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Inject(method = "getRespiration", at = @At("RETURN"), cancellable = true)
    private static void modifyRespiration(LivingEntity entity, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(Hooks.modifyRespiration(entity, info.getReturnValue()));
    }
}
