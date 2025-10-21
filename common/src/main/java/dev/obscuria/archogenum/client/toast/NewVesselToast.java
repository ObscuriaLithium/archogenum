package dev.obscuria.archogenum.client.toast;

import dev.obscuria.archogenum.client.KeyMappings;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class NewVesselToast implements Toast {

    private static final long DISPLAY_TIME = 7500L;
    private static final Component TITLE_TEXT = Component.translatable("toast.archogenum.new_vessel.title");
    private final Component descriptionText = Component.translatable(
            "toast.archogenum.new_vessel.description",
            KeyMappings.COLLECTION.getTranslatedKeyMessage());
    private final ItemStack stack;

    public NewVesselToast(ItemStack stack) {
        this.stack = stack;
    }

    public Toast.Visibility render(GuiGraphics graphics, ToastComponent component, long timeSinceLastVisible) {
        graphics.blit(TEXTURE, 0, 0, 0, 32, this.width(), this.height());
        graphics.drawString(component.getMinecraft().font, TITLE_TEXT, 30, 7, -11534256, false);
        graphics.drawString(component.getMinecraft().font, descriptionText, 30, 18, -16777216, false);
        graphics.renderFakeItem(stack, 8, 8);
        return timeSinceLastVisible >= DISPLAY_TIME * component.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }
}
