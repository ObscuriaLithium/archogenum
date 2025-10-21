package dev.obscuria.archogenum.client.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.item.ItemStack;

public class ItemDisplay extends HierarchicalNode {

    private final ItemStack stack;

    public ItemDisplay(ItemStack stack) {
        super(0, 0, 18, 18, CommonComponents.EMPTY);
        this.stack = stack;
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 0.25f);
        GuiUtil.drawShifted(graphics, ArchoTextures.SOLID_LIGHT, this, 1, 0, -2, -1);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();

        if (transform.isMouseOver(mouseX, mouseY)) {
            GuiUtil.drawShifted(graphics, ArchoTextures.OUTLINE_PURPLE, this);
            ArchoScreen.tooltipStack = stack;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(rect.centerX(), rect.centerY(), 0);
        graphics.renderItem(stack, -8, -8);
        graphics.renderItemDecorations(Minecraft.getInstance().font, stack, -8, -8);
        graphics.pose().popPose();
    }

    @Override
    public void reorganize() {}
}
