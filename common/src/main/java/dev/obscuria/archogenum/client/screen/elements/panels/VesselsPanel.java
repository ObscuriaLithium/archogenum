package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.AbstractVesselDisplay;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.archogenum.world.genetics.resource.GeneBundle;
import dev.obscuria.fragmentum.util.signal.Signal0;
import net.minecraft.network.chat.Component;

import java.util.Objects;
import java.util.function.Function;

public class VesselsPanel extends PageContainer {

    public static final Signal0 saveXenotype = new Signal0();

    public VesselsPanel(int x, int y, int width, int height, Function<GeneBundle, ClickAction<AbstractVesselDisplay>> action) {
        super(x, y, width, height);

        final var header = new HeaderNode(Component.literal("Vessels"));
        header.setCentered(true);
        this.setHeader(header);

        final var scroll = new ScrollContainer(Component.translatable("screen.archogenum.vessels.placeholder"));
        final var list = createBody(action);
        scroll.addChild(list);
        this.setBody(scroll);
    }

    private static ListContainer createBody(Function<GeneBundle, ClickAction<AbstractVesselDisplay>> action) {
        final var list = new ListContainer();
        final var bundles = ClientGeneticProfile.INSTANCE.bundles().bundles();
        bundles.forEach(bundle -> {
            final var display = new VesselDisplay(bundle);
            display.clickAction = action.apply(bundle);
            list.addChild(display);
        });
        return list;
    }

    public static class VesselDisplay extends AbstractVesselDisplay {

        public VesselDisplay(IBundleLike bundle) {
            super(bundle);
        }

        @Override
        protected boolean isSelected() {
            return Objects.equals(VesselDetailsPanel.target, bundle);
        }
    }
}
