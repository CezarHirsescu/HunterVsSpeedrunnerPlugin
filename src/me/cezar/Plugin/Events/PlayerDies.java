package me.cezar.Plugin.Events;

import me.cezar.Plugin.TrackerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDies implements Listener {

    @EventHandler
    public void onPlayerDieEvent(PlayerDeathEvent event) {

        // if the game has started
        if (TrackerPlugin.getInstance().isGameStarted()) {

            // if the speedrunner has died end the game and the hunters have won
            Player player = event.getEntity();
            if (player == TrackerPlugin.getInstance().getSpeedrunner()) {
                TrackerPlugin.getInstance().stopGame();
                Bukkit.broadcastMessage(ChatColor.GOLD + "The Hunters Have Won!!!!!!");
                Bukkit.broadcastMessage(ChatColor.WHITE + "Stopping Game");
            }
        }
    }
}
