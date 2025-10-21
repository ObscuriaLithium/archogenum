package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootParams;

public record ExtraDropsTrait(
        ResourceLocation lootTable
) implements ITrait {

    public static final Codec<ExtraDropsTrait> CODEC;

    @Override
    public Codec<ExtraDropsTrait> codec() {
        return CODEC;
    }

    @Override
    public void dropLoot(LivingEntity entity, LootParams params, long seed, LootDropper dropper) {
        if (!(entity.level() instanceof ServerLevel level)) return;
        final var lootTable = level.getServer().getLootData().getLootTable(this.lootTable);
        lootTable.getRandomItems(params, seed, dropper::drop);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("loot_table").forGetter(ExtraDropsTrait::lootTable)
        ).apply(codec, ExtraDropsTrait::new));
    }
}
