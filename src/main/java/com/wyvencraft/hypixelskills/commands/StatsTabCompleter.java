package com.wyvencraft.hypixelskills.commands;

import com.wyvencraft.wyvencore.attributes.Attribute;
import com.wyvencraft.wyvencore.commands.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StatsTabCompleter implements TabCompleter {
    List<String> arguments = new ArrayList<>();
    List<String> availableStats = new ArrayList<>();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("help");
            arguments.add("set");
            arguments.add("add");
            arguments.add("take");
        }

        if (availableStats.isEmpty()) {
            for (int i = 0; i < Attribute.values().length; i++) {
                availableStats.add(Attribute.values()[i].name().toLowerCase());
            }
        }

        if (args.length == 1) {
            if (sender.hasPermission(Permission.STATS_HELP.getPerm())) {
                List<String> result = new ArrayList<>();
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(a);
                    }
                }
                return result;
            }
        }

        switch (args[0].toLowerCase()) {
            // /stats add(0) <attr>(1) [player](2) [amount](3)
            case "add":
                if (sender.hasPermission(Permission.STATS_ADDLEVEL.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableStats) {
                            if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                                result.add(a);
                            }
                        }
                        return result;
                    }

                    if (args.length == 3) {
                        List<String> result = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                                result.add(player.getName());
                            }
                        }
                        return result;
                    }
                    if (args.length == 4) {
                        List<String> result = new ArrayList<>();
                        result.add("1");
                        result.add("10");
                        result.add("20");
                        result.add("40");
                        result.add("80");
                        return result;
                    }
                }
                break;
            case "set":
                if (sender.hasPermission(Permission.STATS_SETLEVEL.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableStats) {
                            if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                                result.add(a);
                            }
                        }
                        return result;
                    }

                    if (args.length == 3) {
                        List<String> result = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                                result.add(player.getName());
                            }
                        }
                        return result;
                    }
                    if (args.length == 4) {
                        List<String> result = new ArrayList<>();
                        if (args[1].equalsIgnoreCase("speed")) {
                            result.add("100");
                            result.add("150");
                            result.add("200");
                            result.add("350");
                            result.add("600");
                        } else {
                            result.add("1");
                            result.add("10");
                            result.add("20");
                            result.add("40");
                            result.add("80");
                        }
                        return result;
                    }
                }
                break;
            case "take":
                if (sender.hasPermission(Permission.STATS_TAKELEVEL.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableStats) {
                            if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                                result.add(a);
                            }
                        }
                        return result;
                    }

                    if (args.length == 3) {
                        List<String> result = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                                result.add(player.getName());
                            }
                        }
                        return result;
                    }
                    if (args.length == 4) {
                        List<String> result = new ArrayList<>();
                        result.add("1");
                        result.add("10");
                        result.add("20");
                        result.add("40");
                        result.add("80");
                        return result;
                    }
                }
                break;

        }

        return null;
    }
}
