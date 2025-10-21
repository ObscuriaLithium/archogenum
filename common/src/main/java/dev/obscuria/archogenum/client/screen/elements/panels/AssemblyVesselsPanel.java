package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.AbstractVesselDisplay;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.pages.AssemblyPage;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import dev.obscuria.fragmentum.util.signal.Signal0;
import net.minecraft.network.chat.Component;

public class AssemblyVesselsPanel extends PageContainer {

    public static final Signal0 saveXenotype = new Signal0();

    public AssemblyVesselsPanel(int x, int y, int width, int height) {
        super(x, y, width, height);

        final var header = new HeaderNode(Component.literal("Vessels"));
        header.setCentered(true);
        this.setHeader(header);

        final var scroll = new ScrollContainer(Component.translatable("screen.archogenum.vessels.placeholder"));
        final var list = createBody();
        scroll.addChild(list);
        this.setBody(scroll);
    }

    private static ListContainer createBody() {
        final var list = new ListContainer();
        final var bundles = ClientGeneticProfile.INSTANCE.bundles().bundles();
        bundles.forEach(bundle -> {
            final var display = new AbstractVesselDisplay.Reservable(bundle);
            display.clickAction = ClickAction.leftClick(it -> AssemblyPage.bundleAdded.emit(bundle));
            list.addChild(display);
        });
        return list;
    }
}
