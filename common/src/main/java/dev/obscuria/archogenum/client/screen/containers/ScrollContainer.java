package dev.obscuria.archogenum.client.screen.containers;

import dev.obscuria.archogenum.client.screen.ArchoPalette;
import dev.obscuria.archogenum.client.screen.nodes.HierarchicalNode;
import dev.obscuria.archogenum.client.screen.tool.GlobalTransform;
import dev.obscuria.archogenum.registry.ArchoSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class ScrollContainer extends HierarchicalNode {

    private @Nullable MultiLineLabel placeholderLabel;
    private double scrollValue = 0.0;
    private double scrollValueO = 0.0;
    private double scroll = 0.0;
    private int childrenHeight = 0;
    private int visibleHeight = 0;
    private int tickCount = 0;
    private int lastSoundTick = 0;

    public ScrollContainer(Component placeholder) {
        super(0, 0, 0, 0, placeholder);
        this.setUpdateFlags(UPDATE_BY_HEIGHT);
    }

    private boolean isScrollEnabled() {
        return childrenHeight > visibleHeight;
    }

    private double maxScroll() {
        return (double) (childrenHeight - visibleHeight);
    }

    public void resetScroll() {
        this.scrollValue = 0.0;
        this.scrollValueO = 0.0;
        this.scroll = 0.0;
        consumeChanges();
    }

    @Override
    public void render(GuiGraphics graphics, GlobalTransform transform, int mouseX, int mouseY) {

        final var scroll = lerpScroll();
        graphics.enableScissor(
                transform.region().left(),
                transform.region().top(),
                transform.region().right(),
                transform.region().bottom());
        graphics.pose().pushPose();
        graphics.pose().translate(0.0, -scroll, 0.0);
        this.listChildren().forEach(child -> {
            final var offset = GlobalTransform.offset(child, transform.region(), 0, -(int) scroll);
            child.render(graphics, offset, mouseX, mouseY);
        });
        graphics.pose().popPose();
        graphics.disableScissor();

        if (isScrollEnabled()) {
            graphics.fill(rect.right() - 1, rect.y(), rect.right() + 1, rect.bottom(), 0x2F000000);
            double clampedScroll = Mth.clamp(scroll, 0.0, maxScroll());
            float ratio = (float) (clampedScroll / maxScroll());
            int height = (int) (rect.height() * (visibleHeight / (float) childrenHeight));
            double offset = (rect.height() - height) * ratio;
            graphics.pose().pushPose();
            graphics.pose().translate(rect.right() - 1.0, rect.y() + offset, 0.0);
            graphics.fill(0, 0, 2, height, ArchoPalette.ACCENT.withAlpha(100 / 255f).decimal());
            graphics.pose().popPose();
        }

        if (scroll > 5) {
            graphics.fill(rect.left(), rect.top(), rect.right() - 1, rect.top() + 5, 0x6080788A);
        }

        if (childrenHeight - scroll > rect.height() + 5) {
            graphics.fill(rect.left(), rect.bottom() - 5, rect.right() - 1, rect.bottom(), 0x6080788A);
        }

        if (hasVisibleContents()) return;
        if (placeholderLabel != null) {
            graphics.pose().pushPose();
            graphics.pose().translate(rect.centerX(), rect.centerY() - placeholderLabel.getLineCount() * 5, 0f);
            graphics.pose().scale(0.75f, 0.75f, 1f);
            placeholderLabel.renderCentered(graphics, 0, 0);
            graphics.pose().popPose();
        }
    }

    @Override
    public void tick() {
        this.tickCount += 1;
        this.scrollValueO = scrollValue;
        this.scrollValue = Mth.lerp(0.5, scrollValue, scroll);
        if (scroll < 0 || !isScrollEnabled()) {
            this.scroll = Mth.lerp(0.75, scroll, 0.0);
        } else if (scroll > maxScroll()) {
            this.scroll = Mth.lerp(0.75, scroll, maxScroll());
        }
        super.tick();
    }

    @Override
    public boolean mouseClicked(GlobalTransform transform, double mouseX, double mouseY, int button) {
        double scroll = lerpScroll();
        return listChildren().anyMatch(child -> {
            final var offset = GlobalTransform.offset(child, transform.region(), 0, -(int) scroll);
            return child.mouseClicked(offset, mouseX, mouseY, button);
        });
    }

    @Override
    public boolean mouseScrolled(GlobalTransform transform, double mouseX, double mouseY, double scroll) {
        if (transform.isMouseOver(mouseX, mouseY)) {
            if (!isScrollEnabled()) return false;
            this.scroll += scroll * -16.0;
            if (Math.abs(scroll) > 0.1f && lastSoundTick < tickCount) {
                var manager = Minecraft.getInstance().getSoundManager();
                float pitch = (scroll > 0) ? 0.9f : 1.1f;
                manager.play(SimpleSoundInstance.forUI(ArchoSounds.UI_SCROLL.get(), pitch));
                this.lastSoundTick = tickCount;
            }
            return true;
        }
        return super.mouseScrolled(transform, mouseX, mouseY, scroll);
    }

    @Override
    public void reorganize() {

        this.visibleHeight = getHeight();
        this.childrenHeight = 0;

        this.listChildren().forEach(it -> {
            it.rect.setX(rect.x());
            it.rect.setY(rect.y() + 5);
            it.rect.setWidth(rect.width() - 2);
            this.childrenHeight = Math.max(childrenHeight + 10, it.rect.height() + 10);
        });

        final var font = Minecraft.getInstance().font;
        final var placeholder = getMessage().copy().withStyle(Style.EMPTY.withColor(ArchoPalette.LIGHT.decimal()));
        this.placeholderLabel = MultiLineLabel.create(font, placeholder, (int) (rect.width() * 1.25));
    }

    private double lerpScroll() {
        float delta = Minecraft.getInstance().getFrameTime();
        return Mth.lerp(delta, scrollValueO, scrollValue);
    }
}