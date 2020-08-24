package me.cezar.Plugin.Events;

import me.cezar.Plugin.TrackerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CompassInteract implements Listener {

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        // if the game is started and the speedrunner is not null
        // the speedrunner null is taken care of by the startGame method
        if (TrackerPlugin.getInstance().isGameStarted()) {

            // if the player right-clicked while holding the tracking compass
            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                if (item != null && item.getType() == Material.COMPASS) {
                    if (item.getItemMeta().getDisplayName().equals("Compass Tracker") && TrackerPlugin.getInstance().isGameStarted()) {


                        // if the speedrunner is not in the overworld:
                        if (!TrackerPlugin.getInstance().getSpeedrunner_loc().getWorld().getName().equals("world")) {

                            // if the tracker IS in the overworld:
                            if (player.getLocation().getWorld().getName().equals("world")) {
                                Bukkit.broadcastMessage(ChatColor.YELLOW + "The Tracker is pointing to the last known location of the speedrunner");

                            } else {    // The tracker is also not in the overworld
                                player.sendMessage(ChatColor.RED + "Tracker only works in the overworld");

                            }
                            // the tracker is not in the overworld but the speedrunner still is
                        } else if (!player.getLocation().getWorld().getName().equals("world")) {
                            player.sendMessage(ChatColor.RED + "Tracker only works in the overworld");

                        } else {    // the tracker and the speedrunner are both in the overworld

                            TrackerPlugin.getInstance().setSpeedrunner_loc(TrackerPlugin.getInstance().getSpeedrunner().getLocation());
                            player.setCompassTarget(TrackerPlugin.getInstance().getSpeedrunner_loc());
                            Bukkit.broadcastMessage(ChatColor.GREEN + "The compass is pointing at " + TrackerPlugin.getInstance().getSpeedrunner().getName());

                        }
                    } else {    // the speedrunner was not set yet
                        player.sendMessage(ChatColor.RED + "You haven't set a speedrunner! Use /set_speedrunner <player>");
                    }

                }
            }
        }

    }
}
