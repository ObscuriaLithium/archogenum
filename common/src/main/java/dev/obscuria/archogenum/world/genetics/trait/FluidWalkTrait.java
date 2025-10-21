package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public record FluidWalkTrait(
        TagKey<Fluid> fluidTag
) implements ITrait {

    public static final Codec<FluidWalkTrait> CODEC;

    @Override
    public Codec<FluidWalkTrait> codec() {
        return CODEC;
    }

    @Override
    public boolean canStandOnFluid(FluidState state) {
        return state.is(fluidTag);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TagKey.codec(Registries.FLUID).fieldOf("fluid_tag").forGetter(FluidWalkTrait::fluidTag)
        ).apply(codec, FluidWalkTrait::new));
    }
}
