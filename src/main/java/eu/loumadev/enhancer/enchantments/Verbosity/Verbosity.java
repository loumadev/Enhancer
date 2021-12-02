package eu.loumadev.enhancer.enchantments.Verbosity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import eu.loumadev.enhancer.enchantments.CustomEnchantment;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;

public class Verbosity extends CustomEnchantment implements Listener {

	public Verbosity(JavaPlugin plugin) {
		super("verbosity");

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	//Events

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(!item.hasItemMeta()) return;
		ItemMeta meta = item.getItemMeta();

		meta.getEnchants().forEach((enchantment, level) -> {
			event.getPlayer().sendMessage(enchantment.getName() + " " + level);
		});

		if(item.containsEnchantment(this)) {
			event.getPlayer().sendMessage("You just mined a block!");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(this, 1, true);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(this, 1);

		// List<String> lore = new ArrayList<String>();
		// lore.add(ChatColor.GRAY + "Verbosity I");
		// meta.setLore(lore);
		// item.setItemMeta(meta);

		//ItemMeta meta = item.getItemMeta();
		List<Component> lore = new ArrayList<Component>();
		lore.add(Component.text(ChatColor.GRAY + "Verbosity I"));
		meta.lore(lore);
		item.setItemMeta(meta);

		event.getPlayer().getInventory().setItem(0, item);
		event.getPlayer().sendMessage("You have obtained a special pickaxe!");
	}



	//Settings

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
		// return Arrays.asList(
		// 	Material.WOODEN_PICKAXE,
		// 	Material.STONE_PICKAXE,
		// 	Material.GOLDEN_PICKAXE,
		// 	Material.IRON_PICKAXE,
		// 	Material.DIAMOND_PICKAXE,
		// 	Material.NETHERITE_PICKAXE
		// ).contains(item.getType());
	}

	@Override
	public String getName() {
		return "Verbosity";
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public String translationKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEnchantmentTableMinimumLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEnchantmentTableMaximumLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<EquipmentSlot> getActiveSlots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getDamageIncrease(int arg0, EntityCategory arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EnchantmentRarity getRarity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDiscoverable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTradeable() {
		// TODO Auto-generated method stub
		return false;
	}
}
