package dev.obscuria.archogenum.client.toast;

import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

public final class NewGeneToast implements Toast {

    private static final long DISPLAY_TIME = 5000L;
    private static final Component TITLE_TEXT = Component.translatable("toast.archogenum.new_gene.title");
    private final Holder<Gene> gene;

    public NewGeneToast(Holder<Gene> gene) {
        this.gene = gene;
    }

    public Visibility render(GuiGraphics graphics, ToastComponent component, long timeSinceLastVisible) {
        graphics.blit(TEXTURE, 0, 0, 0, 32, this.width(), this.height());
        graphics.drawString(component.getMinecraft().font, TITLE_TEXT, 30, 7, -11534256, false);
        graphics.drawString(component.getMinecraft().font, gene.value().getDisplayName(gene), 30, 18, -16777216, false);
        GuiUtil.drawGene(graphics, gene, 16, 16, false);
        return timeSinceLastVisible >= DISPLAY_TIME * component.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }
}
