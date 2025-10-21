package dev.obscuria.archogenum.world.genetics.behavior.effects;

import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataResolver;
import org.jetbrains.annotations.Nullable;

public final class EmptyLootData implements LootDataResolver {

    public static final EmptyLootData INSTANCE = new EmptyLootData();

    @Override
    public @Nullable <T> T getElement(LootDataId<T> lootDataId) {
        return null;
    }

    private EmptyLootData() {}
}
