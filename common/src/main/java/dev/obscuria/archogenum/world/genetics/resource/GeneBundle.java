package dev.obscuria.archogenum.world.genetics.resource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record GeneBundle(
        @Unmodifiable List<GeneInstance> genes
) implements IBundleLike {

    public static final Codec<GeneBundle> CODEC;

    public boolean isEmpty() {
        return genes.isEmpty();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                GeneInstance.CODEC.listOf().fieldOf("genes").forGetter(GeneBundle::genes)
        ).apply(codec, GeneBundle::new));
    }
}
