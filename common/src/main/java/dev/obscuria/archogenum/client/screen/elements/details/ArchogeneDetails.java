package dev.obscuria.archogenum.client.screen.elements.details;

import dev.obscuria.archogenum.client.screen.nodes.TextNode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ArchogeneDetails extends AbstractDetails {

    public ArchogeneDetails() {
        super(Component.literal("Archogene").withStyle(ChatFormatting.GOLD));
        this.addChild(TextNode.description(Component.translatable("screen.archogenum.genes.archogene_desc")));
    }
}
