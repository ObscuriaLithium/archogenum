package dev.obscuria.archogenum.client.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.pages.AssemblyPage;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.registry.ArchoItems;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractVesselDisplay extends HierarchicalNode {

    private final ItemStack stack;
    public final IBundleLike bundle;

    public AbstractVesselDisplay(IBundleLike bundle) {
        super(0, 0, 0, 26, CommonComponents.EMPTY);
        this.stack = ArchoItems.ECHO_VESSEL.instantiate();
        this.bundle = bundle;
        EchoVesselItem.setStoredGenes(stack, new StoredGenes(bundle.genes()));
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {

        if (this.isSelected()) {
            GuiUtil.drawShifted(graphics, ArchoTextures.OUTLINE_WHITE, this);
        } else if (isHovered(transform, mouseX, mouseY)) {
            GuiUtil.drawShifted(graphics, ArchoTextures.OUTLINE_PURPLE, this);
        }

        final var step = bundle.genes().size() <= 3 ? 22 : 60 / bundle.genes().size();
        var offset = 32;
        for (var gene : bundle.genes()) {
            GuiUtil.drawGene(graphics, gene.gene(), rect.x() + offset, rect.y() + 13, false, true);
            offset += step;
        }

        graphics.renderItem(stack, rect.x() + 3, rect.y() + 5);
    }

    @Override
    public void reorganize() {}

    protected abstract boolean isSelected();

    protected boolean isHovered(GlobalTransform transform, int mouseX, int mouseY) {
        return transform.isMouseOver(mouseX, mouseY);
    }

    @Override
    protected boolean hasContents() {
        return true;
    }

    public static class Simple extends AbstractVesselDisplay {

        public Simple(IBundleLike bundle) {
            super(bundle);
        }

        @Override
        protected boolean isSelected() {
            return false;
        }
    }

    public static class Reservable extends AbstractVesselDisplay {

        private boolean isReserved = false;

        public Reservable(IBundleLike bundle) {
            super(bundle);
            AssemblyPage.bundleAdded.connect(this, false, ArchoScreen.breaker, this::onBundleAdded);
            AssemblyPage.bundleRemoved.connect(this, false, ArchoScreen.breaker, this::onBundleRemoved);
        }

        @Override
        public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
            if (isReserved) {
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1f, 1f, 1f, 0.25f);
                super.render(graphics, transform, mouseX, mouseY);
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                RenderSystem.disableBlend();
            } else {
                super.render(graphics, transform, mouseX, mouseY);
            }
        }

        @Override
        protected boolean isSelected() {
            return false;
        }

        @Override
        public boolean mouseClicked(GlobalTransform transform, double mouseX, double mouseY, int button) {
            if (isReserved) return false;
            return super.mouseClicked(transform, mouseX, mouseY, button);
        }

        @Override
        protected boolean isHovered(GlobalTransform transform, int mouseX, int mouseY) {
            return !isReserved && super.isHovered(transform, mouseX, mouseY);
        }

        private void onBundleAdded(IBundleLike bundle) {
            if (this.bundle.equals(bundle)) this.isReserved = true;
        }

        private void onBundleRemoved(IBundleLike bundle) {
            if (this.bundle.equals(bundle)) this.isReserved = false;
        }
    }
}
