package eu.loumadev.enhancer;

import eu.loumadev.enhancer.enchantments.EnchantmentRegistrar;
import eu.loumadev.enhancer.events.StructureGenerator;
import eu.loumadev.enhancer.structures.Structure;
import org.bukkit.event.world.ChunkPopulateEvent;
import eu.loumadev.enhancer.commands.StructureCommand;
import eu.loumadev.enhancer.events.SelectionEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

		this.getServer().getPluginCommand("struct").setExecutor(new StructureCommand(this));
		this.getServer().getPluginManager().registerEvents(new SelectionEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new StructureGenerator(this), this);
	}

	@Override
	public void onDisable() {
		this.getLogger().info("The plugin is disabling!");
	}

	public static String getNamespace() {
		return "enhancer_";
	}
}
