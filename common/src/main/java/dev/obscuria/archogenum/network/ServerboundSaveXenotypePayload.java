package dev.obscuria.archogenum.network;

import dev.obscuria.archogenum.world.genetics.Xenotype;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ServerboundSaveXenotypePayload(Xenotype xenotype) {

    public static void encode(ServerboundSaveXenotypePayload payload, FriendlyByteBuf buf) {
        Xenotype.NETWORK_CODEC.write(buf, payload.xenotype());
    }

    public static ServerboundSaveXenotypePayload decode(FriendlyByteBuf buf) {
        return new ServerboundSaveXenotypePayload(Xenotype.NETWORK_CODEC.read(buf));
    }

    public static void handle(Player player, ServerboundSaveXenotypePayload payload) {
        ServerNetworkHandler.handle(player, payload);
    }
}
