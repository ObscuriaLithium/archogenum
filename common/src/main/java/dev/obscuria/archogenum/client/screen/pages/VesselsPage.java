package dev.obscuria.archogenum.client.screen.pages;

import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.ScreenPage;
import dev.obscuria.archogenum.client.screen.elements.DummyDisplay;
import dev.obscuria.archogenum.client.screen.elements.panels.VesselDetailsPanel;
import dev.obscuria.archogenum.client.screen.elements.panels.VesselsPanel;
import dev.obscuria.archogenum.client.screen.tool.ClickAction;

public class VesselsPage extends ArchoScreen {

    public VesselsPage() {
        super(ScreenPage.VESSELS);
    }

    @Override
    protected void init() {
        super.init();
        this.addChild(new DummyDisplay(center(), bottom(0), height));
        this.addChild(new VesselsPanel(left(10), top(10), 126, height - 20,
                bundle -> ClickAction.flatLeftClick(it -> {
                    VesselDetailsPanel.target = bundle;
                    return true;
                })));
        this.addChild(new VesselDetailsPanel(right(-134), top(10), 126, height - 20));
    }
}
