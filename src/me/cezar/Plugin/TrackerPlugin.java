package me.cezar.Plugin;

import me.cezar.Plugin.Events.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;



import java.util.ArrayList;
import java.util.List;

public class TrackerPlugin extends JavaPlugin {
    private final Commands commands = new Commands();
    private Player speedrunner;
    private Location speedrunner_loc;
    private boolean gameStarted = false;
    private List<Player> hunters = new ArrayList<>();
    private static TrackerPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getCommand(commands.cmdHunter).setExecutor(commands);
        getCommand(commands.cmdSpeedrunner).setExecutor(commands);
        getCommand(commands.cmdRemoveHunter).setExecutor(commands);
        getCommand(commands.cmdRemoveSpeedrunner).setExecutor(commands);
        getCommand(commands.cmdStartGame).setExecutor(commands);
        getCommand(commands.cmdStopGame).setExecutor(commands);
        getCommand(commands.cmdList).setExecutor(commands);
        getCommand(commands.cmdHelp).setExecutor(commands);


        getServer().getPluginManager().registerEvents(new CompassInteract(), this);
        getServer().getPluginManager().registerEvents(new EnderDragonSlain(), this);
        getServer().getPluginManager().registerEvents(new PlayerDies(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "(!) HunterVsSpeedrunner has been enabled! ");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "(!) HunterVsSpeedrunner has been disabled! ");

    }

    public static TrackerPlugin getInstance() {
        return instance;
    }

    public static void giveTrackingCompass(Player hunter) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName("Compass Tracker");
        compass.setItemMeta(meta);
        hunter.getInventory().addItem(compass);
    }


    public boolean stopGame() {
        if (gameStarted) {
            gameStarted = false;
            speedrunner = null;
            speedrunner_loc = null;
            hunters.forEach(hunter -> hunter.getInventory().remove(Material.COMPASS));
            hunters = new ArrayList<>();
            return true;
        }
        return false;
    }

    public boolean startGame() {
        // if the game has not started already and there is at least one player on both teams
        if (!gameStarted && speedrunner != null && hunters.size() > 0) {
            hunters.forEach(hunter -> giveTrackingCompass(hunter));
            TrackerPlugin.getInstance().setSpeedrunner_loc(TrackerPlugin.getInstance().getSpeedrunner().getLocation());
            gameStarted = true;
            return true;
        }
        return false;
    }

    public boolean addHunter(Player player) {
        boolean playerIsSpeedrunner;
        if (speedrunner == null) {
            playerIsSpeedrunner = false;
        } else {
            playerIsSpeedrunner = speedrunner.equals(player);
        }

        if (!hunters.contains(player) && !playerIsSpeedrunner) {
            hunters.add(player);
            return true;
        }
        return false;
    }

    public boolean addSpeedrunner(Player player) {
        if (player != speedrunner && !hunters.contains(player)) {
            speedrunner = player;
            return true;
        }
        return false;
    }

    public boolean removeHunter(Player player) {
        if (hunters.contains(player)) {
            hunters.remove(player);
            return true;
        }
        return false;
    }

    public boolean removeSpeedrunner(Player player) {
        if (speedrunner != null && speedrunner == player) {
            speedrunner = null;
            return true;
        }
        return false;
    }


    public Player getSpeedrunner() {
        return speedrunner;
    }

    public Location getSpeedrunner_loc() {
        return speedrunner_loc;
    }

    public void setSpeedrunner_loc(Location speedrunner_loc) {
        this.speedrunner_loc = speedrunner_loc;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public List<Player> getHunters() {
        return hunters;
    }

}
