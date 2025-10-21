package dev.obscuria.archogenum.client.screen;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.client.screen.pages.GenesPage;
import dev.obscuria.archogenum.client.screen.pages.VesselsPage;
import dev.obscuria.archogenum.client.screen.pages.AssemblyPage;
import dev.obscuria.archogenum.client.screen.pages.XenofruitsPage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public enum ScreenPage implements StringRepresentable {
    MY_XENOTYPE(GLFW.GLFW_KEY_0, GenesPage::new),
    GENES(GLFW.GLFW_KEY_1, GenesPage::new),
    VESSELS(GLFW.GLFW_KEY_2, VesselsPage::new),
    XENOFRUITS(GLFW.GLFW_KEY_3, XenofruitsPage::new),
    ASSEMBLY(GLFW.GLFW_KEY_4, AssemblyPage::new);

    private final int hotkey;
    private final Supplier<ArchoScreen> factory;

    ScreenPage(int hotkey, Supplier<ArchoScreen> factory) {
        this.hotkey = hotkey;
        this.factory = factory;
    }

    public void openScreen() {
        Minecraft.getInstance().setScreen(factory.get());
    }

    public ResourceLocation getIcon() {
        return Archogenum.key("textures/gui/page/%s.png".formatted(getSerializedName()));
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
