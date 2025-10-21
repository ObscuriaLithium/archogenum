package dev.obscuria.archogenum.client.screen.elements;

import dev.obscuria.archogenum.client.screen.containers.GridContainer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemListDisplay extends GridContainer {

    public ItemListDisplay(List<ItemStack> stacks) {
        super(0);
        stacks.forEach(stack -> this.addChild(new ItemDisplay(stack)));
    }
}
