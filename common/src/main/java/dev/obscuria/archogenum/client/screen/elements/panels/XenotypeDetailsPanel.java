package dev.obscuria.archogenum.client.screen.elements.panels;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.containers.PageContainer;
import dev.obscuria.archogenum.client.screen.containers.ScrollContainer;
import dev.obscuria.archogenum.client.screen.elements.XenofruitDisplay;
import dev.obscuria.archogenum.client.screen.nodes.ButtonNode;
import dev.obscuria.archogenum.client.screen.nodes.HeaderNode;
import dev.obscuria.archogenum.client.screen.pages.XenofruitsPage;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;
import dev.obscuria.archogenum.network.ServerboundSynthesizePayload;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class XenotypeDetailsPanel extends PageContainer {

    private final HeaderNode header;
    private final ButtonNode synthesizeButton;
    private @Nullable Xenotype xenotype;

    public XenotypeDetailsPanel(int x, int y, int width, int height) {
        super(x, y, width, height);

        header = new HeaderNode(Component.literal("Details"));
        this.setHeader(header);

        final var scroll = new ScrollContainer(CommonComponents.EMPTY);
        final var list = new ListContainer(0, 1, 0);
        final var xenotypes = ClientGeneticProfile.INSTANCE.xenotypes().xenotypes();
        xenotypes.forEach(xenotype -> list.addChild(new XenofruitDisplay(xenotype)));
        scroll.addChild(list);
        this.setBody(scroll);

        synthesizeButton = new ButtonNode(Component.literal("Synthesize"));
        synthesizeButton.clickAction = ClickAction.leftClick(this::synthesize);
        this.setFooter(synthesizeButton);

        XenofruitsPage.selected.connect(this, false, ArchoScreen.breaker, this::onSelected);
    }

    private void onSelected(Xenotype xenotype) {
        this.xenotype = xenotype;
        this.header.setContent(Component.literal("Xenotype"));
    }

    private void synthesize(ButtonNode button) {
        if (this.xenotype == null) return;
        FragmentumNetworking.sendToServer(new ServerboundSynthesizePayload(xenotype));
    }
}
