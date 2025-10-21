package dev.obscuria.archogenum.registry;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.world.enchantment.EchoGraspEnchantment;
import dev.obscuria.fragmentum.registry.Deferred;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public interface ArchoEnchantments {

    Deferred<Enchantment, EchoGraspEnchantment> ECHO_GRASP = create("echo_grasp", () -> new EchoGraspEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    private static <T extends Enchantment> Deferred<Enchantment, T> create(String name, Supplier<T> value) {
        return ArchoRegistries.REGISTRAR.register(Registries.ENCHANTMENT, Archogenum.key(name), value);
    }

    static void init() {}
}
