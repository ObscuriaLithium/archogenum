package dev.obscuria.archogenum.client.screen.elements;

import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.registry.ArchoItems;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.item.ItemStack;

public class XenofruitDisplay extends HierarchicalNode {

    private final ItemStack stack;
    public final Xenotype xenotype;

    public XenofruitDisplay(Xenotype xenotype) {
        super(0, 0, 0, 26, CommonComponents.EMPTY);
        this.stack = ArchoItems.XENOFRUIT.instantiate();
        this.xenotype = xenotype;
        EchoVesselItem.setStoredGenes(stack, new StoredGenes(xenotype.genes()));
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {

        if (isHovered(transform, mouseX, mouseY)) {
            GuiUtil.drawShifted(graphics, ArchoTextures.OUTLINE_PURPLE, this);
        }

        final var step = xenotype.genes().size() <= 3 ? 22 : 60 / xenotype.genes().size();
        var offset = 32;
        for (var gene : xenotype.genes()) {
            GuiUtil.drawGene(graphics, gene.gene(), rect.x() + offset, rect.y() + 13, false, true);
            offset += step;
        }

        graphics.renderItem(stack, rect.x() + 3, rect.y() + 5);
    }

    @Override
    public void reorganize() {}

    protected boolean isHovered(GlobalTransform transform, int mouseX, int mouseY) {
        return transform.isMouseOver(mouseX, mouseY);
    }

    @Override
    protected boolean hasContents() {
        return true;
    }
}
