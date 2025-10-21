package dev.obscuria.archogenum.mixin;

import dev.obscuria.archogenum.ArchogenumProxy;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Inject(method = "runServer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;"))
    private void onServerStart(CallbackInfo info) {
        final var self = (MinecraftServer) (Object) this;
        ArchogenumProxy.onServerStart(self);
        ArchogenumProxy.onServerLoad(self);
    }

    @Inject(method = "saveEverything", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;saveAll()V"))
    private void onServerSave(boolean suppressLog, boolean flush, boolean forced, CallbackInfoReturnable<Boolean> info) {
        final var self = (MinecraftServer) (Object) this;
        ArchogenumProxy.onServerSave(self);
    }

    @Inject(method = "stopServer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;saveAllChunks(ZZZ)Z"))
    private void onServerStop(CallbackInfo info) {
        final var self = (MinecraftServer) (Object) this;
        ArchogenumProxy.onServerSave(self);
        ArchogenumProxy.onServerStop(self);
    }
}