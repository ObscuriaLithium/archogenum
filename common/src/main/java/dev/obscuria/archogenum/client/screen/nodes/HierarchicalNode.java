package dev.obscuria.archogenum.client.screen.nodes;

import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.client.screen.tool.MouseFilter;
import dev.obscuria.archogenum.client.screen.tool.Rectangle;
import dev.obscuria.archogenum.registry.ArchoSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class HierarchicalNode extends AbstractWidget {

    public static final byte UPDATE_BY_WIDTH = 1;
    public static final byte UPDATE_BY_HEIGHT = 1 << 1;

    public final Rectangle rect = new Rectangle(this);
    public MouseFilter mouseFilter = MouseFilter.STOP;
    public @Nullable ClickAction<?> clickAction;
    public @Nullable SoundEvent clickSound = ArchoSounds.UI_CLICK_1.get();
    public boolean isChanged = true;

    private final List<HierarchicalNode> children = new ArrayList<>();
    private byte internalUpdateFlags;

    public HierarchicalNode(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public abstract void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY);

    public abstract void reorganize();

    public Stream<HierarchicalNode> listChildren() {
        return Arrays.stream(children.toArray(HierarchicalNode[]::new));
    }

    public boolean hasChildren() {
        return listChildren().findAny().isPresent();
    }

    public <T extends HierarchicalNode> T addChild(T node) {
        children.add(node);
        isChanged = true;
        return node;
    }

    public void removeChild(HierarchicalNode node) {
        children.remove(node);
        isChanged = true;
    }

    public void clearChildren() {
        children.clear();
        isChanged = true;
    }

    public void tick() {
        listChildren().forEach(HierarchicalNode::tick);
    }

    public boolean mouseClicked(GlobalTransform transform, double mouseX, double mouseY, int button) {
        if (!rect.visible()) return false;
        if (active
                && mouseFilter != MouseFilter.IGNORE
                && clickAction != null
                && clickAction.mouseClicked(this, transform, mouseX, mouseY, button)) {
            playClickSound();
            if (mouseFilter == MouseFilter.STOP) return true;
        }
        return listChildren().anyMatch(it -> it.mouseClicked(transform.forChild(it), mouseX, mouseY, button));
    }

    public boolean mouseScrolled(GlobalTransform transform, double mouseX, double mouseY, double scroll) {
        if (!rect.visible()) return false;
        return listChildren().anyMatch(it -> it.mouseScrolled(transform.forChild(it), mouseX, mouseY, scroll));
    }

    public void playSound(SoundEvent sound) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F));
    }

    public void setHeight(int value) {
        this.height = value;
    }

    public void setWidth(int value) {
        this.width = value;
    }

    public void setUpdateFlags(byte flags) {
        this.internalUpdateFlags = flags;
    }

    public boolean hasUpdateFlag(byte flag) {
        return (internalUpdateFlags & flag) != 0;
    }

    protected boolean hasContents() {
        return false;
    }

    protected boolean hasVisibleContents() {
        if (rect.visible() && hasContents()) return true;
        return listChildren().anyMatch(HierarchicalNode::hasVisibleContents);
    }

    protected boolean isAnyChanged() {
        return isChanged || listChildren().anyMatch(HierarchicalNode::isAnyChanged);
    }

    protected void consumeChanges() {
        listChildren().forEach(HierarchicalNode::consumeChanges);
        isChanged = false;
        reorganize();
    }

    protected void playClickSound() {
        if (clickSound == null) return;
        playSound(clickSound);
    }

    protected void renderChildren(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {
        if (!rect.visible()) return;
        listChildren().forEach(child -> child.render(graphics, transform.forChild(child), mouseX, mouseY));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {}

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
}
