package dev.obscuria.archogenum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.registry.ArchoAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer {

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void createCustomAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        ArchoAttributes.createPlayerAttributes(info.getReturnValue());
    }

    @WrapOperation(method = "dropEquipment", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean modifyDropEquipment(GameRules instance,
                                        GameRules.Key<GameRules.BooleanValue> key,
                                        Operation<Boolean> original) {
        final var player = (Player) (Object) this;
        return key != GameRules.RULE_KEEPINVENTORY || !Archogenum.shouldKeepInventory(player)
                ? original.call(instance, key)
                : true;
    }

    @WrapOperation(method = "getExperienceReward", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean modifyGetExperienceReward(GameRules instance,
                                              GameRules.Key<GameRules.BooleanValue> key,
                                              Operation<Boolean> original) {
        final var player = (Player) (Object) this;
        return key != GameRules.RULE_KEEPINVENTORY || !Archogenum.shouldKeepInventory(player)
                ? original.call(instance, key)
                : true;
    }
}
