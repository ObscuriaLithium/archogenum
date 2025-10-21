package dev.obscuria.archogenum.network;

import dev.obscuria.archogenum.registry.ArchoItems;
import dev.obscuria.archogenum.server.ServerGenetics;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import net.minecraft.world.entity.player.Player;

public interface ServerNetworkHandler {

    static void handle(Player player, ServerboundSynthesizePayload payload) {
        final var stack = ArchoItems.XENOFRUIT.instantiate();
        EchoVesselItem.setStoredGenes(stack, StoredGenes.of(payload.xenotype()));
        if (player.addItem(stack)) player.drop(stack, true);
    }

    static void handle(Player player, ServerboundSaveXenotypePayload payload) {
        final var profile = ServerGenetics.INSTANCE.profileOf(player);
        profile.xenotypes().add(payload.xenotype());
    }
}
