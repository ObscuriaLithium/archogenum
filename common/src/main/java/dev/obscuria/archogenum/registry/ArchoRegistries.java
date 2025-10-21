package dev.obscuria.archogenum.registry;

import com.mojang.serialization.Codec;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.world.genetics.basis.Archetype;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.basis.Genome;
import dev.obscuria.archogenum.world.genetics.behavior.ITrait;
import dev.obscuria.archogenum.world.genetics.behavior.conditions.ICondition;
import dev.obscuria.archogenum.world.genetics.behavior.effects.IEffect;
import dev.obscuria.archogenum.world.genetics.behavior.triggers.ITrigger;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.fragmentum.registry.DelegatedRegistry;
import dev.obscuria.fragmentum.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface ArchoRegistries {

    Registrar REGISTRAR = FragmentumRegistry.registrar(Archogenum.MODID);

    DelegatedRegistry<Codec<? extends ITrait>> TRAIT_TYPE = REGISTRAR.createRegistry(Keys.TRAIT_TYPE);
    DelegatedRegistry<Codec<? extends ITrigger>> TRIGGER_TYPE = REGISTRAR.createRegistry(Keys.TRIGGER_TYPE);
    DelegatedRegistry<Codec<? extends IEffect>> EFFECT_TYPE = REGISTRAR.createRegistry(Keys.EFFECT_TYPE);
    DelegatedRegistry<Codec<? extends ICondition>> CONDITION_TYPE = REGISTRAR.createRegistry(Keys.CONDITION_TYPE);

    interface Keys {

        ResourceKey<Registry<Codec<? extends ITrait>>> TRAIT_TYPE = create("trait_type");
        ResourceKey<Registry<Codec<? extends ITrigger>>> TRIGGER_TYPE = create("trigger_type");
        ResourceKey<Registry<Codec<? extends IEffect>>> EFFECT_TYPE = create("effect_type");
        ResourceKey<Registry<Codec<? extends ICondition>>> CONDITION_TYPE = create("condition_type");

        ResourceKey<Registry<Archetype>> ARCHETYPE = create("archetype");
        ResourceKey<Registry<ITrait>> TRAIT = create("trait");
        ResourceKey<Registry<Gene>> GENE = create("gene");
        ResourceKey<Registry<Genome>> GENOME = create("genome");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(Archogenum.key(name));
        }
    }

    static void init() {

        ArchoSounds.init();
        ArchoEnchantments.init();
        ArchoAttributes.init();
        ArchoItems.init();
        ArchoCreativeTabs.init();

        ITrait.bootstrap(BootstrapContext.create(REGISTRAR, Keys.TRAIT_TYPE, Archogenum::key));
        ITrigger.bootstrap(BootstrapContext.create(REGISTRAR, Keys.TRIGGER_TYPE, Archogenum::key));
        IEffect.bootstrap(BootstrapContext.create(REGISTRAR, Keys.EFFECT_TYPE, Archogenum::key));
        ICondition.bootstrap(BootstrapContext.create(REGISTRAR, Keys.CONDITION_TYPE, Archogenum::key));

        REGISTRAR.createSyncedDataRegistry(Keys.ARCHETYPE, () -> Archetype.DIRECT_CODEC);
        REGISTRAR.createSyncedDataRegistry(Keys.TRAIT, () -> ITrait.DIRECT_CODEC);
        REGISTRAR.createSyncedDataRegistry(Keys.GENE, () -> Gene.DIRECT_CODEC);
        REGISTRAR.createSyncedDataRegistry(Keys.GENOME, () -> Genome.DIRECT_CODEC);
    }
}
