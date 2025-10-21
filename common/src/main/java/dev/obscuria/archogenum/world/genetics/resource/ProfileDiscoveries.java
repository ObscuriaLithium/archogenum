package dev.obscuria.archogenum.world.genetics.resource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.network.ClientboundNewGenePayload;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import dev.obscuria.fragmentum.network.PayloadCodec;
import dev.obscuria.fragmentum.util.signal.Signal0;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public record ProfileDiscoveries(
        List<ResourceLocation> knownGenes,
        Signal0 modified
) {

    public static final Codec<ProfileDiscoveries> CODEC;
    public static final PayloadCodec<ProfileDiscoveries> PAYLOAD_CODEC;

    public static ProfileDiscoveries empty() {
        return new ProfileDiscoveries(new ArrayList<>(), new Signal0());
    }

    public static ProfileDiscoveries load(List<ResourceLocation> knownGenes) {
        return new ProfileDiscoveries(new ArrayList<>(knownGenes), new Signal0());
    }

    public boolean isKnown(Holder<Gene> gene) {
        return knownGenes.contains(gene.unwrapKey().orElseThrow().location());
    }

    public boolean discover(ServerPlayer player, Holder<Gene> gene) {
        if (!discover(gene)) return false;
        FragmentumNetworking.sendTo(player, new ClientboundNewGenePayload(gene.unwrapKey().orElseThrow()));
        return true;
    }

    public boolean discover(Holder<Gene> gene) {
        final var id = gene.unwrapKey().orElseThrow().location();
        if (knownGenes.contains(id)) return false;
        knownGenes.add(id);
        modified.emit();
        return true;
    }

    public boolean forget(Holder<Gene> gene) {
        final var id = gene.unwrapKey().orElseThrow().location();
        if (!knownGenes.contains(id)) return false;
        knownGenes.remove(id);
        modified.emit();
        return true;
    }

    public void updateFrom(ProfileDiscoveries other) {
        knownGenes.clear();
        knownGenes.addAll(other.knownGenes);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.listOf().fieldOf("known_genes").forGetter(ProfileDiscoveries::knownGenes)
        ).apply(codec, ProfileDiscoveries::load));
        PAYLOAD_CODEC = PayloadCodec.registryFriendly(CODEC, ArchogenumProxy::registryAccess);
    }
}
