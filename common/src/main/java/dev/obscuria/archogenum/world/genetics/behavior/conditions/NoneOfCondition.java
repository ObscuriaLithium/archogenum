package dev.obscuria.archogenum.world.genetics.behavior.conditions;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record NoneOfCondition(
        @Unmodifiable List<ICondition> terms
) implements CompositeCondition {

    public static final Codec<NoneOfCondition> CODEC = CompositeCondition.createCodec(NoneOfCondition::new);
    public static final Codec<NoneOfCondition> INLINE_CODEC = CompositeCondition.createInlineCodec(NoneOfCondition::new);

    @Override
    public Codec<NoneOfCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerLevel level, LootContext lootContext) {
        return terms.stream().noneMatch(it -> it.test(level, lootContext));
    }
}
