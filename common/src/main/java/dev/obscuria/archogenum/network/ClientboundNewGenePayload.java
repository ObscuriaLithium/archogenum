package dev.obscuria.archogenum.network;

import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

public record ClientboundNewGenePayload(ResourceKey<Gene> gene) {

    public static void encode(ClientboundNewGenePayload payload, FriendlyByteBuf buf) {
        buf.writeResourceKey(payload.gene());
    }

    public static ClientboundNewGenePayload decode(FriendlyByteBuf buf) {
        return new ClientboundNewGenePayload(buf.readResourceKey(ArchoRegistries.Keys.GENE));
    }

    public static void handle(Player player, ClientboundNewGenePayload payload) {
        ClientNetworkHandler.handle(player, payload);
    }
}
