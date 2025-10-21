package dev.obscuria.archogenum.world.genetics.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record EffectTrait(
        @Unmodifiable List<EffectTemplate> effects
) implements ITrait {

    public static final Codec<EffectTrait> CODEC;

    @Override
    public Codec<EffectTrait> codec() {
        return CODEC;
    }

    @Override
    public void tick(ServerLevel level, LivingEntity entity, GeneInstance gene) {
        if (entity.tickCount % 18 != 0) return;
        for (var template : effects) {
            entity.addEffect(template.instantiate());
        }
    }

    public record EffectTemplate(
            Holder<MobEffect> effect,
            int amplifier,
            boolean ambient,
            boolean visible
    ) {

        public static final Codec<EffectTemplate> CODEC;

        public MobEffectInstance instantiate() {
            return new MobEffectInstance(effect.value(), 38, amplifier, ambient, visible);
        }

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(EffectTemplate::effect),
                    Codec.INT.optionalFieldOf("amplifier", 0).forGetter(EffectTemplate::amplifier),
                    Codec.BOOL.optionalFieldOf("ambient", false).forGetter(EffectTemplate::ambient),
                    Codec.BOOL.optionalFieldOf("visible", false).forGetter(EffectTemplate::visible)
            ).apply(codec, EffectTemplate::new));
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                EffectTemplate.CODEC.listOf().fieldOf("effects").forGetter(EffectTrait::effects)
        ).apply(codec, EffectTrait::new));
    }
}
