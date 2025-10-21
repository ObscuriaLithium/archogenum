package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public record DamageImmunityTrait(
        ResourceKey<DamageType> damageType
) implements ITrait {

    public static final Codec<DamageImmunityTrait> CODEC;

    @Override
    public Codec<DamageImmunityTrait> codec() {
        return CODEC;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(damageType);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage_type").forGetter(DamageImmunityTrait::damageType)
        ).apply(codec, DamageImmunityTrait::new));
    }
}
