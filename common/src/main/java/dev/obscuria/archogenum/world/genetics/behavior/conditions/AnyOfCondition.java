package dev.obscuria.archogenum.world.genetics.behavior.conditions;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record AnyOfCondition(
        @Unmodifiable List<ICondition> terms
) implements CompositeCondition {

    public static final Codec<AnyOfCondition> CODEC = CompositeCondition.createCodec(AnyOfCondition::new);
    public static final Codec<AnyOfCondition> INLINE_CODEC = CompositeCondition.createInlineCodec(AnyOfCondition::new);

    @Override
    public Codec<AnyOfCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerLevel level, LootContext lootContext) {
        return terms.stream().anyMatch(it -> it.test(level, lootContext));
    }
}
