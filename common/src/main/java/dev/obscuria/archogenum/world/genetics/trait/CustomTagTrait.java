package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public record CustomTagTrait(
        ResourceLocation tag
) implements ITrait {

    public static final Codec<CustomTagTrait> CODEC;

    @Override
    public Codec<CustomTagTrait> codec() {
        return CODEC;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("tag").forGetter(CustomTagTrait::tag)
        ).apply(codec, CustomTagTrait::new));
    }
}
