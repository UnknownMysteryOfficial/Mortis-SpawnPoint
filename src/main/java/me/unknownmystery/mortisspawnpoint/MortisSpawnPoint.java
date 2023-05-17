package me.unknownmystery.mortisspawnpoint;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public final class MortisSpawnPoint extends JavaPlugin implements Listener {

    private String joinMsg;
    private String quitMsg;

    private int taskID;

    private List<String> messages;

    private int interval;

    private int messagesPerInterval;

    private BukkitTask messsageTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("msreload").setExecutor(this);
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        loadConfig();
        startMessageTask();
        getLogger().info("MortisSpawnpoint have successfully loaded. You are running v1.0, thank you for downloading our plugin!");
        joinMsg = getConfig().getString("join-message");
        quitMsg = getConfig().getString("leave-message");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new TeleportOnJoin(this), this);

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + ", " + quitMsg));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + ", " + joinMsg));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stopMessagesTask();
        getLogger().info("MortisSpawnPoint have successfully disabled. You were running v1.0, thank you for using our plugin!");
    }

    private void loadConfig(){
        reloadConfig();
        FileConfiguration config = getConfig();
        messages = getConfig().getStringList("messages");
        interval = config.getInt("interval");
        messagesPerInterval = config.getInt("message-per-interval");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("msreload")){
            if (sender.hasPermission("mortisspawnpoint.reload")){
                reloadConfig();
                loadConfig();
                cancelMessageTask();
                sheduleMessageTask();
                sender.sendMessage(ChatColor.GREEN + "MortisSpawnPoint have successfully been reloaded!");
                return true;
            }else{
                sender.sendMessage(ChatColor.RED + "You don't have the permission to access this command!");
            }
        }
        return false;
    }

    public void sheduleMessageTask(){
        messsageTask = new BukkitRunnable(){
            private int messageIndex = 0;

            @Override
            public void run(){
                if (!messages.isEmpty()){
                    for (int i = 0; i < messagesPerInterval; i++){
                        if (messageIndex >= messages.size()){
                            messageIndex = 0;
                        }
                        String message = messages.get(messageIndex);
                        messageIndex++;
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                }
            }
        }.runTaskTimer(this, interval * 20L, interval * 20L);
    }

    public void cancelMessageTask(){
        if (messsageTask !=null){
            messsageTask.cancel();
            messsageTask = null;
        }
    }

    public int getInterval(){
        return interval;
    }

    public int getMessagesPerInterval(){
        return messagesPerInterval;
    }

    public List<String> getMessages(){
        return messages;
    }

    private void startMessageTask(){
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () ->{
            if (!messages.isEmpty()){
                int messagesToSend = Math.min(messagesPerInterval, messages.size());
                List<String> messagesToSendCopy = new ArrayList<>(messages.subList(0, messagesToSend));

                for (String message : messagesToSendCopy) {
                    String coloredMessages = ChatColor.translateAlternateColorCodes('&', message);
                    Bukkit.broadcastMessage(coloredMessages);
                }
                messages.addAll(messagesToSendCopy);
                messages.subList(0, messagesToSend).clear();
            }
        }, interval * 20L, interval * 20L);
    }

    private void stopMessagesTask(){
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
