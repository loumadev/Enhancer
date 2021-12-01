package eu.loumadev.enhancer;

import eu.loumadev.enhancer.enchantments.EnchantmentRegistrar;
import eu.loumadev.enhancer.structures.Structure;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Hello world!
 *
 */
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getLogger().info("The plugin is enabling!");

		//Setup custom enchantment class
		EnchantmentRegistrar.init(this);
	}

	@Override
	public void onDisable() {
		this.getLogger().info("The plugin is disabling!");
	}

	public static String getNamespace() {
		return "enhancer_";
	}
}
