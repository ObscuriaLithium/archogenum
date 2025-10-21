package dev.obscuria.archogenum.client.screen.containers;

import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class GridContainer extends HierarchicalNode {

    private final int separation;
    private final int margin = 0;
    private final int fitContents = 80;

    public GridContainer(int separation) {
        super(0, 0, 0, 0, Component.empty());
        this.separation = separation;
        this.setUpdateFlags(UPDATE_BY_WIDTH);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        this.renderChildren(graphics, transform, mouseX, mouseY);
    }

    @Override
    public void reorganize() {

        var offsetX = margin;
        var offsetY = margin;
        var maxHeight = 0;
        for (var child : listChildren().toList()) {
            final var width = offsetX + separation + child.rect.width();
            if (width > fitContents && width > rect.width()) {
                offsetX = margin;
                offsetY += maxHeight + separation;
                maxHeight = 0;
            } else if (rect.width() < width) {
                rect.setWidth(width + margin - 1);
            }
            child.rect.setX(rect.x() + offsetX);
            child.rect.setY(rect.y() + offsetY);
            offsetX += separation + child.rect.width();
            maxHeight = Math.max(maxHeight, child.rect.height());
        }
        rect.setHeight(hasChildren() ? offsetY + maxHeight + margin : 0);
    }
}
