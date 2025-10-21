package dev.obscuria.archogenum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {

    @Inject(method = "restoreFrom", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;onUpdateAbilities()V"))
    private void onRestore(ServerPlayer from, boolean keepEverything, CallbackInfo info) {
        XenotypeHandler.restore(from, (ServerPlayer) (Object) this);
    }

    @WrapOperation(method = "restoreFrom", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean modifyRestoreFrom(GameRules instance,
                                      GameRules.Key<GameRules.BooleanValue> key,
                                      Operation<Boolean> original,
                                      @Local(argsOnly = true) ServerPlayer that) {
        return key != GameRules.RULE_KEEPINVENTORY || !Archogenum.shouldKeepInventory(that)
                ? original.call(instance, key)
                : true;
    }
}
