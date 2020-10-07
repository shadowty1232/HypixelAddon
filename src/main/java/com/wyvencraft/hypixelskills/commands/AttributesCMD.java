package com.wyvencraft.hypixelskills.commands;

import com.wyvencraft.hypixelskills.HypixelAddon;
import com.wyvencraft.hypixelskills.attributes.Attribute;
import com.wyvencraft.hypixelskills.attributes.AttributesHandler;
import com.wyvencraft.wyvencore.commands.Permission;
import com.wyvencraft.wyvencore.common.Lang;
import com.wyvencraft.wyvencore.configuration.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AttributesCMD implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission(Permission.STATS_HELP.getPerm())) {
                for (String str : HypixelAddon.instance.getConfig("lang").getStringList("STATS.HELP")) {
                    sender.sendMessage(Lang.color(str));
                }
            } else {
                sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
            }
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                if (sender.hasPermission(Permission.STATS_HELP.getPerm())) {
                    for (String str : HypixelAddon.instance.getConfig("lang").getStringList("STATS.HELP")) {
                        sender.sendMessage(Lang.color(str));
                    }
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            case "set":
                if (sender.hasPermission(Permission.STATS_SETLEVEL.getPerm())) {
                    int setLevel;
                    Player setTarget;
                    Attribute setAttribute;
                    if (args.length == 3) {
                        if (AttributesHandler.getAttribute(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_ATTRIBUTE.getChatMessage());
                            return true;
                        }

                        setAttribute = AttributesHandler.getAttribute(args[1]);

                        setLevel = getLevel(args[2]);
                        if (sender instanceof Player)
                            setTarget = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <attribute>");
                            return true;
                        }

                    } else if (args.length == 4) {
                        if (AttributesHandler.getAttribute(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_ATTRIBUTE.getChatMessage());
                            return true;
                        }

                        setAttribute = AttributesHandler.getAttribute(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        setTarget = getTarget(args[2]);
                        setLevel = getLevel(args[3]);
                    } else {
                        sender.sendMessage("Usage: /attributes set <attribute> [player] <level>");
                        return true;
                    }

                    AttributesHandler.instance.set(HypixelAddon.instance.getStatsManager().getPlayerStats(setTarget.getUniqueId()), setAttribute, setLevel, false);
                    sender.sendMessage(Message.ATTRIBUTE_LEVEL_SET.getChatMessage()
                            .replace("{player}", setTarget.getName())
                            .replace("{amount}", String.valueOf(setLevel))
                            .replace("{attribute}", setAttribute.name().toLowerCase()));
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            case "add":
                if (sender.hasPermission(Permission.STATS_ADDLEVEL.getPerm())) {
                    int addLevel;
                    Player addTarget;
                    Attribute addAttribute;
                    if (args.length == 3) {
                        if (AttributesHandler.instance.getAttribute(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        addAttribute = AttributesHandler.instance.getAttribute(args[1]);

                        addLevel = getLevel(args[2]);
                        if (sender instanceof Player)
                            addTarget = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <attribute>");
                            return true;
                        }

                    } else if (args.length == 4) {
                        if (AttributesHandler.instance.getAttribute(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_ATTRIBUTE.getChatMessage());
                            return true;
                        }

                        addAttribute = AttributesHandler.instance.getAttribute(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        addTarget = getTarget(args[2]);
                        addLevel = getLevel(args[3]);
                    } else {
                        sender.sendMessage("Usage: /attributes set <attribute> [player] <level>");
                        return true;
                    }

                    AttributesHandler.instance.add(plugin.getStatsManager().getPlayerStats(addTarget.getUniqueId()), addAttribute, addLevel, false);
                    sender.sendMessage(Message.ATTRIBUTE_LEVEL_ADDED.getChatMessage()
                            .replace("{player}", addTarget.getName())
                            .replace("{amount}", String.valueOf(addLevel))
                            .replace("{attribute}", addAttribute.name().toLowerCase()));
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            case "take":
                if (sender.hasPermission(Permission.STATS_TAKELEVEL.getPerm())) {
                    int takeLevel;
                    Player takeTarget;
                    Attribute takeAttribute;
                    if (args.length == 3) {
                        if (AttributesHandler.instance.getAttribute(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        takeAttribute = AttributesHandler.instance.getAttribute(args[1]);

                        takeLevel = getLevel(args[2]);
                        if (sender instanceof Player)
                            takeTarget = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <attribute>");
                            return true;
                        }

                    } else if (args.length == 4) {
                        if (AttributesHandler.instance.getAttribute(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        takeAttribute = AttributesHandler.instance.getAttribute(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        takeTarget = getTarget(args[2]);
                        takeLevel = getLevel(args[3]);
                    } else {
                        sender.sendMessage("Usage: /attributes set <attribute> [player] <level>");
                        return true;
                    }

                    AttributesHandler.instance.take(plugin.getStatsManager().getPlayerStats(takeTarget.getUniqueId()), takeAttribute, takeLevel, false);
                    sender.sendMessage(Message.ATTRIBUTE_LEVEL_TAKE.getChatMessage()
                            .replace("{player}", takeTarget.getName())
                            .replace("{amount}", String.valueOf(takeLevel))
                            .replace("{attribute}", takeAttribute.name().toLowerCase()));
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
        }

        return true;
    }

    private Player getTarget(String arg) {
        return Bukkit.getPlayer(arg);
    }

    private int getLevel(String arg) {
        int level;
        try {
            level = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return 0;
        }
        return level;
    }
}