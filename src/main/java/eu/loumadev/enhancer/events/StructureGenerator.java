package eu.loumadev.enhancer.events;

import eu.loumadev.enhancer.structures.Structure;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Random;
import java.util.logging.Level;

public class StructureGenerator implements Listener {

	private Plugin plugin;

	public StructureGenerator(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onGenerate(final ChunkLoadEvent e) {
		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
			@Override
			public void run() {
				if(e.isNewChunk()) {

					Chunk chunk = e.getChunk();
					Biome biome = chunk.getBlock(0,0, 0).getBiome();

					if (biome == Biome.FOREST || biome == Biome.FLOWER_FOREST) {
						Random rand = new Random();
						if (rand.nextDouble(100) < 25.00) {
							int x = rand.nextInt(16);
							int z = rand.nextInt(16);

							for (int y = 255; y >= 0; y--){
								if(chunk.getBlock(x, y, z).getType() == Material.GRASS_BLOCK){
									try {
										Structure.build(
												chunk.getBlock(x, y + 1, z).getLocation(),
												new File(plugin.getDataFolder(), "/structures/fallen_log.json")
										);
										//plugin.getLogger().log(Level.INFO, "Generated a fallen log on " + chunk.getBlock(x,y+1,z));
									}
									catch (Exception e) {
										e.printStackTrace();
									}
									break;
								}
							}
						}
					}
				}
			}
		}, 20);
	}
}
