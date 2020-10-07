package com.wyvencraft.hypixelskills;

import com.wyvencraft.hypixelskills.commands.SkillsTabCompleter;
import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.commands.SkillsCMD;
import com.wyvencraft.wyvencore.player.PlayerStats;
import com.wyvencraft.wyvencore.skills.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HypixelSkills extends Core {

    // SKILLS
    public Map<Material, Map<SkillType, Double>> xpYield = new EnumMap<>(Material.class);
    public List<Integer> xpToNextLevel = new ArrayList<>();

    public void onEnable() {
        registerListeners();
        registerCommands();

        for (String level : this.getConfig("config").getConfigurationSection("Xp_Yield").getKeys(false)) {
            xpToNextLevel.add(this.getConfig("config").getInt("Xp_Yield." + level));
        }

        FileConfiguration skillsConfig = this.getConfig("skills");
        for (String skillName : skillsConfig.getKeys(false)) {

            SkillType skill = Skills.instance.getSkill(skillName);
            if (skill == null) {
                getLogger().severe("no skill named: " + skillName + ", please fix inside config.yml");
                continue;
            }

            if (skill == SkillType.COMBAT) continue;

            ConfigurationSection materialsSections = skillsConfig.getConfigurationSection(skillName + ".xpyield");

            for (String material : materialsSections.getKeys(false)) {
                Material mat = Material.getMaterial(material);
                if (mat == null) {
                    getLogger().severe(material + " under skills-section in config.yml is not a valid material");
                    continue;
                }
                Map<SkillType, Double> matSkillXp = new EnumMap<SkillType, Double>(SkillType.class) {{
                    put(skill, materialsSections.getDouble(material));
                }};

                xpYield.put(mat, matSkillXp);
            }
        }

        new Skills();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }

    private void registerCommands() {

        this.getCommand("skills").setExecutor(new SkillsCMD());
        this.getCommand("skills").setTabCompleter(new SkillsTabCompleter());
    }

    // CMD ACTIONS

//    case "[combat-xp":
//    double combatxp = Methods.getDouble(value);
//
//                if (combatxp < 0) {
//        plugin.getLogger().severe(value + " has too be greater than 0. Make sure it is also a number LOL");
//        return;
//    }
//
//                Skills.instance.gainXp(ps, SkillType.COMBAT, combatxp);
//                break;
//            case "[farming-xp":
//    double farmingxp = Methods.getDouble(value);
//
//                if (farmingxp < 0) {
//        plugin.getLogger().severe(value + " has too be greater than 0. Make sure it is also a number LOL");
//        return;
//    }
//
//                Skills.instance.gainXp(ps, SkillType.FARMING, farmingxp);
//                break;
//            case "[foraging-xp":
//    double foragingxp = Methods.getDouble(value);
//
//                if (foragingxp < 0) {
//        plugin.getLogger().severe(value + " has too be greater than 0. Make sure it is also a number LOL");
//        return;
//    }
//
//                Skills.instance.gainXp(ps, SkillType.FORAGING, foragingxp);
//                break;
//            case "[mining-xp":
//    double miningxp = Methods.getDouble(value);
//
//                if (miningxp < 0) {
//        plugin.getLogger().severe(value + " has too be greater than 0. Make sure it is also a number LOL");
//        return;
//    }
//
//                Skills.instance.gainXp(ps, SkillType.MINING, miningxp);
//                break;
//            case "[alchemy-xp":
//    double alchemyxp = Methods.getDouble(value);
//
//                if (alchemyxp < 0) {
//        plugin.getLogger().severe(value + " has too be greater than 0. Make sure it is also a number LOL");
//        return;
//    }
//
//                Skills.instance.gainXp(ps, SkillType.ALCHEMY, alchemyxp);
//                break;
//            case "[enchanting-xp":
//    double enchantingxp = Methods.getDouble(value);
//
//                if (enchantingxp < 0) {
//        plugin.getLogger().severe(value + " has too be greater than 0. Make sure it is also a number LOL");
//        return;
//    }
//
//                Skills.instance.gainXp(ps, SkillType.ENCHANTING, enchantingxp);
//                break;
//            case "[fishing-xp":
//    double fishingxp = Methods.getDouble(value);
//
//                if (fishingxp < 0) {
//        plugin.getLogger().severe(value + " has too be greater than 0. Make sure it is also a number LOL");
//        return;
//    }
//
//                Skills.instance.gainXp(ps, SkillType.FISHING, fishingxp);
//break;

    // TODO: ADD PLACEHOLDERS
//    if(identifier.startsWith("skill_")&&identifier.endsWith("_level")){ // Returns players specific skill level
//        String skillName = identifier.substring(6, identifier.length() - 6);
//
//        SkillType skill = Skills.instance.getSkill(skillName);
//
//        if (skill == null) {
//            plugin.getLogger().severe("no valid skill named " + skillName);
//            return "invalid skill";
//        }
//
//        PlayerStats ps = plugin.getStatsManager().getPlayerStats(player.getUniqueId());
//
//        return String.valueOf(Skills.instance.getSkillLevel(ps, skill));
//    }

}
