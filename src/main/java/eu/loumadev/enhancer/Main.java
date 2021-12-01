package eu.loumadev.enhancer;

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
		this.getServer().getPluginCommand("struct").setExecutor(new StructureCommand(this));
		this.getServer().getPluginManager().registerEvents(new SelectionEvent(this), this);
	}

	@Override
	public void onDisable() {
		this.getLogger().info("The plugin is disabling!");
	}
}
