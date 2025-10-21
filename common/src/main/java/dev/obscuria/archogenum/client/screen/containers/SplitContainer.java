package dev.obscuria.archogenum.client.screen.containers;

import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;

public class SplitContainer extends HierarchicalNode {

    private final float ratio;
    private final int separation;
    private final boolean drawSeparator;

    public SplitContainer(float ratio, int separation, boolean drawSeparator) {
        super(0, 0, 0, 0, CommonComponents.EMPTY);
        this.ratio = ratio;
        this.separation = separation;
        this.drawSeparator = drawSeparator;
        this.setUpdateFlags(UPDATE_BY_WIDTH);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        this.renderChildren(graphics, transform, mouseX, mouseY);
    }

    @Override
    public void reorganize() {

        final var children = listChildren().toList();
        if (children.size() != 2) return;
        final var first = children.get(0);
        final var second = children.get(1);
        final var split = (int) (rect.width() * ratio);

        first.rect.setX(rect.x());
        first.rect.setY(rect.y());
        first.rect.setWidth(split - separation);
        first.rect.setHeight(rect.height());

        second.rect.setX(rect.x() + split + 1 + separation);
        second.rect.setY(rect.y());
        second.rect.setWidth(rect.width() - split - 1 - separation);
        second.rect.setHeight(rect.height());
    }
}
