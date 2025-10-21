package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.function.Consumer;

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

    @Override
    public void appendComponents(Holder<ITrait> self, Consumer<TraitComponent> consumer) {
        for (var template : effects) {
            final var name = makeEffectName(template);
            final var category = pickCategory(template.effect().value());
            consumer.accept(new TraitComponent(category, name));
        }
    }

    public Component makeEffectName(EffectTemplate template) {
        final var name = Component.translatable(template.effect.value().getDescriptionId());
        if (template.amplifier <= 0) return name;
        final var potency = Component.translatable("potion.potency." + template.amplifier);
        return Component.translatable("potion.withAmplifier", name, potency);
    }

    private GeneCategory pickCategory(MobEffect effect) {
        return effect.getCategory() == MobEffectCategory.HARMFUL ? GeneCategory.HARMFUL : GeneCategory.BENEFICIAL;
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
