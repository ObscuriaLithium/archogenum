package dev.obscuria.archogenum.client.screen.tool;

import net.minecraft.client.gui.components.AbstractWidget;

public record Region(int x, int y, int width, int height) {

    public static Region of(AbstractWidget widget) {
        return new Region(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
    }

    public static Region offset(AbstractWidget widget, int x, int y) {
        return new Region(widget.getX() + x, widget.getY() + y, widget.getWidth(), widget.getHeight());
    }

    public int left() {
        return x;
    }

    public int right() {
        return x + width;
    }

    public int top() {
        return y;
    }

    public int bottom() {
        return y + height;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= left() && mouseY >= top() && mouseX < right() && mouseY < bottom();
    }

    public boolean collidesWith(Region other) {
        return left() < other.right() && right() > other.left() && top() < other.bottom() && bottom() > other.top();
    }
}
