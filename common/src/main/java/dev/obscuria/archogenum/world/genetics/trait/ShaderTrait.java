package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import net.minecraft.resources.ResourceLocation;

public record ShaderTrait(
        ResourceLocation shader
) implements ITrait {

    public static final Codec<ShaderTrait> CODEC;

    @Override
    public Codec<ShaderTrait> codec() {
        return CODEC;
    }

    @Override
    public GeneCategory defaultCategory() {
        return GeneCategory.HARMFUL;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("shader").forGetter(ShaderTrait::shader)
        ).apply(codec, ShaderTrait::new));
    }
}
