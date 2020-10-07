package com.wyvencraft.hypixelskills.commands;

import com.wyvencraft.wyvencore.commands.Permission;
import com.wyvencraft.wyvencore.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SkillsTabCompleter implements TabCompleter {
    List<String> arguments = new ArrayList<>();
    List<String> availableSkills = new ArrayList<>();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("help");
            arguments.add("set");
            arguments.add("add");
            arguments.add("take");
            arguments.add("givexp");
            arguments.add("levelup");
        }

        if (availableSkills.isEmpty()) {
            for (int i = 0; i < SkillType.values().length; i++) {
                availableSkills.add(SkillType.values()[i].name());
            }
        }

        if (args.length == 1) {
            if (sender.hasPermission(Permission.SKILLS_HELP.getPerm())) {
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
                if (sender.hasPermission(Permission.SKILLS_ADDLEVEL.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableSkills) {
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
                        result.add("50");
                        return result;
                    }
                }
                break;
            case "set":
                if (sender.hasPermission(Permission.SKILLS_SETLEVEL.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableSkills) {
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
                        result.add("0");
                        result.add("1");
                        result.add("10");
                        result.add("20");
                        result.add("40");
                        result.add("50");
                        return result;
                    }
                }
                break;
            case "take":
                if (sender.hasPermission(Permission.SKILLS_TAKELEVEL.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableSkills) {
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
                        result.add("50");
                        return result;
                    }
                }
                break;
            case "givexp":
                if (sender.hasPermission(Permission.SKILLS_GIVEXP.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableSkills) {
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
                }
                break;
            case "levelup":
                if (sender.hasPermission(Permission.SKILLS_LEVELUP.getPerm())) {
                    if (args.length == 2) {
                        List<String> result = new ArrayList<>();
                        for (String a : availableSkills) {
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
                }
                break;
        }

        return null;
    }
}
