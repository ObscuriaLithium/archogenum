package dev.obscuria.archogenum.client.screen.tool;

import net.minecraft.client.gui.components.AbstractWidget;

public record GlobalTransform(
        AbstractWidget widget,
        Region scissor,
        Region region
) {

    public static GlobalTransform of(AbstractWidget widget, Region scissor) {
        return new GlobalTransform(widget, scissor, Region.of(widget));
    }

    public static GlobalTransform offset(AbstractWidget widget, Region scissor, int x, int y) {
        return new GlobalTransform(widget, scissor, Region.offset(widget, x, y));
    }

    public boolean isWithinScissor() {
        return scissor.collidesWith(region);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return isMouseOver((int) mouseX, (int) mouseY);
    }

    public GlobalTransform forChild(AbstractWidget child) {
        return new GlobalTransform(child, scissor, new Region(
                child.getX() + (region.x() - widget.getX()),
                child.getY() + (region.y() - widget.getY()),
                child.getWidth(), child.getHeight()
        ));
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return scissor.isMouseOver(mouseX, mouseY) && region.isMouseOver(mouseX, mouseY);
    }
}
