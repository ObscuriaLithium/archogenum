package dev.obscuria.archogenum.world.genetics.basis;

import com.mojang.serialization.Codec;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.client.screen.ArchoPalette;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public enum GeneCategory implements StringRepresentable {
    BENEFICIAL(ChatFormatting.GREEN),
    COSMETIC(ChatFormatting.LIGHT_PURPLE),
    HARMFUL(ChatFormatting.RED);

    public static final Codec<GeneCategory> CODEC = StringRepresentable.fromEnum(GeneCategory::values);

    public final ResourceLocation overlay;
    public final ResourceLocation icon;
    public final ChatFormatting color;
    public final ARGB paleColor;

    @SuppressWarnings("DataFlowIssue")
    GeneCategory(ChatFormatting color) {
        this.overlay = Archogenum.key("textures/gui/category/%s.png".formatted(getSerializedName()));
        this.icon = Archogenum.key("textures/gui/trait/%s.png".formatted(getSerializedName()));
        this.color = color;
        this.paleColor = Colors.argbOf(color.getColor()).lerp(ArchoPalette.LIGHT, 0.33f);
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
