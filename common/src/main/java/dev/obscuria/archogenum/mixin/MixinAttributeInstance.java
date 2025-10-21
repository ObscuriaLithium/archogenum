package dev.obscuria.archogenum.mixin;

import dev.obscuria.archogenum.world.attribute.PercentAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(AttributeInstance.class)
public abstract class MixinAttributeInstance {

    @Shadow @Final private Attribute attribute;

    @Shadow public abstract double getBaseValue();

    @Shadow public abstract Set<AttributeModifier> getModifiers();

    @Inject(method = "calculateValue", at = @At("HEAD"), cancellable = true)
    private void calculatePercentValue(CallbackInfoReturnable<Double> info) {
        if (!(attribute instanceof PercentAttribute)) return;
        final var value = getBaseValue() + getModifiers().stream().mapToDouble(AttributeModifier::getAmount).sum();
        info.setReturnValue(attribute.sanitizeValue(value));
    }
}