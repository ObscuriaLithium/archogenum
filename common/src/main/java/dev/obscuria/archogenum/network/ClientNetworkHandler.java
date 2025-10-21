package dev.obscuria.archogenum.network;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.toast.NewGeneToast;
import dev.obscuria.archogenum.client.toast.NewVesselToast;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public interface ClientNetworkHandler {

    static void handle(Player player, ClientboundProfilePayload payload) {
        ClientGeneticProfile.INSTANCE.updateFrom(payload.profile());
    }

    static void handle(Player player, ClientboundNewVesselPayload payload) {
        Minecraft.getInstance().getToasts().addToast(new NewVesselToast(payload.stack()));
    }

    static void handle(Player player, ClientboundNewGenePayload payload) {
        final var lookup = player.level().registryAccess().registryOrThrow(ArchoRegistries.Keys.GENE);
        final var gene = lookup.getHolderOrThrow(payload.gene());
        Minecraft.getInstance().getToasts().addToast(new NewGeneToast(gene));
    }
}
