package eu.loumadev.enhancer.enchantments;

import java.lang.NoSuchFieldException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.lang.IllegalAccessException;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import eu.loumadev.enhancer.Main;
import eu.loumadev.enhancer.enchantments.Verbosity.Verbosity;
import eu.loumadev.enhancer.utils.RomanNumber;
import net.kyori.adventure.text.Component;

enum EnchantmentRegistrationResult {
	ALREADY_REGISTERED,
	REGISTERED,
	FIELD_VALUE_FAILURE,
	NOT_ACCEPTING_FAILURE
}

public class EnchantmentRegistrar implements Listener {
	private static JavaPlugin plugin = null;
	static boolean registered = false;

	public EnchantmentRegistrar() {}

	private static void register() {
		//Register here all custom enchantments...

		registerLocalEnchantment(new Verbosity(plugin));

	}

	private static EnchantmentRegistrationResult registerLocalEnchantments() {
		if(registered) return EnchantmentRegistrationResult.ALREADY_REGISTERED;

		try {
			Field field = Enchantment.class.getDeclaredField("acceptingNew");
			field.setAccessible(true);
			field.set(null, true);
			field.setAccessible(false);

			EnchantmentRegistrar.register();
		}
		catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			return EnchantmentRegistrationResult.FIELD_VALUE_FAILURE;
		}
		catch(IllegalStateException e) {
			return EnchantmentRegistrationResult.NOT_ACCEPTING_FAILURE;
		}

		registered = true;

