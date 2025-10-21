package dev.obscuria.archogenum.client.screen.nodes;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.client.screen.ArchoPalette;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static java.lang.Math.ceil;

public class TextNode extends HierarchicalNode {

    @Getter private Component content = Component.empty();
    @Getter private float scale = 1f;
    @Getter private boolean centered = false;

    private final Font font = Minecraft.getInstance().font;
    private MultiLineLabel label = MultiLineLabel.EMPTY;

    public static TextNode header(MutableComponent content) {
        TextNode node = new TextNode();
        node.setContent(content.withStyle(style -> style.withFont(Archogenum.FONT)));
        node.setCentered(true);
        node.setScale(1.2f);
        return node;
    }

    public static TextNode description(Component content, ChatFormatting color) {
        TextNode node = new TextNode();
        node.setContent(content.copy().withStyle(style -> style.withColor(color)));
        node.setScale(0.75f);
        return node;
    }

    public static TextNode description(Component content) {
        TextNode node = new TextNode();
        node.setContent(content.copy().withStyle(style -> style.withColor(ArchoPalette.LIGHT.decimal())));
        node.setScale(0.75f);
        return node;
    }


    public static TextNode panelFooter(MutableComponent content) {
        TextNode node = new TextNode();
        node.setContent(content.withStyle(style -> style.withColor(ArchoPalette.LIGHT.decimal())));
        node.setCentered(true);
        node.setScale(0.66f);
        return node;
    }

    public static TextNode title(MutableComponent content) {
        TextNode node = new TextNode();
        node.setContent(content.withStyle(style -> style.withFont(Archogenum.FONT)));
        node.setCentered(true);
        return node;
    }

    public TextNode() {
        this(0, 0, 0, 0);
    }

    public TextNode(Component content) {
        this(0, 0, 0, 0);
        this.setContent(content);
    }

    public TextNode(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.setUpdateFlags(UPDATE_BY_WIDTH);
    }

    public void setContent(Component value) {
        this.content = value;
        this.isChanged = true;
    }

    public void setScale(float value) {
        this.scale = value;
        this.isChanged = true;
    }

    public void setCentered(boolean value) {
        this.centered = value;
        this.isChanged = true;
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        if (!this.rect.visible()) return;

        graphics.pose().pushPose();

        if (this.centered) {
            graphics.pose().translate(rect.centerX(), rect.y(), 0);
            graphics.pose().scale(scale, scale, scale);
            label.renderCentered(graphics, 0, 0, 10, 0xFFFFFFFF);
        } else {
            graphics.pose().translate(rect.x(), rect.y(), 0);
            graphics.pose().scale(scale, scale, scale);
            label.renderLeftAligned(graphics, 0, 0, 10, 0xFFFFFFFF);
        }

        graphics.pose().popPose();
    }

    @Override
    public void reorganize() {
        this.label = MultiLineLabel.create(font, content, (int) (rect.width() / scale));
        this.rect.setHeight((int) ceil(((10 * label.getLineCount() - 1) * scale)));
    }

    @Override
    protected boolean hasContents() {
        return true;
    }
}