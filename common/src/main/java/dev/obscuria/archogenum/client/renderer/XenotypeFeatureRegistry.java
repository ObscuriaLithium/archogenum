package dev.obscuria.archogenum.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class XenotypeFeatureRegistry {

    private static final Map<ResourceLocation, FeatureProvider> REGISTRY = new HashMap<>();

    public static void register(ResourceLocation id, FeatureProvider provider) {
        REGISTRY.put(id, provider);
    }

    public static @Nullable FeatureProvider get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    @FunctionalInterface
    public interface FeatureProvider {

        <T extends LivingEntity> XenotypeFeature<T> create(EntityRendererProvider.Context context);
    }
}
