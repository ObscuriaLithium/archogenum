package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.DummyDisplay;
import dev.obscuria.archogenum.client.screen.elements.GraphNode;
import dev.obscuria.archogenum.client.screen.elements.SpacingNode;
import dev.obscuria.archogenum.client.screen.elements.details.PropertyDetails;
import dev.obscuria.archogenum.client.screen.elements.details.SynthesisCostDetails;
import dev.obscuria.archogenum.client.screen.elements.details.TraitsDetails;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.nodes.TextNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.resource.GeneBundle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VesselDetailsPanel extends PageContainer {

    public static @Nullable GeneBundle target;

    private static final Component VESSEL_NAME = Component.translatable("item.archogenum.echo_vessel");
    private static final Component VESSEL_DESC = Component.translatable("item.archogenum.echo_vessel.desc");

    private final HeaderNode header;
    private final ListContainer body;

    private @Nullable GeneBundle lastTarget;

    public VesselDetailsPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.header = new HeaderNode(Component.literal("Details"));
        this.setHeader(header);
        final var scroll = new ScrollContainer(Component.translatable("screen.archogenum.vessels.details_placeholder"));
        this.body = new ListContainer();
        scroll.addChild(body);
        this.setBody(scroll);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        maybeUpdate();
        super.render(graphics, transform, mouseX, mouseY);
    }

    private void maybeUpdate() {
        if (Objects.equals(target, lastTarget)) return;

        this.body.clearChildren();
        this.lastTarget = target;
        this.isChanged = true;
        if (target == null) return;

        DummyDisplay.xenotype = new Xenotype(target.genes());
        this.header.setContent(VESSEL_NAME);
        this.body.addChild(new GraphNode(target));
        this.body.addChild(new SpacingNode(5));
        this.body.addChild(TextNode.description(VESSEL_DESC));
        this.body.addChild(SynthesisCostDetails.ofBundle(target));
        this.body.addChild(new TraitsDetails(target));
        this.body.addChild(new PropertyDetails(target));
    }
}
