package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.XenofruitDisplay;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.pages.XenofruitsPage;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import net.minecraft.network.chat.Component;

public class XenofruitsPanel extends PageContainer {

    public XenofruitsPanel(int x, int y, int width, int height) {
        super(x, y, width, height);

        final var header = new HeaderNode(Component.literal("Xenofruits"));
        header.setCentered(true);
        this.setHeader(header);

        final var scroll = new ScrollContainer(Component.translatable("screen.archogenum.xenofruits.placeholder"));
        final var list = new ListContainer(0, 1, 0);
        final var xenotypes = ClientGeneticProfile.INSTANCE.xenotypes().xenotypes();
        xenotypes.forEach(xenotype -> {
            final var display = new XenofruitDisplay(xenotype);
            display.clickAction = ClickAction.leftClick(it -> XenofruitsPage.selected.emit(xenotype));
            list.addChild(display);
        });
        scroll.addChild(list);
        this.setBody(scroll);
    }
}
