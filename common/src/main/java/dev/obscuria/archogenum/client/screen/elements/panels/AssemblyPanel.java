package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.*;
import dev.obscuria.archogenum.client.screen.elements.details.AssemblyResultDetails;
import dev.obscuria.archogenum.client.screen.elements.details.PropertyDetails;
import dev.obscuria.archogenum.client.screen.elements.details.SynthesisCostDetails;
import dev.obscuria.archogenum.client.screen.elements.details.TraitsDetails;
import dev.obscuria.archogenum.client.screen.nodes.ButtonNode;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.pages.AssemblyPage;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import dev.obscuria.archogenum.client.screen.tool.Texture;
import dev.obscuria.archogenum.network.ServerboundSaveXenotypePayload;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import net.minecraft.network.chat.Component;

public class AssemblyPanel extends PageContainer {

    private final HeaderNode header;
    private final ListContainer content;
    private final ListContainer graph;
    private final ListContainer bundles;
    private final ListContainer details;
    private final ListContainer footer;
    private final ListContainer result;
    private final CreateButton createButton;

    public AssemblyPanel(int x, int y, int width, int height) {
        super(x, y, width, height);

        header = new HeaderNode(Component.literal("Assembly"));
        this.setHeader(header);

        final var scroll = new ScrollContainer(Component.translatable("screen.archogenum.assembly.placeholder"));
        content = new ListContainer();
        graph = new ListContainer(0, 5, 5);
        bundles = new ListContainer();
        details = new ListContainer(5, 5, 0);
        content.addChild(graph);
        content.addChild(bundles);
        content.addChild(details);
        scroll.addChild(content);
        this.setBody(scroll);

        footer = new ListContainer();
        result = new ListContainer();
        createButton = new CreateButton();
        createButton.clickAction = ClickAction.leftClick(this::save);
        footer.addChild(result);
        footer.addChild(createButton);
        this.setFooter(footer);

        AssemblyPage.bundleAdded.connect(this, false, ArchoScreen.breaker, this::onBundleAdded);
        AssemblyPage.bundleRemoved.connect(this, false, ArchoScreen.breaker, this::onBundleRemoved);
        update();
    }

    private void onBundleAdded(IBundleLike bundle) {
        final var display = new AbstractVesselDisplay.Simple(bundle);
        display.clickAction = ClickAction.leftClick(it -> AssemblyPage.bundleRemoved.emit(bundle));
        bundles.addChild(display);
        update();
    }

    private void onBundleRemoved(IBundleLike bundle) {
        bundles.listChildren().forEach(child -> {
            if (!(child instanceof AbstractVesselDisplay display)) return;
            if (!display.bundle.equals(bundle)) return;
            bundles.removeChild(child);
        });
        update();
    }

    private Xenotype assemble() {
        return Xenotype.assemble(bundles.listChildren()
                .filter(AbstractVesselDisplay.class::isInstance)
                .map(AbstractVesselDisplay.class::cast)
                .map(it -> it.bundle)
                .toList());
    }

    private void save(CreateButton button) {
        if (button.inCollection) return;
        final var xenotype = assemble();
        if (xenotype.isEmpty()) return;
        FragmentumNetworking.sendToServer(new ServerboundSaveXenotypePayload(xenotype));
        button.forceInCollection();
    }

    private void update() {

        this.graph.clearChildren();
        this.details.clearChildren();
        this.result.clearChildren();

        final var xenotype = assemble();
        DummyDisplay.xenotype = xenotype;
        createButton.update(xenotype);
        if (xenotype.isEmpty()) return;

        this.graph.addChild(new GraphNode(xenotype));
        this.details.addChild(SynthesisCostDetails.ofXenotype(xenotype));
        this.details.addChild(new TraitsDetails(xenotype));
        this.details.addChild(new PropertyDetails(xenotype));
        this.result.addChild(new AssemblyResultDetails(xenotype));
    }

    private static class CreateButton extends ButtonNode {

        private boolean inCollection;

        public CreateButton() {
            super(Component.literal("Create"));
        }

        public void update(Xenotype xenotype) {
            this.inCollection = ClientGeneticProfile.INSTANCE.xenotypes().contains(xenotype);
            this.active = !xenotype.isEmpty() && xenotype.complexity() <= 16;
            setMessage(inCollection
                    ? Component.literal("In Collection")
                    : Component.literal("Create"));
        }

        public void forceInCollection() {
            this.inCollection = true;
            setMessage(Component.literal("In Collection"));
        }

        @Override
        public Texture pickTexture(boolean isHovered) {
            return inCollection
                    ? ArchoTextures.buttonGreen(isHovered)
                    : ArchoTextures.buttonGray(isHovered);
        }
    }
}
