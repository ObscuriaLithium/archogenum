package dev.obscuria.archogenum.client.screen.tool;

import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;

import java.util.function.Consumer;

@FunctionalInterface
public interface ClickAction<T extends HierarchicalNode> {

    static <T extends HierarchicalNode> LeftClick<T> leftClick(Consumer<T> action) {
        return it -> {
            action.accept(it);
            return true;
        };
    }

    static <T extends HierarchicalNode> RightClick<T> rightClick(Consumer<T> action) {
        return it -> {
            action.accept(it);
            return true;
        };
    }

    static <T extends HierarchicalNode> LeftClick<T> flatLeftClick(ClickAction<T> action) {
        return action::invoke;
    }

    static <T extends HierarchicalNode> RightClick<T> flatRightClick(ClickAction<T> action) {
        return action::invoke;
    }

    boolean invoke(T node);

    @SuppressWarnings("unchecked")
    default boolean invokeCast(HierarchicalNode node) {
        try {
            return invoke((T) node);
        } catch (ClassCastException ignored) {
            return false;
        }
    }

    default boolean canConsume(int button) {
        return true;
    }

    default boolean mouseClicked(HierarchicalNode node, GlobalTransform transform, double mouseX, double mouseY, int button) {
        if (!transform.isMouseOver(mouseX, mouseY)) return false;
        return invokeCast(node);
    }

    interface LeftClick<T extends HierarchicalNode> extends ClickAction<T> {

        @Override
        default boolean canConsume(int button) {
            return button == 0;
        }
    }

    interface RightClick<T extends HierarchicalNode> extends ClickAction<T> {

        @Override
        default boolean canConsume(int button) {
            return button == 1;
        }
    }
}
