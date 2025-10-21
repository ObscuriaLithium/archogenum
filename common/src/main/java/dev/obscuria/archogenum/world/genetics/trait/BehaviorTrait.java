package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import dev.obscuria.archogenum.world.genetics.trait.triggers.ITrigger;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record BehaviorTrait(
        GeneCategory category,
        @Unmodifiable List<ITrigger> triggers
) implements ITrait {

    public static final Codec<BehaviorTrait> CODEC;

    @Override
    public Codec<BehaviorTrait> codec() {
        return CODEC;
    }

    @Override
    public void tick(ServerLevel level, LivingEntity entity, GeneInstance gene) {
        for (var trigger : triggers) {
            trigger.tick(level, entity, gene);
        }
    }

    @Override
    public GeneCategory defaultCategory() {
        return category;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                GeneCategory.CODEC.fieldOf("category").forGetter(BehaviorTrait::category),
                ITrigger.CODEC.listOf().fieldOf("triggers").forGetter(BehaviorTrait::triggers)
        ).apply(codec, BehaviorTrait::new));
    }
}
