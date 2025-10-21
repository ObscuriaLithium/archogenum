package dev.obscuria.archogenum.world.genetics.behavior.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public record WorldStateCondition(
        Optional<Boolean> isDay
) implements ICondition {

    public static final Codec<WorldStateCondition> CODEC;

    @Override
    public Codec<WorldStateCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerLevel level, LootContext context) {
        if (isDay.isPresent() && isDay.get() != level.isDay()) return false;
        return true;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.BOOL.optionalFieldOf("is_day").forGetter(WorldStateCondition::isDay)
        ).apply(codec, WorldStateCondition::new));
    }
}
