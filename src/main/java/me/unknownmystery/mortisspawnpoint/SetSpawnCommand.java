package me.unknownmystery.mortisspawnpoint;

import me.unknownmystery.mortisspawnpoint.MortisSpawnPoint;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final MortisSpawnPoint plugin;

    public SetSpawnCommand(MortisSpawnPoint plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            Location location = player.getLocation();
            if (player.hasPermission("mortisspawnpoint.setspawn")){
                plugin.reloadConfig();
                plugin.getConfig().set("spawn", location);
                plugin.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Spawn was successfully set to your location!");

            return true;
            }else{
                player.sendMessage(ChatColor.RED + "You don't have the permission to run this command");
            }

        }
        return false;
    }
}
