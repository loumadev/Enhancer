package eu.loumadev.enhancer.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class SelectionEvent implements Listener {

    private Plugin plugin;

    public SelectionEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSelect(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.GHAST_TEAR) return;

        Player p = e.getPlayer();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            p.sendMessage("Primary selection point set to " + b);
            p.setMetadata("pos1", new FixedMetadataValue(this.plugin, b));
        }

        else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            p.sendMessage("Secondary selection point set to " + b);
            p.setMetadata("pos2", new FixedMetadataValue(this.plugin, b));
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e){
        if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GHAST_TEAR)
            e.setCancelled(true);
    }
}
