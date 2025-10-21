package dev.obscuria.archogenum.world.genetics.behavior.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.fragmentum.registry.LazyHolder;
import dev.obscuria.fragmentum.registry.RegistryLazyCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public record DamageEntityEffect(
        LazyHolder<DamageType> damageType,
        double minAmount,
        double maxAmount
) implements IEffect {

    public static final Codec<DamageEntityEffect> CODEC;

    @Override
    public void apply(ServerLevel level, LivingEntity entity, GeneInstance gene) {
        final var damage = Mth.randomBetween(entity.getRandom(), (float) minAmount, (float) maxAmount);
        entity.hurt(new DamageSource(damageType.getOrThrow(), entity), damage);
    }

    @Override
    public Codec<DamageEntityEffect> codec() {
        return CODEC;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                RegistryLazyCodec.create(Registries.DAMAGE_TYPE, ArchogenumProxy::registryAccess).fieldOf("damage_type").forGetter(DamageEntityEffect::damageType),
                Codec.DOUBLE.fieldOf("min_amount").forGetter(DamageEntityEffect::minAmount),
                Codec.DOUBLE.fieldOf("max_amount").forGetter(DamageEntityEffect::maxAmount)
        ).apply(codec, DamageEntityEffect::new));
    }
}
