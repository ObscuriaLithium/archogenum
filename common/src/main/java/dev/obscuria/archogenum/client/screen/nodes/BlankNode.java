package dev.obscuria.archogenum.client.screen.nodes;

import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;

public class BlankNode extends HierarchicalNode {

    public BlankNode() {
        super(0, 0, 0, 0, CommonComponents.EMPTY);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {}

    @Override
    public void reorganize() {}
}
