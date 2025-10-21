package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.client.screen.containers.GridContainer;
import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.AbstractGeneDisplay;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.Objects;

public class GenesPanel extends PageContainer {

    public GenesPanel(int x, int y, int width, int height) {
        super(x, y, width, height);

        final var header = new HeaderNode(Component.literal("Genes"));
        header.setCentered(true);
        this.setHeader(header);

        final var scroll = new ScrollContainer(CommonComponents.EMPTY);
        final var list = new ListContainer(0, 1, 0);
        {
            final var container = new GridContainer(1);
            final var lookup = ArchogenumProxy.registryAccess().lookupOrThrow(ArchoRegistries.Keys.GENE);
            lookup.listElements()
                    .sorted(Comparator.comparing(gene -> gene.value().category()))
                    .sorted(Comparator.comparing(gene -> gene.value().group()))
                    .forEach(gene -> container.addChild(new GeneDisplay(gene)));
            list.addChild(container);
        }
        scroll.addChild(list);
        this.setBody(scroll);
    }

    public static class GeneDisplay extends AbstractGeneDisplay {

        public GeneDisplay(Holder<Gene> gene) {
            super(gene);
        }

        @Override
        protected boolean isSelected() {
            return Objects.equals(GeneDetailsPanel.target, this.gene);
        }
    }
}
