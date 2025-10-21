package dev.obscuria.archogenum.world.genetics.behavior.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.behavior.effects.ConditionalEffect;
import dev.obscuria.archogenum.world.genetics.behavior.effects.IEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record TickTrigger(
        @Unmodifiable List<ConditionalEffect<IEffect>> effects,
        int interval
) implements ITrigger {

    public static final Codec<TickTrigger> CODEC;

    @Override
    public Codec<TickTrigger> codec() {
        return CODEC;
    }

    @Override
    public void tick(ServerLevel level, LivingEntity entity, GeneInstance gene) {
        if (entity.tickCount % interval != 0) return;
        final var context = ITrigger.defaultContext(level, entity);
        ConditionalEffect.select(effects, level, context, effect -> effect.apply(level, entity, gene));
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ConditionalEffect.codec(IEffect.CODEC, TriggerParams.DEFAULT).listOf().fieldOf("effects").forGetter(TickTrigger::effects),
                Codec.INT.optionalFieldOf("interval", 1).forGetter(TickTrigger::interval)
        ).apply(codec, TickTrigger::new));
    }
}
