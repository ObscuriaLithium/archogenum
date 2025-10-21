package dev.obscuria.archogenum.network;

import dev.obscuria.archogenum.world.genetics.Xenotype;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ServerboundSynthesizePayload(Xenotype xenotype) {

    public static void encode(ServerboundSynthesizePayload payload, FriendlyByteBuf buf) {
        Xenotype.NETWORK_CODEC.write(buf, payload.xenotype());
    }

    public static ServerboundSynthesizePayload decode(FriendlyByteBuf buf) {
        return new ServerboundSynthesizePayload(Xenotype.NETWORK_CODEC.read(buf));
    }

    public static void handle(Player player, ServerboundSynthesizePayload payload) {
        ServerNetworkHandler.handle(player, payload);
    }
}
