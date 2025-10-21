package dev.obscuria.archogenum.client.screen.tool;

import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;

public record Rectangle(HierarchicalNode node) {

    public int x() {
        return node.getX();
    }

    public int y() {
        return node.getY();
    }

    public int width() {
        return node.getWidth();
    }

    public int height() {
        return node.getHeight();
    }

    public boolean visible() {
        return node.visible;
    }

    public int left() {
        return x();
    }

    public int right() {
        return x() + width();
    }

    public int top() {
        return y();
    }

    public int bottom() {
        return y() + height();
    }

    public float centerX() {
        return x() + width() / 2f;
    }

    public float centerY() {
        return y() + height() / 2f;
    }

    public void setX(int x) {
        final var difference = x - x();
        node.setX(x);
        node.listChildren().forEach(child -> child.rect.setX(child.rect.x() + difference));
    }

    public void setY(int y) {
        final var difference = y - y();
        node.setY(y);
        node.listChildren().forEach(child -> child.rect.setY(child.rect.y() + difference));
    }

    public void setWidth(int width) {
        if (width == width()) return;
        if (node.hasUpdateFlag(HierarchicalNode.UPDATE_BY_WIDTH)) {
            node.isChanged = true;
        }
        node.setWidth(width);
    }

    public void setHeight(int height) {
       if (height == height()) return;
       if (node.hasUpdateFlag(HierarchicalNode.UPDATE_BY_HEIGHT)) {
           node.isChanged = true;
       }
       node.setHeight(height);
    }

    public void setVisible(boolean visible) {
        node.visible = visible;
        node.listChildren().forEach(child -> child.rect.setVisible(visible));
    }
}
