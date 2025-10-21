package dev.obscuria.archogenum.client.screen.pages;

import dev.obscuria.archogenum.client.ClientGeneticProfile;
import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.ScreenPage;
import dev.obscuria.archogenum.client.screen.elements.DummyDisplay;
import dev.obscuria.archogenum.client.screen.elements.panels.GeneDetailsPanel;
import dev.obscuria.archogenum.client.screen.elements.panels.GenesPanel;

public class GenesPage extends ArchoScreen {

    public GenesPage() {
        super(ScreenPage.GENES);
    }

    @Override
    protected void init() {
        super.init();
        this.addChild(new DummyDisplay(center(), bottom(0), height));
        this.addChild(new GenesPanel(left(10), top(10), 126, height - 20));
        this.addChild(new GeneDetailsPanel(right(-134), top(10), 126, height - 20));
        updateDummyVisibility();
    }

    public static void updateDummyVisibility() {
        if (GeneDetailsPanel.target == null) return;
        DummyDisplay.isHidden = !ClientGeneticProfile.INSTANCE.discoveries().isKnown(GeneDetailsPanel.target);
    }
}
