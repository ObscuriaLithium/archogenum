package dev.obscuria.archogenum.client.screen.containers;

import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ListContainer extends HierarchicalNode {

    private final int marginTop;
    private final int separation;
    private final int marginBottom;

    public ListContainer() {
        this(0, 0, 0);
    }

    public ListContainer(int marginTop, int separation, int marginBottom) {
        super(0, 0, 0, 0, Component.empty());
        this.marginTop = marginTop;
        this.separation = separation;
        this.marginBottom = marginBottom;
        this.setUpdateFlags(UPDATE_BY_WIDTH);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        this.renderChildren(graphics, transform, mouseX, mouseY);
    }

    @Override
    public void reorganize() {
        var offset = marginTop;
        for (var child : listChildren().toList()) {
            if (!child.rect.visible()) continue;
            child.rect.setX(rect.x());
            child.rect.setY(rect.y() + offset);
            child.rect.setWidth(rect.width());
            offset += child.rect.height() + separation;
        }
        rect.setHeight(hasChildren() ? offset + marginBottom - separation : 0);
    }
}
