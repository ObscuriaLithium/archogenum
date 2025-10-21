package dev.obscuria.archogenum.world.item;

import com.mojang.datafixers.util.Pair;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.network.ClientboundNewVesselPayload;
import dev.obscuria.archogenum.server.ServerGenetics;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import dev.obscuria.fragmentum.world.tooltip.TooltipOptions;
import dev.obscuria.fragmentum.world.tooltip.Tooltips;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EchoVesselItem extends Item {

    private static final Component DESCRIPTION = Component.translatable("item.archogenum.echo_vessel.desc");

    public EchoVesselItem(Properties properties) {
        super(properties);
    }

    public static StoredGenes getStoredGenes(ItemStack stack) {
        final @Nullable var tag = stack.getTag();
        if (tag == null) return StoredGenes.EMPTY;
        final var ops = RegistryOps.create(NbtOps.INSTANCE, ArchogenumProxy.registryAccess());
        final var result = StoredGenes.CODEC.decode(ops, tag.getCompound("StoredGenes"));
        return result.result().map(Pair::getFirst).orElse(StoredGenes.EMPTY);
    }

    public static void setStoredGenes(ItemStack stack, StoredGenes genes) {
        final var ops = RegistryOps.create(NbtOps.INSTANCE, ArchogenumProxy.registryAccess());
        final var result = StoredGenes.CODEC.encodeStart(ops, genes);
        result.result().ifPresent(tag -> stack.getOrCreateTag().put("StoredGenes", tag));
    }

    public static int getOverlayColor(ItemStack stack, int layer) {
        if (layer != 1) return -1;
        final var storedGenes = getStoredGenes(stack);
        if (storedGenes.isEmpty()) return -1;
        return storedGenes.genes().get(0).gene().value().color().decimal();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.addAll(Tooltips.process(DESCRIPTION, null, TooltipOptions.DESCRIPTION));
        tooltip.add(CommonComponents.EMPTY);

        final var storedGenes = getStoredGenes(stack);
        if (storedGenes.isEmpty()) {
            tooltip.add(Component.literal("Empty"));
        } else {
            for (var gene : storedGenes.genes()) {
                tooltip.add(gene.getDisplayName());
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final var stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            final var profile = ServerGenetics.INSTANCE.profileOf(player);
            final var bundle = getStoredGenes(stack).toBundle();
            if (profile.bundles().add(bundle)) {
                if (!player.getAbilities().instabuild) stack.shrink(1);
                FragmentumNetworking.sendTo(serverPlayer, new ClientboundNewVesselPayload(stack));
                for (var gene : bundle.genes()) {
                    profile.discoveries().discover(serverPlayer, gene.gene());
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
