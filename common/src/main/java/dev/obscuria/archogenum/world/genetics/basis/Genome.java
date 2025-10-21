package dev.obscuria.archogenum.world.genetics.basis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.fragmentum.network.PayloadCodec;
import dev.obscuria.fragmentum.registry.LazyHolder;
import dev.obscuria.fragmentum.registry.RegistryLazyCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record Genome(
        @Unmodifiable List<EntityType<?>> entities,
        @Unmodifiable List<LazyHolder<Gene>> genes
) {

    public static final Codec<Holder<Genome>> CODEC;
    public static final PayloadCodec<Holder<Genome>> PAYLOAD_CODEC;
    public static final Codec<Genome> DIRECT_CODEC;

    public boolean isEmpty() {
        return genes.isEmpty();
    }

    public Stream<Holder<Gene>> unwrapGenes() {
        return genes.stream().map(LazyHolder::getOrThrow);
    }

    public static Optional<Genome> findFor(EntityType<?> entity) {
        return ArchogenumProxy.registryAccess()
                .registryOrThrow(ArchoRegistries.Keys.GENOME).stream()
                .filter(genome -> genome.entities.contains(entity))
                .findFirst();
    }

    static {
        CODEC = RegistryFixedCodec.create(ArchoRegistries.Keys.GENOME);
        PAYLOAD_CODEC = PayloadCodec.registryFriendly(CODEC, ArchogenumProxy::registryAccess);
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().fieldOf("entities").forGetter(Genome::entities),
                RegistryLazyCodec.create(ArchoRegistries.Keys.GENE, ArchogenumProxy::registryAccess).listOf().fieldOf("genes").forGetter(Genome::genes)
        ).apply(codec, Genome::new));
    }
}
