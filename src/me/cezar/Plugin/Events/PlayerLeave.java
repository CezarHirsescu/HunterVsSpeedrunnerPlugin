package me.cezar.Plugin.Events;

import me.cezar.Plugin.TrackerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // removing player from teams if they disconnect
        if (TrackerPlugin.getInstance().getHunters().contains(player)) {
            TrackerPlugin.getInstance().getHunters().remove(player);
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has been removed from hunter");
        } else if(TrackerPlugin.getInstance().getSpeedrunner() != null
                && TrackerPlugin.getInstance().getSpeedrunner().equals(player)) {
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has been removed from speedrunner");
        }

        // if the game has started
        if (TrackerPlugin.getInstance().isGameStarted()) {

            // if the speedrunner has left the server stop the game
            if (TrackerPlugin.getInstance().getSpeedrunner() == player
                || TrackerPlugin.getInstance().getHunters().size() == 0) {
                TrackerPlugin.getInstance().stopGame();
                Bukkit.broadcastMessage(ChatColor.RED + "The Speedrunner has left the server, stopping game!");
            }
        }
    }
}
