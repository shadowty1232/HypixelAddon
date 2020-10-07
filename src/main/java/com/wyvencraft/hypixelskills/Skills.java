package com.wyvencraft.hypixelskills;


import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.common.Lang;
import com.wyvencraft.wyvencore.configuration.Message;
import com.wyvencraft.wyvencore.enchantments.Enchant;
import com.wyvencraft.wyvencore.player.PlayerStats;
import com.wyvencraft.wyvencore.utils.Methods;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

import java.io.Serializable;
import java.util.*;

public class Skills implements Serializable {

    public transient static Skills instance;

    public Skills() {
        instance = this;
    }

    public final static List<Skill> Skills = new ArrayList<>();

    public void load() {
        for (String skillName : HypixelSkills.instance.getConfig("skills").getKeys(false)) {
            ConfigurationSection skillSection = HypixelSkills.instance.getConfig("skills").getConfigurationSection(skillName);

            SkillType skill = getSkill(skillName);
            if (skill == null) {
                HypixelSkills.instance.getLogger().severe("no skill named: " + skillName + ", please fix inside config.yml");
                continue;
            }

            String skillAbility = "";
            if (skillSection.get("skillability") != null) skillAbility = skillSection.getString("skillability");

            List<SkillReward> cumulativeRewards = new ArrayList<>();
            if (skillSection.getConfigurationSection("cumulative") != null) {
                for (String cumulative : skillSection.getConfigurationSection("cumulative").getKeys(false)) {

                    String displayname = skillSection.getString("cumulative." + cumulative + ".name");
                    if (skillSection.get("cumulative." + cumulative + ".execute") == null) {
                        HypixelSkills.instance.getLogger().warning("Missing list of executes: " + skillName + " -> " + cumulative + ", skipping");
                        continue;
                    }
                    List<String> executes = skillSection.getStringList("cumulative." + cumulative + ".execute");

                    cumulativeRewards.add(new SkillReward(cumulative, displayname, executes));
                }
            }

            Map<Integer, List<SkillReward>> rewards = new HashMap<>();
            if (skillSection.getConfigurationSection("rewards") != null) {

                int level = 1;
                for (String id : skillSection.getConfigurationSection("rewards").getKeys(false)) {
                    List<SkillReward> skillRewards = new ArrayList<>();
                    ConfigurationSection rewardsSection = skillSection.getConfigurationSection("rewards." + id);

                    for (String reward : rewardsSection.getKeys(false)) {

                        String displayname = rewardsSection.getString(reward + ".name");
                        if (rewardsSection.get(reward + ".execute") == null) {
                            HypixelSkills.instance.getLogger().warning("Missing list of executes: " + skillName + " -> " + id + " -> " + reward + ", skipping");
                            continue;
                        }

                        List<String> executes = rewardsSection.getStringList(reward + ".execute");

                        skillRewards.add(new SkillReward(reward, displayname, executes));
                    }

                    rewards.put(level, skillRewards);
                    level++;
                }
            }

            HypixelSkills.instance.getLogger().info("Successfully Loaded: " + skillName);
            Skills.add(new Skill(skill, rewards, cumulativeRewards, skillAbility));
        }
    }

    public Map<SkillType, Double> defaultSkillXp() {
        return new EnumMap<SkillType, Double>(SkillType.class) {{
            for (SkillType skill : SkillType.values()) put(skill, 0.0);
        }};
    }

    public void addLevels(PlayerSkill playerSkill, SkillType skill, int amount) {
        int currLevel = playerSkill.getSkillLevel().get(skill);
        playerSkill.getSkillLevel().put(skill, Methods.limit(currLevel + amount, 25, 0));
        HypixelSkills.instance.getStatsManager().savePlayer(playerSkill, true);
    }

    public void setLevels(PlayerSkill playerSkill, SkillType skill, int amount) {
        playerSkill.getSkillXp().put(skill, 0.0D);
        playerSkill.getSkillLevel().put(skill, Methods.limit(amount, 25, 0));

        HypixelSkills.instance.getStatsManager().savePlayer(playerSkill, true);
    }

    public void takeLevels(PlayerSkill playerSkill, SkillType skill, int amount) {
        int currLevel = playerSkill.getSkillLevel().get(skill);
        playerSkill.getSkillXp().put(skill, 0.0D);
        playerSkill.getSkillLevel().put(skill, Methods.limit(Math.abs(currLevel - amount), 25, 0));

        HypixelSkills.instance.getStatsManager().savePlayer(playerSkill, true);
    }

