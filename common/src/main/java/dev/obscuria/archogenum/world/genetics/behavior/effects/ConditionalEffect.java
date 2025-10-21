package dev.obscuria.archogenum.world.genetics.behavior.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.behavior.conditions.ICondition;
import dev.obscuria.fragmentum.extension.CodecExtension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.List;
import java.util.function.Consumer;

public record ConditionalEffect<T>(
        ICondition requirements,
        T effect
) {

    public boolean matches(ServerLevel level, LootContext context) {
        return requirements.test(level, context);
    }

    public static <T> void select(List<ConditionalEffect<T>> effects, ServerLevel level, LootContext context, Consumer<T> consumer) {
        for (var effect : effects) {
            if (!effect.matches(level, context)) continue;
            consumer.accept(effect.effect);
        }
    }

    public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> effectCodec, LootContextParamSet params) {
        return RecordCodecBuilder.create(codec -> codec.group(
                conditionCodec(params).fieldOf("requirements").forGetter(ConditionalEffect::requirements),
                effectCodec.fieldOf("effect").forGetter(ConditionalEffect::effect)
        ).apply(codec, ConditionalEffect::new));
    }

    private static Codec<ICondition> conditionCodec(LootContextParamSet params) {
        return CodecExtension.validate(ICondition.CODEC, value -> {
            final var context = new ValidationContext(params, EmptyLootData.INSTANCE);
            value.validate(context);
            if (context.getProblems().isEmpty()) {
                return DataResult.success(value);
            } else {
                return DataResult.error(() -> "Validation error in enchantment effect condition: " + context.getProblems());
            }
        });
    }
}
