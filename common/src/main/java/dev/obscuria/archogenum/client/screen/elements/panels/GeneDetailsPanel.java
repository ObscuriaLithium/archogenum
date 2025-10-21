package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.DummyDisplay;
import dev.obscuria.archogenum.client.screen.elements.details.*;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class GeneDetailsPanel extends PageContainer {

    public static @Nullable Holder<Gene> target;

    private static final Component UNKNOWN = Component.translatable("gene.archogenum.unknown");

    private final HeaderNode header;
    private final ListContainer details;

    private @Nullable Holder<Gene> lastTarget;

    public GeneDetailsPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.header = new HeaderNode(Component.literal("Details"));
        this.setHeader(header);
        final var scroll = new ScrollContainer(Component.translatable("screen.archogenum.genes.details_placeholder"));
        this.details = new ListContainer();
        scroll.addChild(details);
        this.setBody(scroll);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        maybeUpdate();
        super.render(graphics, transform, mouseX, mouseY);
    }

    private void maybeUpdate() {

        if (Objects.equals(target, lastTarget)) return;
        this.details.clearChildren();
        this.lastTarget = target;
        this.isChanged = true;
        if (target == null) return;
        DummyDisplay.xenotype = new Xenotype(List.of(new GeneInstance(target, 1)));

        if (ClientGeneticProfile.INSTANCE.discoveries().isKnown(target)) {
            DummyDisplay.isHidden = false;
            this.header.setContent(lastTarget.value().getDisplayName(lastTarget));
            this.details.addChild(GeneDescription.known(target));
            if (target.value().isArchogene()) this.details.addChild(new ArchogeneDetails());
            this.details.addChild(SynthesisCostDetails.ofGene(target));
            this.details.addChild(new TraitsDetails(target));
            this.details.addChild(new PropertyDetails(target));

        } else {
            DummyDisplay.isHidden = true;
            this.header.setContent(UNKNOWN);
            this.details.addChild(GeneDescription.unknown(target));
        }
    }
}
