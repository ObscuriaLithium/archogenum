package dev.obscuria.archogenum.client.screen.nodes;

import dev.obscuria.archogenum.Archogenum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SubHeaderNode extends TextNode {

    public SubHeaderNode(MutableComponent content) {
        this.setContent(content);
        this.setCentered(true);
    }

    @Override
    public void setContent(Component value) {
        super.setContent(value.copy().withStyle(style -> style.withFont(Archogenum.FONT)));
    }
}
