package dev.obscuria.archogenum.network;

import dev.obscuria.archogenum.world.genetics.resource.GeneticProfile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ClientboundProfilePayload(GeneticProfile profile) {

    public static void encode(ClientboundProfilePayload payload, FriendlyByteBuf buf) {
        GeneticProfile.Instance.NETWORK_CODEC.write(buf, payload.profile());
    }

    public static ClientboundProfilePayload decode(FriendlyByteBuf buf) {
        return new ClientboundProfilePayload(GeneticProfile.Instance.NETWORK_CODEC.read(buf));
    }

    public static void handle(Player player, ClientboundProfilePayload payload) {
        ClientNetworkHandler.handle(player, payload);
    }
}
