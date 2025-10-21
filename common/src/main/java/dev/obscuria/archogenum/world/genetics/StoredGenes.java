package dev.obscuria.archogenum.world.genetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.archogenum.world.genetics.resource.GeneBundle;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.stream.Stream;

public record StoredGenes(
        @Unmodifiable List<GeneInstance> genes
) implements IBundleLike {

    public static final StoredGenes EMPTY = new StoredGenes(List.of());
    public static final Codec<StoredGenes> CODEC;

    public static StoredGenes of(IBundleLike bundle) {
        return new StoredGenes(bundle.genes());
    }

    public boolean isEmpty() {
        return genes.isEmpty();
    }

    public StoredGenes with(GeneInstance gene) {
        if (genes.contains(gene)) return this;
        return new StoredGenes(Stream.concat(genes.stream(), Stream.of(gene)).toList());
    }

    public StoredGenes without(GeneInstance gene) {
        if (!genes.contains(gene)) return this;
        return new StoredGenes(genes.stream().filter(it -> !it.equals(gene)).toList());
    }

    public GeneBundle toBundle() {
        return new GeneBundle(genes);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                GeneInstance.CODEC.listOf().fieldOf("genes").forGetter(StoredGenes::genes)
        ).apply(codec, StoredGenes::new));
    }
}
