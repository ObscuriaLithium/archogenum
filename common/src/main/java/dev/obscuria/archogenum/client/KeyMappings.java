package dev.obscuria.archogenum.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.obscuria.archogenum.client.screen.ScreenPage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public interface KeyMappings {

    String CATEGORY = "key_category.archo";

    KeyMapping COLLECTION = new KeyMapping("key.archo.collection", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY);

    static void collectionPressed() {
        if (Minecraft.getInstance().screen != null) return;
        ScreenPage.GENES.openScreen();
    }
}
