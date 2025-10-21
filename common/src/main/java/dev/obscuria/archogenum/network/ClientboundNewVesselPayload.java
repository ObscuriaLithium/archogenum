package dev.obscuria.archogenum.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ClientboundNewVesselPayload(ItemStack stack) {

    public static void encode(ClientboundNewVesselPayload payload, FriendlyByteBuf buf) {
        buf.writeItem(payload.stack());
    }

    public static ClientboundNewVesselPayload decode(FriendlyByteBuf buf) {
        return new ClientboundNewVesselPayload(buf.readItem());
    }

    public static void handle(Player player, ClientboundNewVesselPayload payload) {
        ClientNetworkHandler.handle(player, payload);
    }
}
