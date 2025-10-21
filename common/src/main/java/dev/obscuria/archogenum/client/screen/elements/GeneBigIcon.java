package dev.obscuria.archogenum.client.screen.elements;

import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;

public class GeneBigIcon extends HierarchicalNode {

    private final Holder<Gene> gene;

    public GeneBigIcon(Holder<Gene> gene) {
        super(0, 0, 0, 56, CommonComponents.EMPTY);
        this.gene = gene;
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        graphics.pose().pushPose();
        graphics.pose().translate(rect.centerX(), rect.centerY(), 200);
        graphics.pose().scale(2f, 2f, 1f);
        GuiUtil.drawGene(graphics, gene, 0, 0, false);
        graphics.pose().popPose();
    }

    @Override
    public void reorganize() {}
}
