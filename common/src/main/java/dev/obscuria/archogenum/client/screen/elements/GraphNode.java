package dev.obscuria.archogenum.client.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.archogenum.world.genetics.behavior.TraitComponent;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import dev.obscuria.fragmentum.util.color.RGB;
import dev.obscuria.fragmentum.util.easing.Easing;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.lang.Math.*;

public class GraphNode extends HierarchicalNode {

    private static final ARGB GRID_COLOR = Colors.argbOf(0xff4a3847);
    private final IBundleLike bundle;
    private final List<Entry> entries;
    private final List<Point> points = new ArrayList<>();
    private int tickCount = 0;

    public GraphNode(IBundleLike bundle) {
        super(0, 0, 0, 0, Component.empty());
        this.bundle = bundle;
        this.entries = compileEntries();
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        if (!transform.isWithinScissor()) return;
        GuiUtil.setShaderColor(GRID_COLOR);
        GuiUtil.drawShifted(graphics, ArchoTextures.OUTLINE_WHITE, this);
        GuiUtil.resetShaderColor();

        for (int i = 0; i < entries.size(); i++) {
            final var x = rect.left() + (i + 1) * getAnchorSeparation();
            graphics.vLine((int) x, rect.top(), rect.bottom(), GRID_COLOR.decimal());
        }

        for (int i = 1; i <= 4; i++) {
            graphics.hLine(rect.left(), rect.right() - 1, rect.top() + rect.height() / 5 * i, GRID_COLOR.decimal());
        }

        var matrix = graphics.pose().last().pose();
        float timer = (tickCount + Minecraft.getInstance().getFrameTime()) / 20f;
        float factor = timer < 1f ? Easing.EASE_OUT_ELASTIC.compute(timer) : 1f;
        float b = (float) rect.bottom();

        for (int i = 0; i < points.size(); i++) {
            final @Nullable var p1 = i < points.size() ? points.get(i) : null;
            final @Nullable var p2 = i + 1 < points.size() ? points.get(i + 1) : null;
            if (p1 == null || p2 == null) break;

            final var c1 = p1.color;
            final var c2 = p2.color;

            graphics.drawManaged(() -> {
                var buffer = graphics.bufferSource().getBuffer(RenderType.guiOverlay());
                buffer.vertex(matrix, p1.x, lerp(b, p1.y, factor), 0f).color(c1.red(), c1.green(), c1.blue(), 0.5f).endVertex();
                buffer.vertex(matrix, p1.x, b - 2, 0f).color(c1.red(), c1.green(), c1.blue(), 0f).endVertex();
                buffer.vertex(matrix, p2.x, b - 2, 0f).color(c2.red(), c2.green(), c2.blue(), 0f).endVertex();
                buffer.vertex(matrix, p2.x, lerp(b, p2.y, factor), 0f).color(c2.red(), c2.green(), c2.blue(), 0.5f).endVertex();

                buffer.vertex(matrix, p1.x, lerp(b, p1.y, factor), 0f).color(c1.red(), c1.green(), c1.blue(), 1f).endVertex();
                buffer.vertex(matrix, p1.x, lerp(b, p1.y, factor) + 2, 0f).color(c1.red(), c1.green(), c1.blue(), 1f).endVertex();
                buffer.vertex(matrix, p2.x, lerp(b, p2.y, factor) + 2, 0f).color(c2.red(), c2.green(), c2.blue(), 1f).endVertex();
                buffer.vertex(matrix, p2.x, lerp(b, p2.y, factor), 0f).color(c2.red(), c2.green(), c2.blue(), 1f).endVertex();
            });
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
    }

    @Override
    public void reorganize() {
        rect.setWidth(Math.max(rect.width(), 50));
        rect.setHeight(Math.max(rect.height(), 70));
        points.clear();

        final var anchors = new ArrayList<Point>();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            double x = rect.left() + (i + 1) * getAnchorSeparation();
            float y = getEntryY(entry);
            anchors.add(new Point((float) x, y, entry.color));
        }

        anchors.add(0, new Point(rect.left() + 1, rect.bottom() - 4, Colors.rgbOf(0x342D3B)));
        anchors.add(new Point(rect.right() - 2, rect.bottom() - 4, Colors.rgbOf(0x342D3B)));
        points.addAll(compilePoints(anchors, (int) ceil(64.0 / getTotalAnchors())));
    }

    @Override
    protected boolean hasContents() {
        return true;
    }

    private float getEntryY(Entry entry) {
        return (rect.bottom() - 3) - (rect.height() - 4) * (entry.weight / 10f);
    }

    private int getTotalAnchors() {
        return entries.size() + 2;
    }

    private double getAnchorSeparation() {
        return rect.width() / (getTotalAnchors() - 1.0);
    }

    private float lerp(float start, float end, float delta) {
        return (1 - delta) * start + delta * end;
    }

    private float smoothstep(float start, float end, float delta) {
        return start + (end - start) * (delta * delta * (3 - 2 * delta));
    }

    private int lerpColor(int startColor, int endColor, float t) {
        int sr = (startColor >> 16) & 0xFF;
        int sg = (startColor >> 8) & 0xFF;
        int sb = startColor & 0xFF;
        int er = (endColor >> 16) & 0xFF;
        int eg = (endColor >> 8) & 0xFF;
        int eb = endColor & 0xFF;

        int r = (int) smoothstep(sr, er, t);
        int g = (int) smoothstep(sg, eg, t);
        int b = (int) smoothstep(sb, eb, t);
        return (r << 16) | (g << 8) | b;
    }

    private List<Entry> compileEntries() {
        final var result = new ArrayList<Entry>();
        float beneficial = 0;
        float harmful = 0;
        float cosmetic = 0;
        for (var gene : bundle.genes()) {
            final var components = new ArrayList<TraitComponent>();
            gene.gene().value().listTraits().forEach(it -> it.value().appendComponents(it, components::add));
            for (var component : components) {
                final var category = component.category();
                if (category == GeneCategory.BENEFICIAL) beneficial += 1;
                if (category == GeneCategory.HARMFUL) harmful += 1;
                if (category == GeneCategory.COSMETIC) cosmetic += 1;
            }
        }
        if (beneficial > 0) result.add(new Entry(beneficial, Colors.rgbOf(ChatFormatting.GREEN.getColor())));
        if (harmful > 0) result.add(new Entry(harmful, Colors.rgbOf(ChatFormatting.RED.getColor())));
        if (cosmetic > 0) result.add(new Entry(cosmetic, Colors.rgbOf(ChatFormatting.LIGHT_PURPLE.getColor())));
        return result;
    }

    private List<Point> compilePoints(List<Point> anchors, int resolution) {
        List<Point> result = new ArrayList<>();
        for (int i = 0; i < anchors.size() - 1; i++) {
            Point start = anchors.get(i);
            Point end = anchors.get(i + 1);
            result.add(start);

            for (int j = 1; j < resolution; j++) {
                float t = j / (float) resolution;
                float interpolatedX = lerp(start.x, end.x, t);
                float interpolatedY = smoothstep(start.y, end.y, t);
                int interpolatedColor = lerpColor(start.color.decimal(), end.color.decimal(), t);
                result.add(new Point(interpolatedX, interpolatedY, Colors.rgbOf(interpolatedColor)));
            }
        }
        result.add(anchors.get(anchors.size() - 1));
        return result;
    }

    private record Entry(float weight, RGB color) {}

    private record Point(float x, float y, RGB color) {}
}
