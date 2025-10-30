package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import dev.obscuria.fragmentum.script.ScriptInstance;
import dev.obscuria.fragmentum.script.ServerScriptLoader;
import dev.obscuria.fragmentum.script.libs.FactoryLib;
import dev.obscuria.fragmentum.script.types.LuaLivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.concurrent.atomic.AtomicReference;

public record ScriptTrait(
        GeneCategory category,
        ResourceLocation script,
        AtomicReference<@Nullable ScriptInstance> instance
) implements ITrait {

    public static final Codec<ScriptTrait> CODEC;

    public ScriptTrait(GeneCategory category, ResourceLocation script) {
        this(category, script, new AtomicReference<>());
    }

    @Override
    public Codec<ScriptTrait> codec() {
        return CODEC;
    }

    @Override
    public void tick(ServerLevel level, LivingEntity entity, GeneInstance gene) {
        final @Nullable var function = getFunction(level, "tick");
        if (function != null) function.call(new LuaLivingEntity<>(entity));
    }

    private @Nullable LuaValue getFunction(ServerLevel level, String name) {
        this.maybeLoadScript(level);
        final @Nullable var instance = instance().get();
        if (instance == null) return null;
        final var value = instance.globals().get(name);
        return value.isfunction() ? value : null;
    }

    private void maybeLoadScript(ServerLevel level) {
        if (instance.get() != null) return;
        final var globals = JsePlatform.standardGlobals();
        globals.set("factory", new FactoryLib());
        instance.set(ServerScriptLoader.load(level.getServer(), script, globals));
    }

    @Override
    public GeneCategory defaultCategory() {
        return category;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                GeneCategory.CODEC.fieldOf("category").forGetter(ScriptTrait::category),
                ResourceLocation.CODEC.fieldOf("script").forGetter(ScriptTrait::script)
        ).apply(codec, ScriptTrait::new));
    }
}
