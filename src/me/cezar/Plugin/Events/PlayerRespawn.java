package me.cezar.Plugin.Events;

import me.cezar.Plugin.TrackerPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // if the game has started
        if (TrackerPlugin.getInstance().isGameStarted()) {

            // if the game is started and a hunter died give him/her back his compass
            if (TrackerPlugin.getInstance().getHunters().contains(player)) {
                TrackerPlugin.giveTrackingCompass(player);

            }

        }
    }
}