    public int getSkillLevel(PlayerSkill playerSkill, SkillType skill) {
        return playerSkill.getSkillLevel().getOrDefault(skill, 0);
    }

    public double getSkillXp(PlayerSkill playerSkill, SkillType skill) {
        return playerSkill.getSkillXp().get(skill);
    }

    public double getXpToNextLevel(int level) {
        if (level >= 25) return HypixelSkills.instance.getSettings().xpToNextLevel.get(HypixelSkills.instance.getSettings().xpToNextLevel.size() - 1);
        return HypixelSkills.instance.getSettings().xpToNextLevel.get(level);
    }

    public void gainXp(PlayerSkill playerSkill, SkillType skill, double xp) {
        xp += getSkillXp(playerSkill, skill);
        playerSkill.getSkillXp().put(skill, xp);

        if (xp >= getXpToNextLevel(getSkillLevel(playerSkill, skill))) {
            levelUp(playerSkill, skill, 1);
        }
    }

    public void levelUp(PlayerSkill playerSkill, SkillType skilltype, int amount) {

        // A check to prevent player levelling up zero times
        if (amount < 1) return;

        // Initialize variables
        int level = getSkillLevel(playerSkill, skilltype);
        if (level == 25) return;

        int newLevel = Math.min(level + amount, 25);

        // Sets the level for the skill to levelled up level.
        setLevels(playerSkill, skilltype, newLevel);

        Skill skill = getSkill(skilltype);

        // SEND LEVELLED UP MESSAGE TO PLAYER
        for (String line : HypixelSkills.instance.getConfig("lang").getStringList("SKILLS.LEVEL_UP")) {
            line = line
                    .replace("{SKILL}", Methods.capitalizeFirstWord(skilltype.name()))
                    .replace("{PREVIOUS_LEVEL}", Enchant.getNumeral(level))
                    .replace("{CURRENT_LEVEL}", Enchant.getNumeral(newLevel))
                    .replace("{PREVIOUS_SKILLABILITY}", String.valueOf(4 * level))
                    .replace("{CURRENT_SKILLABILITY}", String.valueOf(4 * newLevel))
                    .replace("{SKILLABILITY}", skill.getAbilityName())
                    .replace("{REWARDS}", toString(skill, newLevel, line));
            playerSkill.getPlayer().sendMessage(Lang.color(line));
        }

        int timesLevelledup = newLevel - level;

        // EXECUTE REWARDS FOR ALL SKILLS LEVELLED UP!
        for (int i = 1; i <= timesLevelledup; i++) {
            for (SkillReward reward : skill.getRewards(level + i)) {
                reward.executeRewards(playerSkill);
            }
        }

        if (amount > 1) {
            playerSkill.getPlayer().sendMessage(Message.SKILL_LEVELLED_UP_AMOUNT.getChatMessage()
                    .replace("{times}", String.valueOf(timesLevelledup))
                    .replace("{newlevel}", String.valueOf(newLevel))
                    .replace("{SKILL}", Methods.capitalizeFirstWord(skilltype.name())));
        }

        playerSkill.getPlayer().playSound(playerSkill.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
    }

    public Skill getSkill(SkillType skillType) {
        for (Skill skill : Skills) {
            if (skill.getSkill() == skillType) return skill;
        }
        return null;
    }

    private String toString(Skill skill, int level, String rewardsLine) {
        StringBuilder builder = new StringBuilder();

        if (rewardsLine.contains("{REWARDS}")) {
            String spaces = rewardsLine.substring(0, rewardsLine.indexOf('{'));

            for (SkillReward cumulative : skill.getCumulative()) {
                builder.append(cumulative.getDisplayname()).append("\n");
            }

            for (SkillReward reward : skill.getRewards(level)) {
                builder.append(spaces).append(reward.getDisplayname()).append("\n");
            }
        }

        if (builder.toString().equals("null")) return "";
        return builder.toString();
    }

    public static SkillType getSkill(String arg) {
        SkillType skill;
        try {
            skill = SkillType.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            HypixelSkills.instance.getLogger().severe(arg + " is not a valid skill name");
            return null;
        }

        return skill;
    }
}
