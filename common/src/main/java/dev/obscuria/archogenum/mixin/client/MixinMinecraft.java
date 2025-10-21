package dev.obscuria.archogenum.mixin.client;

import dev.obscuria.archogenum.client.KeyMappings;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if (!KeyMappings.COLLECTION.consumeClick()) return;
        KeyMappings.collectionPressed();
    }
}
