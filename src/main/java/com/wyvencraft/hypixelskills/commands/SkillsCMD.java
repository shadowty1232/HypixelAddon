package com.wyvencraft.hypixelskills.commands;

import com.wyvencraft.hypixelskills.SkillType;
import com.wyvencraft.hypixelskills.Skills;
import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.commands.Permission;
import com.wyvencraft.wyvencore.common.Lang;
import com.wyvencraft.wyvencore.configuration.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillsCMD implements CommandExecutor {

    Core plugin = Core.instance;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission(Permission.SKILLS_HELP.getPerm())) {
                for (String str : plugin.getConfig("lang").getStringList("SKILLS.HELP")) {
                    sender.sendMessage(Lang.color(str));
                }
            } else {
                sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
            }
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                if (sender.hasPermission(Permission.SKILLS_HELP.getPerm())) {
                    for (String str : plugin.getConfig("lang").getStringList("SKILLS.HELP")) {
                        sender.sendMessage(Lang.color(str));
                    }
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            case "set":
                if (sender.hasPermission(Permission.SKILLS_SETLEVEL.getPerm())) {
                    int setLevel;
                    Player setTarget;
                    SkillType skill;
                    if (args.length == 3) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        setLevel = getLevel(args[2]);
                        if (sender instanceof Player)
                            setTarget = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <skill>");
                            return true;
                        }

                    } else if (args.length == 4) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        setTarget = getTarget(args[2]);
                        setLevel = getLevel(args[3]);
                    } else {
                        sender.sendMessage("Usage: /skills set <skill> [player] <level>");
                        return true;
                    }

                    Skills.instance.setLevels(plugin.getStatsManager().getPlayerStats(setTarget.getUniqueId()), skill, setLevel);
                    sender.sendMessage(Message.SKILL_LEVEL_SET.getChatMessage()
                            .replace("{player}", setTarget.getName())
                            .replace("{amount}", String.valueOf(setLevel))
                            .replace("{skill}", skill.name().toLowerCase()));
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            case "add":
                if (sender.hasPermission(Permission.SKILLS_ADDLEVEL.getPerm())) {

                    int addLevel;
                    Player addTarget;
                    SkillType skill;
                    if (args.length == 3) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        addLevel = getLevel(args[2]);
                        if (sender instanceof Player)
                            addTarget = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <skill>");
                            return true;
                        }

                    } else if (args.length == 4) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        addTarget = getTarget(args[2]);
                        addLevel = getLevel(args[3]);
                    } else {
                        sender.sendMessage("Usage: /skills add <skill> [player] <level>");
                        return true;
                    }

                    Skills.instance.addLevels(plugin.getStatsManager().getPlayerStats(addTarget.getUniqueId()), skill, addLevel);
                    sender.sendMessage(Message.SKILL_LEVEL_ADDED.getChatMessage()
                            .replace("{player}", addTarget.getName())
                            .replace("{amount}", String.valueOf(addLevel))
                            .replace("{skill}", skill.name().toLowerCase()));
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            case "take":
                if (sender.hasPermission(Permission.SKILLS_TAKELEVEL.getPerm())) {

                    int takeLevel;
                    Player takeTarget;
                    SkillType skill;
                    if (args.length == 3) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        takeLevel = getLevel(args[2]);
                        if (sender instanceof Player)
                            takeTarget = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <skill>");
                            return true;
                        }

                    } else if (args.length == 4) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        takeTarget = getTarget(args[2]);
                        takeLevel = getLevel(args[3]);
                    } else {
                        sender.sendMessage("Usage: /skills take <skill> [player] <level>");
                        return true;
                    }

                    Skills.instance.takeLevels(plugin.getStatsManager().getPlayerStats(takeTarget.getUniqueId()), skill, takeLevel);
                    sender.sendMessage(Message.SKILL_LEVEL_TAKE.getChatMessage()
                            .replace("{player}", takeTarget.getName())
                            .replace("{amount}", String.valueOf(takeLevel))
                            .replace("{skill}", skill.name().toLowerCase()));
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            // /skills levelup <skill> [player] [amount]
            case "levelup":
                if (sender.hasPermission(Permission.SKILLS_LEVELUP.getPerm())) {

                    int levelupLevel = 1;
                    Player target;
                    SkillType skill;
                    if (args.length == 2) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (sender instanceof Player)
                            target = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <skill>");
                            return true;
                        }

                    } else if (args.length == 3) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        target = getTarget(args[2]);
                    } else if (args.length == 4) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        target = getTarget(args[2]);
                        levelupLevel = getLevel(args[3]);
                    } else {
                        sender.sendMessage("Usage: /skills levelup <skill> [player] [amount]");
                        return true;
                    }

                    Skills.instance.levelUp(plugin.getStatsManager().getPlayerStats(target.getUniqueId()), skill, levelupLevel);
                } else {
                    sender.sendMessage(Message.NO_PERMISSION.getChatMessage());
                }
                break;
            // skills givexp <skill> [player] <amount>
            case "givexp":
                if (sender.hasPermission(Permission.SKILLS_GIVEXP.getPerm())) {
                    double xp = 0;
                    Player xpTarget;
                    SkillType skill = null;
                    if (args.length == 2) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        if (sender instanceof Player)
                            xpTarget = (Player) sender;
                        else {
                            sender.sendMessage("Please specify player after <skill>");
                            return true;
                        }

                    } else if (args.length == 3) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        xpTarget = getTarget(args[2]);
                    } else if (args.length == 4) {
                        if (Skills.getSkill(args[1]) == null) {
                            sender.sendMessage(Message.INVALID_SKILL.getChatMessage());
                            return true;
                        }

                        skill = Skills.getSkill(args[1]);

                        if (getTarget(args[2]) == null) {
                            sender.sendMessage(Message.INVALID_PLAYER.getChatMessage());
                            return true;
                        }

                        xpTarget = getTarget(args[2]);
                        xp = getXp(args[3]);
                    } else {
                        sender.sendMessage("Usage: /skills givexp <skill> [player] <amount>");
                        return true;
                    }

                    if (xp < 1) {
                        sender.sendMessage(Lang.color("&cXp has too be greater than zero"));
                        return true;
                    }

                    plugin.getStatsManager().getPlayerStats(xpTarget.getUniqueId()).getStatsBar().gainXp(skill, xp);
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

    private double getXp(String arg) {
        double xp;
        try {
            xp = Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            return 0;
        }
        return xp;
    }
}
