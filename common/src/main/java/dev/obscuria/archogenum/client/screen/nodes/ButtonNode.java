package dev.obscuria.archogenum.client.screen.nodes;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.client.screen.tool.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ButtonNode extends HierarchicalNode {

    public ButtonNode(Component name) {
        super(0, 0, 14, 14, name);
    }

    public Component getButtonName() {
        return getMessage();
    }

    public Texture pickTexture(boolean isHovered) {
        return ArchoTextures.buttonGray(isHovered);
    }

    public void renderButton(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        GuiUtil.drawShifted(graphics, pickTexture(active && isHovered), this);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        if (!transform.isWithinScissor()) return;
        this.isHovered = transform.isMouseOver(mouseX, mouseY);
        final var font = Minecraft.getInstance().font;

        if (active) {
            this.renderButton(graphics, transform, mouseX, mouseY);
            graphics.drawCenteredString(font, getButtonName(), (int) rect.centerX(), rect.y() + 3, -0x1);
        } else {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, 0.33f);
            this.renderButton(graphics, transform, mouseX, mouseY);
            graphics.drawCenteredString(font, getButtonName(), (int) rect.centerX(), rect.y() + 3, -0x1);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.disableBlend();
        }
    }

    @Override
    public void reorganize() {}
}
