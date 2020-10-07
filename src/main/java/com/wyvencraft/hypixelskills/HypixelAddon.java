package com.wyvencraft.hypixelskills;

import com.wyvencraft.hypixelskills.attributes.Attribute;
import com.wyvencraft.hypixelskills.commands.AttributesCMD;
import com.wyvencraft.hypixelskills.commands.SkillsTabCompleter;
import com.wyvencraft.hypixelskills.commands.StatsTabCompleter;
import com.wyvencraft.hypixelskills.listeners.DefenseListener;
import com.wyvencraft.hypixelskills.listeners.HungerChangeListener;
import com.wyvencraft.hypixelskills.listeners.StrengthListener;
import com.wyvencraft.hypixelskills.menus.SkillCollectionsMenu;
import com.wyvencraft.hypixelskills.skills.SkillType;
import com.wyvencraft.hypixelskills.skills.Skills;
import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.commands.SkillsCMD;
import com.wyvencraft.wyvencore.menus.Menu;
import com.wyvencraft.wyvencore.skills.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HypixelAddon extends Core {

    // SKILLS
    public Map<Material, Map<SkillType, Double>> xpYield = new EnumMap<>(Material.class);
    public List<Integer> xpToNextLevel = new ArrayList<>();
    // ATTRIBUTES
    public double defaultHpPrSecond = 0.8;
    public double defaultHealth = 100;
    public double maxBaseHealth = -1;
    public double defaultStrength = 0;
    public double maxBaseStrength = -1;
    public double defaultDefense = 0;
    public double maxBaseDefense = -1;
    public double defaultSpeed = 100;
    public double maxBaseSpeed = 600;
    public double defaultCritChance = 20;
    public double maxBaseCritChance = 100;
    public double defaultCritDamage = 50;
    public double maxBaseCritDamage = -1;
    public double defaultIntelligence = 0;
    public double maxBaseIntelligence = -1;
    public double defaultAttackSpeed = 0;
    public double maxBaseAttackSpeed = -1;
    // STATSBAR
    String bar1 = "";
    String bar2 = "";
    String bar3 = "";
    // HUNGER
    boolean disableHunger = false;

    public void onEnable() {
        registerListeners();
        registerCommands();

        new SkillCollectionsMenu(this, inventory, Menu.InventoryType.Collections);


        for (String level : this.getConfig("config").getConfigurationSection("Xp_Yield").getKeys(false)) {
            xpToNextLevel.add(this.getConfig("config").getInt("Xp_Yield." + level));
        }

        FileConfiguration skillsConfig = this.getConfig("skills");
        for (String skillName : skillsConfig.getKeys(false)) {

            SkillType skill = Skills.getSkill(skillName);
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

        FileConfiguration config = getConfig("config");

        ConfigurationSection attributesSection = config.getConfigurationSection("Attributes");
        defaultHpPrSecond = attributesSection.getDouble("default_hpPerSecond");
        for (Attribute attribute : Attribute.values()) {
            switch (attribute) {
                case HEALTH:
                    defaultHealth = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseHealth = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
                case DEFENSE:
                    defaultDefense = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseDefense = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
                case STRENGTH:
                    defaultStrength = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseStrength = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
                case SPEED:
                    defaultSpeed = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseSpeed = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
                case CRIT_CHANCE:
                    defaultCritChance = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseCritChance = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
                case CRIT_DAMAGE:
                    defaultCritDamage = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseCritDamage = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
                case INTELLIGENCE:
                    defaultIntelligence = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseIntelligence = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
                case ATTACK_SPEED:
                    defaultAttackSpeed = attributesSection.getDouble(attribute.name() + ".default");
                    maxBaseAttackSpeed = attributesSection.getDouble(attribute.name() + ".maxBase");
                    break;
            }
        }

        ConfigurationSection statsbarSection = config.getConfigurationSection("Statsbar");
        bar1 = statsbarSection.getString("defaultBar");
        bar2 = statsbarSection.getString("gainXpbar");
        bar3 = statsbarSection.getString("defaultWithSkill");

        disableHunger = config.getBoolean("disableHunger");

        new Skills();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BlockListener(), this);
        pm.registerEvents(new DefenseListener(), this);
        pm.registerEvents(new StrengthListener(), this);
        if (disableHunger) pm.registerEvents(new HungerChangeListener(), this);
    }

    private void registerCommands() {
        this.getCommand("skills").setExecutor(new SkillsCMD());
        this.getCommand("skills").setTabCompleter(new SkillsTabCompleter());
        this.getCommand("attri").setExecutor(new AttributesCMD());
        this.getCommand("attri").setTabCompleter(new StatsTabCompleter());
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
