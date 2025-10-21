package dev.obscuria.archogenum.world.genetics.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.function.Consumer;

public record ExtraDropTrait(
        ResourceLocation lootTable
) implements ITrait {

    public static final Codec<ExtraDropTrait> CODEC;

    @Override
    public Codec<ExtraDropTrait> codec() {
        return CODEC;
    }

    @Override
    public void appendComponents(Holder<ITrait> self, Consumer<TraitComponent> consumer) {
        consumer.accept(TraitComponent.beneficial(Component.literal("Extra Death Loot")));
    }

    @Override
    public void dropLoot(LivingEntity entity, LootParams params, long seed, LootDropper dropper) {
        if (!(entity.level() instanceof ServerLevel level)) return;
        final var lootTable = level.getServer().getLootData().getLootTable(this.lootTable);
        lootTable.getRandomItems(params, seed, dropper::drop);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("loot_table").forGetter(ExtraDropTrait::lootTable)
        ).apply(codec, ExtraDropTrait::new));
    }
}
