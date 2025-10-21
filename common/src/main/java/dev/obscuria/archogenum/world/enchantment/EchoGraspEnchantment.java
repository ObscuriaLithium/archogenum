package dev.obscuria.archogenum.world.enchantment;

import dev.obscuria.archogenum.registry.ArchoEnchantments;
import dev.obscuria.archogenum.registry.ArchoItems;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.archogenum.world.genetics.basis.Genome;
import dev.obscuria.archogenum.world.genetics.behavior.LootDropper;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

public final class EchoGraspEnchantment extends Enchantment {

    public EchoGraspEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }

    public int getMinCost(int level) {
        return 10 * level;
    }

    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    public static void maybeDropVessel(LivingEntity entity, DamageSource source, LootDropper dropper) {
        if (!(entity.level() instanceof ServerLevel level)) return;
        if (!(source.getEntity() instanceof Player player)) return;
        final var weapon = player.getMainHandItem();
        if (EnchantmentHelper.getItemEnchantmentLevel(ArchoEnchantments.ECHO_GRASP.get(), weapon) <= 0) return;
        final @Nullable var genome = Genome.findFor(entity.getType()).orElse(null);
        if (genome == null || genome.isEmpty()) return;
        if (!consumeItemIfPresent(player, Items.ECHO_SHARD)) return;
        final var x = entity.getX();
        final var y = entity.getY() + entity.getEyeHeight();
        final var z = entity.getZ();
        level.playSound(null, x, y, z, SoundEvents.SCULK_CATALYST_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.SOUL, x, y, z, 1, 0.0D, 0, 0, 0);
        dropVessel(genome, dropper);
    }

    public static boolean consumeItemIfPresent(Player player, Item item) {
        if (player.getAbilities().instabuild && player.getInventory().hasAnyMatching(stack -> stack.is(item)))
            return true;
        final var removed = player.getInventory().clearOrCountMatchingItems(stack -> stack.is(item), 1, player.inventoryMenu.getCraftSlots());
        return removed > 0;
    }

    private static void dropVessel(Genome genome, LootDropper dropper) {
        final var vessel = ArchoItems.GENE_PACK.instantiate();
        final var genes = genome.unwrapGenes().map(it -> new GeneInstance(it, 1)).toList();
        EchoVesselItem.setStoredGenes(vessel, new StoredGenes(genes));
        dropper.drop(vessel);
    }
}
