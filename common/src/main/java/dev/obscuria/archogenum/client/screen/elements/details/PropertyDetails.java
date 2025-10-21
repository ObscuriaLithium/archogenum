package dev.obscuria.archogenum.client.screen.elements.details;

import dev.obscuria.archogenum.client.screen.ArchoPalette;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static net.minecraft.network.chat.Component.literal;

import java.util.concurrent.atomic.AtomicBoolean;

public class PropertyDetails extends AbstractDetails {

    public PropertyDetails(Holder<Gene> gene) {
        super(Component.literal("Properties"));
        final var flag = new AtomicBoolean(true);
        this.addChild(new Entry(flag, literal("Complexity"), literal(String.valueOf(gene.value().complexity()))));
        this.addChild(new Entry(flag, literal("Metabolic Efficiency"), literal(String.valueOf(gene.value().metabolicEfficiency()))));
        this.addChild(new Entry(flag, literal("Max Expression"), literal(String.valueOf(gene.value().maxExpression()))));
    }

    public PropertyDetails(IBundleLike bundle) {
        super(Component.literal("Properties"));
        final var flag = new AtomicBoolean(true);
        final var complexity = bundle.genes().stream().mapToInt(it -> it.gene().value().complexity()).sum();
        final var metabolicEfficiency = bundle.genes().stream().mapToInt(it -> it.gene().value().metabolicEfficiency()).sum();
        this.addChild(new Entry(flag, literal("Complexity"), literal(String.valueOf(complexity))));
        this.addChild(new Entry(flag, literal("Metabolic Efficiency"), literal(String.valueOf(metabolicEfficiency))));
    }

    private static class Entry extends HierarchicalNode {

        private static final ARGB BACKGROUND_COLOR = Colors.argbOf(0xff4a3847);
        private final boolean drawBackground;
        private final Component name;
        private final Component value;

        public Entry(AtomicBoolean flag, Component name, Component value) {
            super(0, 0, 14, 14, CommonComponents.EMPTY);
            this.drawBackground = flag.getAndSet(!flag.get());
            this.name = name;
            this.value = value;
        }

        @Override
        public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {

            if (drawBackground) {
                GuiUtil.setShaderColor(BACKGROUND_COLOR);
                GuiUtil.drawShifted(graphics, ArchoTextures.SOLID_WHITE, this);
                GuiUtil.resetShaderColor();
            }

            final var font = Minecraft.getInstance().font;

            graphics.pose().pushPose();
            graphics.pose().translate(rect.x() + 4f, rect.y() + 4f, 0f);
            graphics.pose().scale(0.75f, 0.75f, 0.75f);
            graphics.drawString(font, name, 0, 0, ArchoPalette.LIGHT.decimal());
            graphics.pose().popPose();

            graphics.pose().pushPose();
            final var x = rect.right() - 4f - font.width(value) * 0.75f;
            graphics.pose().translate(x, rect.y() + 4f, 0f);
            graphics.pose().scale(0.75f, 0.75f, 0.75f);
            graphics.drawString(font, value, 0, 0, ArchoPalette.MODERATE.decimal());
            graphics.pose().popPose();
        }

        @Override
        public void reorganize() {}
    }
}
