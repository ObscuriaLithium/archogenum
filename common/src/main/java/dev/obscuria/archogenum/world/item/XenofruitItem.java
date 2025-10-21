package dev.obscuria.archogenum.world.item;

import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import dev.obscuria.fragmentum.world.tooltip.TooltipOptions;
import dev.obscuria.fragmentum.world.tooltip.Tooltips;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class XenofruitItem extends Item {

    public XenofruitItem(Properties properties) {
        super(properties.food(Foods.GOLDEN_APPLE));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        final var description = Component.translatable("item.archogenum.xenofruit.desc");
        tooltip.addAll(Tooltips.process(description, null, TooltipOptions.DESCRIPTION));
        if (!this.canStoreGenes(stack)) return;
        tooltip.add(CommonComponents.EMPTY);

        final var storedGenes = EchoVesselItem.getStoredGenes(stack);
        if (storedGenes.isEmpty()) {
            tooltip.add(Component.literal("Empty"));
        } else {
            storedGenes.genes().forEach(gene -> tooltip.add(gene.getDisplayName()));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        XenotypeHandler.setXenotype(entity, this.getXenotype(stack));
        entity.hurt(entity.damageSources().magic(), 1);
        entity.setHealth(1);
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return !this.canStoreGenes(stack) || !this.getXenotype(stack).hasArchogene()
                ? super.getRarity(stack)
                : Rarity.EPIC;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (!this.canStoreGenes(stack)) return false;
        return this.getXenotype(stack).hasArchogene();
    }

    protected boolean canStoreGenes(ItemStack stack) {
        return true;
    }

    protected Xenotype getXenotype(ItemStack stack) {
        return new Xenotype(EchoVesselItem.getStoredGenes(stack).genes());
    }
}
