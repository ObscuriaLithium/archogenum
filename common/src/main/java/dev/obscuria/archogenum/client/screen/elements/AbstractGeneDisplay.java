package dev.obscuria.archogenum.client.screen.elements;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.elements.panels.GeneDetailsPanel;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.registry.ArchoSounds;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractGeneDisplay extends HierarchicalNode {

    protected final Holder<Gene> gene;

    public AbstractGeneDisplay(Holder<Gene> gene) {
        super(0, 0, 22, 24, Component.empty());
        this.gene = gene;
        this.clickSound = ArchoSounds.UI_BELL.get();
        this.clickAction = ClickAction.<AbstractGeneDisplay>flatLeftClick(display -> {
            GeneDetailsPanel.target = display.gene;
            return true;
        });
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        final var hovered = transform.isMouseOver(mouseX, mouseY);
        GuiUtil.drawGene(graphics, gene, getX() + 11, getY() + 12, isSelected() || hovered);
        if (hovered) ArchoScreen.tooltipProvider = this::makeTooltip;
    }

    @Override
    public void reorganize() {}

    protected abstract boolean isSelected();

    @Override
    protected boolean hasContents() {
        return true;
    }

    private List<FormattedCharSequence> makeTooltip() {
        final var tooltip = new ArrayList<FormattedCharSequence>();
        if (ClientGeneticProfile.INSTANCE.discoveries().isKnown(gene)) {
            tooltip.add(gene.value().getDisplayName(gene).getVisualOrderText());
            appendTraits(component -> tooltip.add(component.getVisualOrderText()));
        } else {
            tooltip.add(Component.literal("Unknown").getVisualOrderText());
        }
        return tooltip;
    }

    private void appendTraits(Consumer<Component> consumer) {
        final @Nullable var traits = gene.value().traits().orElse(null);
        if (traits == null) return;
        for (var holder : traits) {
            holder.getOrThrow().value().appendComponents(holder.getOrThrow(), component -> {
                consumer.accept(Component.literal(" ").append(component.formatted()));
            });
        }
    }
}
