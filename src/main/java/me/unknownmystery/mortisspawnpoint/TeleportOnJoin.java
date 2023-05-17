package me.unknownmystery.mortisspawnpoint;

import me.unknownmystery.mortisspawnpoint.MortisSpawnPoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeleportOnJoin implements Listener {
    private final MortisSpawnPoint plugin;

    public TeleportOnJoin(MortisSpawnPoint plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        boolean teleportOnJoin = plugin.getConfig().getBoolean("teleport-on-join", false);
        if (teleportOnJoin) {
            Location location = plugin.getConfig().getLocation("spawn");

            if (location != null) {
                player.teleport(location);
            }
        }
    }
}
