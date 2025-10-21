package dev.obscuria.archogenum.mixin;

import dev.obscuria.archogenum.event.Hooks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class MixinFoodData {

    @Unique @Nullable private Player archo$player;

    @Inject(method = "tick", at = @At("HEAD"))
    private void cachePlayer(Player player, CallbackInfo info) {
        archo$player = player;
    }

    @ModifyVariable(method = "addExhaustion", at = @At("HEAD"), argsOnly = true)
    private float modifyExhaustion(float value) {
        if (archo$player == null) return value;
        return Hooks.modifyExhaustion(archo$player, value);
    }
}
