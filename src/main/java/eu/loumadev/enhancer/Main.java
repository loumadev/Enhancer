package eu.loumadev.enhancer;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getLogger().info("The plugin is enabling...!");
	}

	@Override
	public void onDisable() {
		this.getLogger().info("The plugin is disabling!");
	}
}
