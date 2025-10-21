package dev.obscuria.archogenum.client.screen.pages;

import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.ScreenPage;
import dev.obscuria.archogenum.client.screen.elements.*;
import dev.obscuria.archogenum.client.screen.elements.details.PropertyDetails;
import dev.obscuria.archogenum.client.screen.elements.details.SynthesisCostDetails;
import dev.obscuria.archogenum.client.screen.elements.details.TraitsDetails;
import dev.obscuria.archogenum.client.screen.elements.panels.XenofruitsPanel;
import dev.obscuria.archogenum.client.screen.nodes.ButtonNode;
import dev.obscuria.archogenum.client.screen.nodes.TextNode;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import dev.obscuria.archogenum.client.screen.tool.Texture;
import dev.obscuria.archogenum.network.ServerboundSynthesizePayload;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import dev.obscuria.fragmentum.util.signal.Signal1;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class XenofruitsPage extends ArchoScreen {

    public static final Signal1<Xenotype> selected = new Signal1<>();

    public XenofruitsPage() {
        super(ScreenPage.XENOFRUITS);
    }

    @Override
    protected void init() {
        super.init();
        this.addChild(new DummyDisplay(center(), bottom(0), height));
        this.addChild(new XenofruitsPanel(left(10), top(10), 126, height - 20));
        this.addChild(new DetailsPanel(right(-134), top(10), 126, height - 20));
    }

    private static class DetailsPanel extends AbstractDetailsPanel {

        private static final Component XENOFRUIT_DESCRIPTION = Component.translatable("item.archogenum.xenofruit.desc");

        private final SynthesizeButton synthesizeButton;
        private Xenotype target = Xenotype.EMPTY;
        private Xenotype lastTarget = Xenotype.EMPTY;

        public DetailsPanel(int x, int y, int width, int height) {
            super(x, y, width, height);
            selected.connect(this, false, ArchoScreen.breaker, this::onSelected);
            synthesizeButton = new SynthesizeButton();
            synthesizeButton.clickAction = ClickAction.leftClick(this::synthesize);
            this.setFooter(synthesizeButton);
            this.update();
        }

        @Override
        protected boolean isUpdated() {
            if (Objects.equals(target, lastTarget)) return false;
            this.lastTarget = target;
            return true;
        }

        @Override
        protected Component getPlaceholder() {
            return Component.translatable("screen.archogenum.xenofruits.details_placeholder");
        }

        @Override
        protected Component getDisplayName() {
            return Component.literal("Xenofruit");
        }

        @Override
        protected void update() {
            DummyDisplay.xenotype = target;
            this.synthesizeButton.update(target);
            if (target.isEmpty()) return;

            this.content.addChild(new GraphNode(target));
            this.content.addChild(new SpacingNode(5));
            this.content.addChild(TextNode.description(XENOFRUIT_DESCRIPTION));
            this.content.addChild(SynthesisCostDetails.ofXenotype(target));
            this.content.addChild(new TraitsDetails(target));
            this.content.addChild(new PropertyDetails(target));
        }

        private void onSelected(Xenotype xenotype) {
            this.target = xenotype;
        }

        private void synthesize(SynthesizeButton button) {
            FragmentumNetworking.sendToServer(new ServerboundSynthesizePayload(target));
            button.synthesized();
        }
    }

    private static class SynthesizeButton extends ButtonNode {

        private boolean isSynthesized;

        public SynthesizeButton() {
            super(Component.literal("Synthesize"));
        }

        public void update(Xenotype xenotype) {
            this.active = !xenotype.isEmpty();
        }

        public void synthesized() {
            this.isSynthesized = true;
            setMessage(Component.literal("Synthesized"));
        }

        @Override
        public Texture pickTexture(boolean isHovered) {
            return isSynthesized
                    ? ArchoTextures.buttonPurple(isHovered)
                    : ArchoTextures.buttonGray(isHovered);
        }
    }
}
