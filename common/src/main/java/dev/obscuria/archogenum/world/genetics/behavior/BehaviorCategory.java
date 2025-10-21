package dev.obscuria.archogenum.world.genetics.behavior;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

public enum BehaviorCategory implements StringRepresentable {
    BENEFICIAL(ChatFormatting.BLUE),
    HARMFUL(ChatFormatting.RED);

    public static final Codec<BehaviorCategory> CODEC = StringRepresentable.fromEnum(BehaviorCategory::values);

    public final ChatFormatting style;

    BehaviorCategory(ChatFormatting style) {
        this.style = style;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
