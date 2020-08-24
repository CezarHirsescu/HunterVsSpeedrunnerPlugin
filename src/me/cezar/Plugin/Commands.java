package me.cezar.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class Commands implements Listener, CommandExecutor {
    public String cmdHunter = "set_hunter";
    public String cmdSpeedrunner = "set_speedrunner";
    public String cmdRemoveHunter = "remove_hunter";
    public String cmdRemoveSpeedrunner = "remove_speedrunner";
    public String cmdStartGame = "start_game";
    public String cmdStopGame = "stop_game";
    public String cmdList = "list_players";
    public String cmdHelp = "help";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // all player argument commands
        if (cmd.getName().equalsIgnoreCase(cmdHunter)
            || cmd.getName().equalsIgnoreCase(cmdSpeedrunner)
            || cmd.getName().equalsIgnoreCase(cmdRemoveHunter)
            || cmd.getName().equalsIgnoreCase(cmdRemoveSpeedrunner)) {

            // check for player arguments
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "this commands takes at least one player argument" );
                return false;
            }

            // add/remove speedrunner commands. These commands must be sepreated from the hunter
            // commands because they only take one player argument
            if (cmd.getName().equalsIgnoreCase(cmdSpeedrunner) || cmd.getName().equalsIgnoreCase(cmdRemoveSpeedrunner)) {
                if (args.length != 1) {
                    sender.sendMessage(ChatColor.RED + "this command does not take more than one player argument");
                    return false;
                }

                // check if the player is offline
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
                    return false;
                }

                // the command has been given proper arguments and the player is online. Time for methods!

                // set speedrunner
                if (cmd.getName().equalsIgnoreCase(cmdSpeedrunner)) {
                    if (TrackerPlugin.getInstance().addSpeedrunner(target)) {
                        sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " is now the speedrunner\n" +
                                "The previous speedrunner is not on a team anymore");
                        target.sendMessage(ChatColor.AQUA + "You are now a speedrunner!");
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.RED + " is either a speedrunner or a hunter\n" +
                                "Check who is on both teams using the /" + cmdList + " command");
                        return false;
                    }

                // remove speedrunner
                } else {
                    if (TrackerPlugin.getInstance().removeSpeedrunner(target)) {
                        sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " has been removed from speedrunner");
                        target.sendMessage(ChatColor.AQUA + "You have been removed from speedrunner!");

                    } else {
                        sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.RED + " is not a speedrunner\n" +
                                "Check who is on both teams using the /" + cmdList + " command");
                        return false;
                    }
                }

                // if this code is reached, the command has worked successfully

                // add/remove hunter commands
            } else {

                // for each player in the argument
                for (String playerName : args) {
                    Player target = Bukkit.getServer().getPlayer(playerName);

                    // check if the player is online
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + playerName + " is not online!");
                        return false;
                    }

                    // the command is given proper arguments and the player(s) is online. Time for methods!

                    // add hunter
                    if (cmd.getName().equalsIgnoreCase(cmdHunter)) {
                        if (TrackerPlugin.getInstance().addHunter(target)) {
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " is now a hunter");
                            target.sendMessage(ChatColor.AQUA + "You are now a hunter!");

                        } else {
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.RED + " is already a hunter or speedrunner\n" +
                                    "Check who is on both teams using the /" + cmdList + " command");
                            return false;
                        }

                    // remove hunter
                    } else {
                        if (TrackerPlugin.getInstance().removeHunter(target)) {
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " has been removed from hunter");
                            target.sendMessage(ChatColor.AQUA + "You have been removed from hunter!");

                        } else {
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.RED + " is not a hunter\n" +
                                    "Check who is on both teams using the /" + cmdList + " command");
                        }

                    }

                }

                // if this code is reached, the command worked with no errors
            }
            return true;


            // all commands which don't take players
        } else if (cmd.getName().equalsIgnoreCase(cmdStartGame)
                || cmd.getName().equalsIgnoreCase(cmdStopGame)
                || cmd.getName().equalsIgnoreCase(cmdList)
                || cmd.getName().equalsIgnoreCase(cmdHelp)
                || cmd.getName().equalsIgnoreCase("")) {

            // check for player arguments (there should be none)
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "this command does not take arguments");
                return false;
            }

            // Time for methods!

            // start game
            if (cmd.getName().equalsIgnoreCase(cmdStartGame)) {
                if (TrackerPlugin.getInstance().startGame()) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The Game has started");
                } else {
                    sender.sendMessage(ChatColor.RED + "Could not start game. Game has started or there is not a speedrunner and a hunter");
                    return false;
                }

            // stop game
            } else if (cmd.getName().equalsIgnoreCase(cmdStopGame)) {
                if (TrackerPlugin.getInstance().stopGame()) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The Game has been manually stopped");
                } else {
                    sender.sendMessage(ChatColor.RED + "Could not stop. Game has not started");
                    return false;
                }

            //list players
            } else if (cmd.getName().equalsIgnoreCase(cmdList)) {
                sender.sendMessage(ChatColor.GOLD + "------------------- Players -----------------");
                if (TrackerPlugin.getInstance().getSpeedrunner() == null) {
                    sender.sendMessage(ChatColor.RED + "No Speedrunner");
                } else {
                    sender.sendMessage(ChatColor.AQUA + "Speedrunner: " + TrackerPlugin.getInstance().getSpeedrunner().getName());
                }
                sender.sendMessage(ChatColor.YELLOW + "Hunters: ");
                TrackerPlugin.getInstance().getHunters().forEach(hunter -> sender.sendMessage(ChatColor.YELLOW + hunter.getName()));
                sender.sendMessage(ChatColor.GOLD + "---------------------------------------------");

            // help
            } else {
                sender.sendMessage(ChatColor.GOLD + "----------------- Hunter Vs Speedrunner ----------------");
                sender.sendMessage(ChatColor.YELLOW +
                        "/set_hunter <player> - set player as a hunter\n" +
                        "/set_speedrunner <player> - set or replace the speedrunner (only one)\n" +
                        "/remove_hunter <player> - remove that player from hunter\n" +
                        "/remove_speedrunner <player> - remove that player as speedrunner\n" +
                        "/start_game - starts the game once teams are set\n" +
                        "/stop_game - stops the game\n" +
                        "/list_players - list all the speedrunners and all the hunters\n" +
                        "/help - list all the commands");
                sender.sendMessage(ChatColor.GOLD + "--------------------------------------------------------");
            }

            // if this code is reached, the command worked with no errors
            return true;

        }

        // if the command entered does not correspond with any of the proper commands
        return false;
    }
}
