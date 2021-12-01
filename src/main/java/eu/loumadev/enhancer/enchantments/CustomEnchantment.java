package eu.loumadev.enhancer.enchantments;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import eu.loumadev.enhancer.Main;
import eu.loumadev.enhancer.utils.RomanNumber;
import net.kyori.adventure.text.Component;

public abstract class CustomEnchantment extends Enchantment {
	//private static int autoId = 100;
	//private static JavaPlugin plugin = null;

	public CustomEnchantment(String namespace) {
		//super(new NamespacedKey(plugin, String.valueOf(autoId)));
		//super(NamespacedKey.minecraft(namespace));
		super(createNamespacedKey(namespace));
	}

	private static NamespacedKey createNamespacedKey(String namespace) {
		return NamespacedKey.minecraft("enhancer_" + namespace);
	}

	public static Map<Enchantment, Integer> getCustomEnchantments(ItemStack item) {
		return item.getEnchantments().entrySet().stream()
		           .filter(entry -> isCustom(entry.getKey()))
		           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static void updateLore(ItemStack item) {
		Map<Enchantment, Integer> enchantments = getCustomEnchantments(item);
		ItemMeta meta = item.getItemMeta();

		meta.lore(
			enchantments.entrySet().stream()
			    .filter(entry -> isCustom(entry.getKey()))
			    .map(entry -> entry.getKey().displayName(entry.getValue()))
			    .collect(Collectors.toList())
		);

		item.setItemMeta(meta);
	}

	public static boolean isCustom(Enchantment enchantment) {
		return enchantment.getKey().asString().startsWith("minecraft:" + Main.getNamespace());
	}


	public void applyTo(ItemStack item, int level) {
		//Apply enchantment
		item.addUnsafeEnchantment(this, level);

		//Update lore
		updateLore(item);



		// ItemMeta meta = item.getItemMeta();
		// meta.addEnchant(this, 1, true);
		// item.setItemMeta(meta);
	}

	// public static void init(JavaPlugin pl) {
	// 	plugin = pl;
	// 	System.out.println("CustomEnchantment.init");
	// }

	public abstract boolean canEnchantItem(ItemStack item);

	public abstract EnchantmentTarget getItemTarget();

	public abstract int getMaxLevel();

	public abstract String getName();

	public abstract int getEnchantmentTableMinimumLevel();

	public abstract int getEnchantmentTableMaximumLevel();

	@Override
	public Component displayName(int level) {
		boolean hasTier = getStartLevel() != getMaxLevel();
		String tier = hasTier ? " " + RomanNumber.toRoman(level) : "";

		return Component.text(getName() + tier);
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public boolean isCursed() {
		return false;
	}
}

