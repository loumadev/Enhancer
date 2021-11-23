package eu.loumadev.enhancer;

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
	}

	@Override
	public void onDisable() {
		this.getLogger().info("The plugin is disabling!");
	}
}