		return EnchantmentRegistrationResult.REGISTERED;
	}

	private static void registerLocalEnchantment(CustomEnchantment enchantment) {
		boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment);
		if(registered) return;

		Enchantment.registerEnchantment(enchantment);
		//register events
		ArrayList<RegisteredListener> listeners = HandlerList.getRegisteredListeners(plugin);
		System.out.println("Registered listeners: " + listeners);

		listeners.forEach(listener -> {
			System.out.println("listener: " + listener + ", instanceof enchantment: " + (listener.getListener() instanceof CustomEnchantment));
		});

		//boolean isRegistered = listeners.contains(enchantment);
		//plugin.getServer().getPluginManager().registerEvents(enchantment, plugin);
	}

	public static void init(JavaPlugin pl) {
		plugin = pl;

		plugin.getServer().getPluginManager().registerEvents(new EnchantmentRegistrar(), plugin);

		//CustomEnchantment.init(pl);
		EnchantmentRegistrationResult result = registerLocalEnchantments();

		if(result != EnchantmentRegistrationResult.REGISTERED) {
			plugin.getLogger().warning("Failed to register custom enchantments: " + result.toString());
		}
	}

	public static boolean isSet(Object any) {
		return any != null;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory() instanceof AnvilInventory) {
			System.out.println("\n");
			System.out.println("AnvilInventoryClickEvent");

			int slot = event.getSlot();
			int rawSlot = event.getRawSlot();

			final ItemStack o_firstItem = event.getInventory().getItem(0);
			final ItemStack o_secondItem = event.getInventory().getItem(1);

			System.out.println("Slot: " + slot + ", rawSlot: " + rawSlot);
			System.out.println("o_firstItem: " + isSet(o_firstItem) + ", o_secondItem: " + isSet(o_secondItem));

			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					AnvilInventory inventory = (AnvilInventory)event.getInventory();

					int slot = event.getSlot();
					int rawSlot = event.getRawSlot();

					System.out.println("Task > Slot: " + slot + ", rawSlot: " + rawSlot);

					// ItemStack resultItem = inventory.getResult();
					// if(resultItem == null) {
					// 	resultItem = inventory.getItem(0);
					// }

					ItemStack t_firstItem = inventory.getFirstItem();
					ItemStack t_secondItem = inventory.getSecondItem();

					System.out.println("Task > t_firstItem: " + isSet(t_firstItem) + ", t_secondItem: " + isSet(t_secondItem));


					ItemStack firstItem = t_firstItem;// == null ? o_firstItem : t_firstItem;
					ItemStack secondItem = t_secondItem;// == null ? o_secondItem : t_secondItem;

					System.out.println("Task > firstItem: " + isSet(firstItem) + ", secondItem: " + isSet(secondItem));

					if(firstItem == null || secondItem == null) {
						System.out.println("Event.invalidValuesReturn");

						return;
					}

					ItemStack resultItem = firstItem.clone();
					ItemMeta resultMeta = resultItem.getItemMeta();


					// System.out.println("Event.FirstItem.length: " + inventory.getFirstItem().getEnchantments().size());
					// inventory.getFirstItem().getEnchantments().forEach((enchantment, level) -> {
					// 	System.out.println("Event.FirstItem: " + enchantment.getName() + " " + level);
					// });

					// System.out.println("Event.result.length: " + resultItem.getEnchantments().size());
					// resultItem.getEnchantments().forEach((enchantment, level) -> {
					// 	System.out.println("Event.result: " + enchantment.getName() + " " + level);
					// });

					// Map<Enchantment, Integer> result = new HashMap<Enchantment, Integer>();
					// List<Map<Enchantment, Integer>> combined = new ArrayList<Map<Enchantment, Integer>>();
					// combined.add(firstItem.getEnchantments());
					// combined.add(secondItem.getEnchantments());

					Map<Enchantment, Integer> result = new HashMap<>(firstItem.getEnchantments());

					System.out.println("Task > result (enchantments:before): " + result);

					List<Component> customLores = new ArrayList<>();
					// List<Component> resultLores = resultMeta.lore();

					// if(resultLores != null) {
					// 	customLores.addAll(resultLores);
					// }

					System.out.println("Task > customLores: " + customLores);

					//EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta)resultMeta;
					Map<Enchantment, Integer> secondEnchants = null;

					if(secondItem.getType() == Material.ENCHANTED_BOOK) {
						System.out.println("Task > secondItem is EnchantmentStorageMeta");
						secondEnchants = ((EnchantmentStorageMeta)secondItem.getItemMeta()).getStoredEnchants();
					} else {
						System.out.println("Task > secondItem is ItemStack");
						secondEnchants = secondItem.getItemMeta().getEnchants();
						System.out.println("Task > secondItem.enchants: " + secondEnchants);
					}


					for(Map.Entry<Enchantment, Integer> entry : /*secondItem.getEnchantments()*/ secondEnchants.entrySet()) {
						Enchantment enchantment = entry.getKey();
						Integer level = entry.getValue();

						//Check if the item can accept the enchantment
						if(!enchantment.canEnchantItem(firstItem)) {
							System.out.println("Event.cannotEnchantItem: " + enchantment.getName());
							continue;
						}

						//Check for conflicts with existing enchantments
						if(checkForConflicts(enchantment, firstItem)) {
							System.out.println("Event.collision: " + enchantment.getName());
							continue;
						}

						//if(enchantment instanceof CustomEnchantment) customLores.add(getDisplayName(enchantment, level));
						// if(isCustom(enchantment)) {
						// 	System.out.println("Event.isCustom: adding custom lore");
						// 	customLores.add(getDisplayName(enchantment, level));
						// }
						System.out.println("LOOP: Enchantment: " + enchantment.getName() + " " + level + ", isCustom: " + CustomEnchantment.isCustom(enchantment));
						//System.out.println("LOOP: Enchantment: " + enchantment.getName() + " " + level + "isCustom: " + (enchantment instanceof CustomEnchantment));

						if(result.containsKey(enchantment)) {
							//System.out.println("LOOP (1): " + enchantment.getName() + " = " + Math.min(result.get(enchantment) + level, enchantment.getMaxLevel()));
							result.put(enchantment, Math.min(result.get(enchantment) + level, enchantment.getMaxLevel()));
							//resultMeta.addEnchant(enchantment, Math.min(result.get(enchantment) + level, enchantment.getMaxLevel()), true);
							//resultItem.addEnchantment(enchantment, Math.min(result.get(enchantment) + level, enchantment.getMaxLevel()));
						} else {
							//System.out.println("LOOP (2): " + enchantment.getName() + " = " + level);
							result.put(enchantment, level);
							//resultItem.addEnchantment(enchantment, level);
							//resultMeta.addEnchant(enchantment, level, true);
						}
					}

					System.out.println("Task > result (enchantments:after): " + result);

					//resultItem.addEnchantments(result);
					//List<Component> lores = resultMeta.lore();

					customLores.addAll(
						result.entrySet().stream()
						    .filter(entry -> CustomEnchantment.isCustom(entry.getKey()))
						    .map(entry -> getDisplayName(entry.getKey(), entry.getValue()))
						    .collect(Collectors.toList())
					);

					System.out.println("Task > customLores: " + customLores);

					// Arrays.stream(customLores.toArray())
					//     .filter(lore -> !lores.contains(lore))
					//     .forEach(lore -> lores.add((Component)lore));

					//resultItem.getItemMeta().setDisplayName("Test Pick");
					resultMeta.lore(customLores);
					//resultMeta.lore(lores);
					// resultMeta.setLore(new ArrayList<String>() {{
					// 	add("Test");
					// 	add("Test2");
					// }});
					//resultMeta.addEnchant(Enchantment.DURABILITY, 3, true);

					//Loop the results map and add the enchantments to the result meta
					for(Map.Entry<Enchantment, Integer> entry : result.entrySet()) {
						Enchantment enchantment = entry.getKey();
						Integer level = entry.getValue();
						//resultItem.addEnchantment(enchantment, level);
						resultMeta.addEnchant(enchantment, level, true);
					}

					resultItem.setItemMeta(resultMeta);
					inventory.setResult(resultItem);

					Player player = (Player)(event.getWhoClicked());
					//player.updateInventory();

					final ItemStack item = resultItem;

					resultItem.getEnchantments().forEach((enchantment, level) -> {
						System.out.println("Event.result: " + enchantment.getName() + " " + level);
					});

					//plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					//	public void run() {
					// We need it

					//inventory.setResult(item);
					//		player.updateInventory();
					//	}
					//});
				}
			});


			// System.out.println("Event.result.length: " + resultItem.getEnchantments().size());
			// resultItem.getEnchantments().forEach((enchantment, level) -> {
			// 	System.out.println("Event.result: " + enchantment.getName() + " " + level);
			// });

		}
	}


	// @EventHandler
	// public void onPrepareAnvil(PrepareAnvilEvent event) {
	// 	System.out.println("PrepareAnvilEvent");

	// 	AnvilInventory inventory = event.getInventory();
	// 	ItemStack resultItem = inventory.getResult();
	// 	if(resultItem == null) return;

	// 	ItemStack firstItem = inventory.getFirstItem();
	// 	ItemStack secondItem = inventory.getSecondItem();


	// 	System.out.println("Event.FirstItem.length: " + inventory.getFirstItem().getEnchantments().size());
	// 	inventory.getFirstItem().getEnchantments().forEach((enchantment, level) -> {
	// 		System.out.println("Event.FirstItem: " + enchantment.getName() + " " + level);
	// 	});

	// 	System.out.println("Event.result.length: " + resultItem.getEnchantments().size());
	// 	resultItem.getEnchantments().forEach((enchantment, level) -> {
	// 		System.out.println("Event.result: " + enchantment.getName() + " " + level);
	// 	});

	// 	// Map<Enchantment, Integer> result = new HashMap<Enchantment, Integer>();
	// 	// List<Map<Enchantment, Integer>> combined = new ArrayList<Map<Enchantment, Integer>>();
	// 	// combined.add(firstItem.getEnchantments());
	// 	// combined.add(secondItem.getEnchantments());

	// 	// Map<Enchantment, Integer> result = firstItem.getEnchantments();
	// 	Map<Enchantment, Integer> result = new HashMap<>(firstItem.getEnchantments());

	// 	for(Map.Entry<Enchantment, Integer> entry : secondItem.getEnchantments().entrySet()) {
	// 		Enchantment enchantment = entry.getKey();
	// 		Integer level = entry.getValue();

	// 		//Check if the item can accept the enchantment
	// 		if(!enchantment.canEnchantItem(firstItem)) continue;

	// 		//Check for conflicts with existing enchantments
	// 		if(checkForCollisions(enchantment, firstItem)) continue;

	// 		if(result.containsKey(enchantment)) {
	// 			result.put(enchantment, Math.min(result.get(enchantment) + level, enchantment.getMaxLevel()));
	// 		} else {
	// 			result.put(enchantment, level);
	// 		}
	// 	}

	// 	ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
	// 	pickaxe.addEnchantments(result);
	// 	pickaxe.getItemMeta().setDisplayName("Test Pick");
	// 	inventory.setResult(pickaxe);

	// 	//resultItem.addEnchantments(result);
	// 	//resultItem.getItemMeta().setDisplayName("Test Pick");
	// 	//inventory.setResult(resultItem);

	// 	System.out.println("Event.pickaxe.length: " + pickaxe.getEnchantments().size());
	// 	pickaxe.getEnchantments().forEach((enchantment, level) -> {
	// 		System.out.println("Event.pickaxe: " + enchantment.getName() + " " + level);
	// 	});
	// 	// System.out.println("Event.result.length: " + resultItem.getEnchantments().size());
	// 	// resultItem.getEnchantments().forEach((enchantment, level) -> {
	// 	// 	System.out.println("Event.result: " + enchantment.getName() + " " + level);
	// 	// });


	// 	// if(!resultItem.getEnchantments().containsKey(this)) {
	// 	// 	resultItem.addUnsafeEnchantment(this, 1); //TODO: Change constant to actual enchantment level
	// 	// }
	// }

	private static Component getDisplayName(Enchantment enchantment, int level) {
		return enchantment.displayName(level) == null ? Component.text("null") : enchantment.displayName(level);// + (enchantment.getMaxLevel() == enchantment.getStartLevel() ? "" : " " + RomanNumber.toRoman(level));
		//return enchantment.getName() + (enchantment.getMaxLevel() == enchantment.getStartLevel() ? "" : " " + RomanNumber.toRoman(level));
	}

	private static boolean checkForConflicts(Enchantment enchantment, ItemStack item) {
		for(Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
			Enchantment other = entry.getKey();
			if(enchantment != other && enchantment.conflictsWith(other)) return true;
		}

		return false;
	}
}
