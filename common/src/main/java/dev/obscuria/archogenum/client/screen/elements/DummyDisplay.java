package dev.obscuria.archogenum.client.screen.elements;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.archogenum.client.screen.ArchoPalette;
import dev.obscuria.archogenum.client.screen.ArchoTextures;
import dev.obscuria.archogenum.client.screen.GuiUtil;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import dev.obscuria.fragmentum.util.easing.Easing;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DummyDisplay extends AbstractWidget {

    public static Xenotype xenotype = Xenotype.EMPTY;
    public static boolean isHidden = false;

    private static final AbstractClientPlayer dummyPlayer;
    private static final List<Particle> particles = new ArrayList<>();

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

        for (var particle : this.particles) particle.render(graphics);

        XenotypeHandler.setXenotype(dummyPlayer, xenotype);
        final var virtualMouseX = (getX() - mouseX) / 4f;
        final var virtualMouseY = (getY() - mouseY) / 4f - (scale / 2f);

        if (isHidden) {
            RenderSystem.setShaderColor(0f, 0f, 0f, 1f);
            renderEntity(graphics, getX(), getY(), scale, virtualMouseX, virtualMouseY, dummyPlayer);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            graphics.pose().pushPose();
            graphics.pose().translate(getX(), getY() - scale * 1.1f, 400);
            graphics.pose().scale(2f, 2f, 1f);
            GuiUtil.draw(graphics, ArchoTextures.LOCK, -12, -12, 24, 24);
            graphics.pose().popPose();
        } else {
            renderEntity(graphics, getX(), getY(), scale, virtualMouseX, virtualMouseY, dummyPlayer);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public void tick() {
        dummyPlayer.tick();
        dummyPlayer.tickCount++;

        particles.removeIf(Particle::isExpired);
        if (dummyPlayer.tickCount % 14 != 0) return;
        particles.add(new Particle(new Vector2f(
                getX() - 100f + RandomUtils.nextFloat(0f, 200f),
                getY() + RandomUtils.nextFloat(10f, 100))));
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
        dummyPlayer = new AbstractClientPlayer(level, player.connection.getLocalGameProfile()) {

            @Override
            public boolean isModelPartShown(PlayerModelPart part) {
                final var player = Objects.requireNonNull(Minecraft.getInstance().player);
                return player.isModelPartShown(part);
            }
        };
    }

    private static class Particle {

        private final long startTime = Util.getMillis();
        private final Vector2f start;
        private final Vector2f end;

        public Particle(Vector2f start) {
            this.start = start;
            this.end = new Vector2f(start.x, start.y - 250);
        }

        public void render(GuiGraphics graphics) {
            final var delta = (Util.getMillis() - startTime) / 3000f;
            final var color = ArchoPalette.WHITE.lerp(ArchoPalette.ACCENT, delta);
            final var scale = Easing.EASE_OUT_CUBIC.compute(1f - delta);
            final var offset = 30f * Math.cos(start.y + delta * 2f);
            graphics.pose().pushPose();
            graphics.pose().translate(
                    Mth.lerp(delta, start.x, end.x) + offset,
                    Mth.lerp(delta, start.y, end.y), 0);
            graphics.pose().scale(scale, scale, 1f);
            graphics.fill(-1, -1, 1, 1, color.decimal());
            graphics.pose().popPose();
        }

        public boolean isExpired() {
            return Util.getMillis() - startTime > 3000;
        }
    }
}
