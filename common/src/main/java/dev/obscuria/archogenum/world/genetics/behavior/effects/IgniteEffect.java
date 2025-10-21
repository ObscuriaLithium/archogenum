package dev.obscuria.archogenum.world.genetics.behavior.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public record IgniteEffect(
        int seconds
) implements IEffect {

    public static final Codec<IgniteEffect> CODEC;

    @Override
    public Codec<IgniteEffect> codec() {
        return CODEC;
    }

    @Override
    public void apply(ServerLevel level, LivingEntity entity, GeneInstance gene) {
        entity.setSecondsOnFire(seconds);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("seconds").forGetter(IgniteEffect::seconds)
        ).apply(codec, IgniteEffect::new));
    }
}
