package dev.obscuria.archogenum.client.screen.elements.details;

import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.elements.SpacingNode;
import dev.obscuria.archogenum.client.screen.nodes.SubHeaderNode;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDetails extends ListContainer {

    public AbstractDetails(@Nullable MutableComponent title) {
        super();
        if (title == null) return;
        this.addChild(new SpacingNode(12));
        this.addChild(new SubHeaderNode(title));
        this.addChild(new SpacingNode(4));
    }
}
