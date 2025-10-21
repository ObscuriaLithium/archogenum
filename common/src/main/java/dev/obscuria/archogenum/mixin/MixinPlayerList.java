package dev.obscuria.archogenum.mixin;

import dev.obscuria.archogenum.ArchogenumProxy;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {

    @Inject(method = "placeNewPlayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;getServerResourcePack()Ljava/util/Optional;"))
    private void onPlayerJoin(Connection connection, ServerPlayer player, CallbackInfo info) {
        ArchogenumProxy.onPlayerJoin(player);
    }

    @Inject(method = "remove", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;getAdvancements()Lnet/minecraft/server/PlayerAdvancements;"))
    private void onPlayerLeave(ServerPlayer player, CallbackInfo info) {
        ArchogenumProxy.onPlayerLeave(player);
    }
}
