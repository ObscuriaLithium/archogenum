package dev.obscuria.archogenum.client.screen.elements.details;

import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.archogenum.world.genetics.trait.ITrait;
import dev.obscuria.archogenum.world.genetics.trait.TraitComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TraitsDetails extends AbstractDetails {

    public TraitsDetails(Holder<Gene> gene) {
        this(gene.value().listTraits().toList());
    }

    public TraitsDetails(IBundleLike bundle) {
        this(bundle.genes().stream().flatMap(it -> it.gene().value().listTraits()).toList());
    }

    private TraitsDetails(List<Holder<ITrait>> traits) {
        super(Component.literal("Traits"));
        final var components = new ArrayList<TraitComponent>();
        for (var trait : traits) trait.value().appendComponents(trait, components::add);
        if (components.isEmpty()) {
            this.rect.setVisible(false);
        } else {
            components.sort(Comparator.comparing(TraitComponent::category));
            components.forEach(component -> this.addChild(new Entry(component)));
        }
    }

    private static class Entry extends HierarchicalNode {

        private static final float SCALE = 0.75f;
        private final TraitComponent component;
        private MultiLineLabel label = MultiLineLabel.EMPTY;

        public Entry(TraitComponent component) {
            super(0, 0, 14, 14, CommonComponents.EMPTY);
            this.component = component;
        }

        @Override
        public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
            graphics.blit(component.category().icon, rect.x(), rect.y(), 10, 10, 10, 10, 10, 10);
            graphics.pose().pushPose();
            graphics.pose().translate(rect.x() + 14, rect.y() + 2, 0);
            graphics.pose().scale(SCALE, SCALE, SCALE);
            label.renderLeftAligned(graphics, 0, 0, 10, 0xFFFFFFFF);
            graphics.pose().popPose();
        }

        @Override
        public void reorganize() {
            final var font = Minecraft.getInstance().font;
            this.label = MultiLineLabel.create(font, component.paleFormatted(), (int) ((rect.width() - 12f) / SCALE));
            this.rect.setHeight(Math.max(12, 2 + (int) Math.ceil((10 * label.getLineCount() - 1) * SCALE)));
        }
    }
}
