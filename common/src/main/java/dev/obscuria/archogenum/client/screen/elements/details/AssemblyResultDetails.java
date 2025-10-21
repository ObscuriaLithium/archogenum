package dev.obscuria.archogenum.client.screen.elements.details;

import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.elements.SpacingNode;
import dev.obscuria.archogenum.client.screen.nodes.TextNode;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class AssemblyResultDetails extends ListContainer {

    public AssemblyResultDetails(Xenotype xenotype) {
        super();
        if (xenotype.complexity() > 16) {
            final var error = TextNode.description(
                    Component.literal("Assembly is too complicated"),
                    ChatFormatting.RED);
            error.setCentered(true);
            this.addChild(error);
            this.addChild(new SpacingNode(4));
        }
    }
}
