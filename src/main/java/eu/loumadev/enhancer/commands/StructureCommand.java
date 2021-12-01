package eu.loumadev.enhancer.commands;

import eu.loumadev.enhancer.structures.Structure;
import eu.loumadev.enhancer.structures.WorldMismatchException;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StructureCommand implements CommandExecutor {

    private Plugin plugin;
    private Logger logger;

    public StructureCommand(Plugin pl) {
        this.plugin = pl;
        this.logger = pl.getLogger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;

            if(player.isOp()) {
                try {

                    if(strings[0].equalsIgnoreCase("save")) {

                        if(player.hasMetadata("pos1") && player.hasMetadata("pos2")) {
                            Block pos1 = (Block)player.getMetadata("pos1").get(0).value();
                            Block pos2 = (Block)player.getMetadata("pos2").get(0).value();

                            File dir = new File(plugin.getDataFolder() + File.separator + "structures");
                            if(!dir.exists()) dir.mkdir();

                            Structure.save(pos1, pos2, new File(dir, strings[1] + ".json"));
                            player.sendMessage("Saved successfully!");

                        } else player.sendMessage(ChatColor.RED + "Positions not set");

                    } else if(strings[0].equalsIgnoreCase("build")) {

                        Structure.build(player.getLocation(), new File(this.plugin.getDataFolder(), "structures/" + strings[1] + ".json"));
                        player.sendMessage("Built!");

                    }

                } catch(IndexOutOfBoundsException e) {
                    player.sendMessage(ChatColor.RED + command.getUsage());
                } catch(WorldMismatchException e) {
                    player.sendMessage(ChatColor.RED + e.getMessage());
                } catch(Exception e) {
                    e.printStackTrace();
                }

            } else player.sendMessage(ChatColor.RED + "You don't have access to this command.");
        } else logger.log(Level.INFO, "You are not a player!");

        return true;
    }
}
