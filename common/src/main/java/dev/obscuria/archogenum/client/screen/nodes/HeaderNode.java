package dev.obscuria.archogenum.client.screen.nodes;

import dev.obscuria.archogenum.Archogenum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class HeaderNode extends TextNode {

    public HeaderNode(MutableComponent content) {
        this.setContent(content);
        this.setCentered(true);
        this.setScale(1.2f);
    }

    @Override
    public void setContent(Component value) {
        super.setContent(value.copy().withStyle(style -> style.withFont(Archogenum.FONT)));
    }
}
