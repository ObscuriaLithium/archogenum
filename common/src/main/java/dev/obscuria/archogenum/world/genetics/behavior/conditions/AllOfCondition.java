package dev.obscuria.archogenum.world.genetics.behavior.conditions;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record AllOfCondition(
        @Unmodifiable List<ICondition> terms
) implements CompositeCondition {

    public static final Codec<AllOfCondition> CODEC = CompositeCondition.createCodec(AllOfCondition::new);
    public static final Codec<AllOfCondition> INLINE_CODEC = CompositeCondition.createInlineCodec(AllOfCondition::new);

    @Override
    public Codec<AllOfCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerLevel level, LootContext lootContext) {
        return terms.stream().allMatch(it -> it.test(level, lootContext));
    }
}
