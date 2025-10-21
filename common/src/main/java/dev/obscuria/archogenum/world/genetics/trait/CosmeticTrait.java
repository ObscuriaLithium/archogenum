package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import net.minecraft.resources.ResourceLocation;

public record CosmeticTrait(
        ResourceLocation feature
) implements ITrait {

    public static final Codec<CosmeticTrait> CODEC;

    @Override
    public Codec<CosmeticTrait> codec() {
        return CODEC;
    }

    @Override
    public GeneCategory defaultCategory() {
        return GeneCategory.COSMETIC;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("feature").forGetter(CosmeticTrait::feature)
        ).apply(codec, CosmeticTrait::new));
    }
}
