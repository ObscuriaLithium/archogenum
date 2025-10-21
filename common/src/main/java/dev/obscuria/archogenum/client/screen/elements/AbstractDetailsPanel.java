package dev.obscuria.archogenum.client.screen.elements;

import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class AbstractDetailsPanel extends PageContainer {

    protected final HeaderNode header;
    protected final ListContainer content;

    public AbstractDetailsPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.header = new HeaderNode(Component.literal("Details"));
        this.setHeader(header);
        final var scroll = new ScrollContainer(getPlaceholder());
        this.content = new ListContainer();
        scroll.addChild(content);
        this.setBody(scroll);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        maybeUpdate();
        super.render(graphics, transform, mouseX, mouseY);
    }

    protected abstract boolean isUpdated();

    protected abstract Component getPlaceholder();

    protected abstract Component getDisplayName();

    protected abstract void update();

    protected void maybeUpdate() {
        if (!this.isUpdated()) return;
        this.content.clearChildren();
        this.isChanged = true;
        this.header.setContent(this.getDisplayName());
        this.update();
    }
}
