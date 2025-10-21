package dev.obscuria.archogenum.world.item;

import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.fragmentum.world.tooltip.TooltipOptions;
import dev.obscuria.fragmentum.world.tooltip.Tooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EchoFruitItem extends XenofruitItem {

    public EchoFruitItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        final var description = Component.translatable("item.archogenum.echo_fruit.desc");
        tooltip.addAll(Tooltips.process(description, null, TooltipOptions.DESCRIPTION));
    }

    @Override
    protected boolean canStoreGenes(ItemStack stack) {
        return false;
    }

    @Override
    protected Xenotype getXenotype(ItemStack stack) {
        return Xenotype.EMPTY;
    }
}
