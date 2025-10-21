package dev.obscuria.archogenum.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.Texture;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.fragmentum.util.color.ARGB;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;

public interface GuiUtil {

    static void setShaderColor(ARGB argb) {
        RenderSystem.setShaderColor(argb.red(), argb.green(), argb.blue(), argb.alpha());
    }

    static void resetShaderColor() {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    static void drawShifted(GuiGraphics graphics, Texture texture, HierarchicalNode node) {
        drawShifted(graphics, texture, node, 0, 0, 0, 0);
    }

    static void drawShifted(GuiGraphics graphics, Texture texture, HierarchicalNode node, int x, int y, int width, int height) {
        draw(graphics, texture, node.rect.x() + x, node.rect.y() + y, node.rect.width() + width, node.rect.height() + height);
    }

    static void draw(GuiGraphics graphics, Texture texture, int x, int y, int width, int height) {
        texture.render(graphics, x, y, width, height);
    }

    static void drawGene(GuiGraphics graphics, Holder<Gene> gene, int x, int y, boolean hovered) {
        GuiUtil.drawGene(graphics, gene, x, y, hovered, false);
    }

    static void drawGene(GuiGraphics graphics, Holder<Gene> gene, int x, int y, boolean hovered, boolean forceKnown) {
        final var frame = hovered
                ? gene.value().isArchogene() ? ArchoTextures.ARCHOGENE_FRAME_HOVERED : ArchoTextures.GENE_FRAME_HOVERED
                : gene.value().isArchogene() ? ArchoTextures.ARCHOGENE_FRAME : ArchoTextures.GENE_FRAME;
        GuiUtil.draw(graphics, frame, x - 12, y - 12, 24, 24);
        if (forceKnown || ClientGeneticProfile.INSTANCE.discoveries().isKnown(gene)) {
            final var texture = Texture.fixed(gene.value().texture(), 24, 24);
            final var overlay = Texture.fixed(gene.value().category().overlay, 24, 24);
            GuiUtil.draw(graphics, texture, x - 12, y - 12, 24, 24);
            GuiUtil.draw(graphics, overlay, x - 12, y - 12, 24, 24);
        } else {
            GuiUtil.draw(graphics, ArchoTextures.LOCK, x - 12, y - 12, 24, 24);
        }
    }
}
