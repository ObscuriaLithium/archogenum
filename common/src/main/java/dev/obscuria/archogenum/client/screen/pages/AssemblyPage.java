package dev.obscuria.archogenum.client.screen.pages;

import dev.obscuria.archogenum.client.screen.ArchoScreen;
import dev.obscuria.archogenum.client.screen.ScreenPage;
import dev.obscuria.archogenum.client.screen.elements.DummyDisplay;
import dev.obscuria.archogenum.client.screen.elements.panels.AssemblyVesselsPanel;
import dev.obscuria.archogenum.client.screen.elements.panels.AssemblyPanel;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.fragmentum.util.signal.Signal1;

public class AssemblyPage extends ArchoScreen {

    public static final Signal1<IBundleLike> bundleAdded = new Signal1<>();
    public static final Signal1<IBundleLike> bundleRemoved = new Signal1<>();

    public AssemblyPage() {
        super(ScreenPage.ASSEMBLY);
    }

    @Override
    protected void init() {
        super.init();
        this.addChild(new DummyDisplay(center(), bottom(0), height));
        this.addChild(new AssemblyVesselsPanel(left(10), top(10), 126, height - 20));
        this.addChild(new AssemblyPanel(right(-134), top(10), 126, height - 20));
    }
}
