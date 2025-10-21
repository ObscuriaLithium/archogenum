package dev.obscuria.archogenum.client.screen.elements;

import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;

public class SpacingNode extends HierarchicalNode {

    public SpacingNode(int height) {
        super(0, 0, 0, height, CommonComponents.EMPTY);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {}

    @Override
    public void reorganize() {}
}
