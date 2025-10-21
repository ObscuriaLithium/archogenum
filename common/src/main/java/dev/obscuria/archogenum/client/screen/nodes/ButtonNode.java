package dev.obscuria.archogenum.client.screen.nodes;

import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
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

    public void renderButton(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        GuiUtil.drawShifted(graphics, ArchoTextures.buttonGray(isHovered), this);
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        if (!transform.isWithinScissor()) return;
        this.isHovered = transform.isMouseOver(mouseX, mouseY);
        final var font = Minecraft.getInstance().font;
        this.renderButton(graphics, transform, mouseX, mouseY);
        graphics.drawCenteredString(font, getButtonName(), (int) rect.centerX(), rect.y() + 3, -0x1);
    }

    @Override
    public void reorganize() {}
}
