package me.cezar.Plugin.Events;

import me.cezar.Plugin.TrackerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EnderDragonSlain implements Listener {

    @EventHandler
    public void onEnderDragonKillEvent(EntityDeathEvent event) {

        // if the game is in progress
        if (TrackerPlugin.getInstance().isGameStarted()) {

            // if ender dragon is killed then the speedrunner wins
            if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
                TrackerPlugin.getInstance().stopGame();
                Bukkit.broadcastMessage(ChatColor.GOLD + "The Speedrunner has won!!!");
                Bukkit.broadcastMessage(ChatColor.WHITE + "Stopping Game");
            }
        }
    }
}
