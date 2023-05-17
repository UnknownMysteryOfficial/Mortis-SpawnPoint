package me.unknownmystery.mortisspawnpoint;

import me.unknownmystery.mortisspawnpoint.MortisSpawnPoint;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final MortisSpawnPoint plugin;

    public SpawnCommand(MortisSpawnPoint plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            Location location = plugin.getConfig().getLocation("spawn");

            if (location != null){
                player.teleport(location);
                player.sendMessage(ChatColor.GREEN + "You were teleported to the spawn!");
            return true;
            }else{
                player.sendMessage(ChatColor.RED + "No spawnpoint found to teleport! Use /setspawn to set the spawnpoint.");
            }
        }
        return true;
    }
}
