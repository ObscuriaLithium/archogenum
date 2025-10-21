package dev.obscuria.archogenum.client.screen.elements;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Objects;

public class DummyDisplay extends AbstractWidget {

    public static Xenotype xenotype = Xenotype.EMPTY;
    public static boolean isHidden = false;

    private static final AbstractClientPlayer DUMMY_PLAYER;

    private final int scale;

    public DummyDisplay(int x, int y, int guiHeight) {
        super(x, y, 0, 0, CommonComponents.EMPTY);
        this.scale = (int) (guiHeight * 0.4f);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        RenderSystem.enableBlend();
        graphics.pose().pushPose();
        graphics.pose().translate(getX(), getY(), 0);
        final var shineScale = scale / 100f;
        graphics.pose().scale(shineScale, shineScale, 1f);
        GuiUtil.draw(graphics, ArchoTextures.SHINE_X256, -128, -256, 256, 256);
        graphics.pose().popPose();
        RenderSystem.disableBlend();

        XenotypeHandler.setXenotype(DUMMY_PLAYER, xenotype);
        final var virtualMouseX = (getX() - mouseX) / 4f;
        final var virtualMouseY = (getY() - mouseY) / 4f - (scale / 2f);

        if (isHidden) {
            RenderSystem.setShaderColor(0f, 0f, 0f, 1f);
            renderEntity(graphics, getX(), getY(), scale, virtualMouseX, virtualMouseY, DUMMY_PLAYER);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            graphics.pose().pushPose();
            graphics.pose().translate(getX(), getY() - scale * 1.1f, 400);
            graphics.pose().scale(2f, 2f, 1f);
            GuiUtil.draw(graphics, ArchoTextures.LOCK, -12, -12, 24, 24);
            graphics.pose().popPose();
        } else {
            renderEntity(graphics, getX(), getY(), scale, virtualMouseX, virtualMouseY, DUMMY_PLAYER);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public void tick() {
        DUMMY_PLAYER.tick();
        DUMMY_PLAYER.tickCount++;
    }

    private static void renderEntity(GuiGraphics graphics, int x, int y, int scale, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float) Math.atan(mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float) Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = entity.yBodyRot;
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F + f * 20.0F;
        entity.yBodyRotO = entity.yBodyRot;
        entity.setYRot(180.0F + f * 40.0F);
        entity.setXRot(-f1 * 20.0F);
        entity.xRotO = entity.getXRot();
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        renderEntity(graphics, x, y, scale, quaternionf, quaternionf1, entity);
//        entity.yBodyRot = f2;
//        entity.setYRot(f3);
//        entity.setXRot(f4);
//        entity.yHeadRotO = f5;
//        entity.yHeadRot = f6;
    }

    private static void renderEntity(GuiGraphics graphics, int x, int y, int scale,
                                     Quaternionf pose, @javax.annotation.Nullable Quaternionf cameraOrientation,
                                     LivingEntity entity) {
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 100);
        graphics.pose().mulPoseMatrix((new Matrix4f()).scaling((float) scale, (float) scale, (float) (-scale)));
        graphics.pose().mulPose(pose);
        Lighting.setupForEntityInInventory();
        final var renderer = Minecraft.getInstance().getEntityRenderDispatcher();

        if (cameraOrientation != null) {
            cameraOrientation.conjugate();
            renderer.overrideCameraOrientation(cameraOrientation);
        }

        renderer.setRenderShadow(false);
        renderer.render(entity,
                0.0F, 0.0F, 0.0F, 0.0F, Minecraft.getInstance().getFrameTime(),
                graphics.pose(), graphics.bufferSource(), 15728880);
        graphics.flush();
        renderer.setRenderShadow(true);
        graphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    static {
        final var player = Objects.requireNonNull(Minecraft.getInstance().player);
        final var level = Objects.requireNonNull(Minecraft.getInstance().level);
        DUMMY_PLAYER = new AbstractClientPlayer(level, player.connection.getLocalGameProfile()) {

            @Override
            public boolean isModelPartShown(PlayerModelPart part) {
                final var player = Objects.requireNonNull(Minecraft.getInstance().player);
                return player.isModelPartShown(part);
            }
        };
    }
}
