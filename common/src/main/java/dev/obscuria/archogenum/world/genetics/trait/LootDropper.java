package dev.obscuria.archogenum.world.genetics.trait;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface LootDropper {

    @Nullable ItemEntity drop(ItemStack stack);
}
