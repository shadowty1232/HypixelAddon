package com.wyvencraft.hypixelskills;

import com.wyvencraft.wyvencore.player.StatsBar;

import java.util.EnumMap;
import java.util.Map;

public class PlayerSkill extends WyvenPlayer {

    private transient StatsBar statsBar;

    private final Map<SkillType, Integer> skillLevel;
    private final Map<SkillType, Double> skillXp;

    public PlayerSkill() {
        skillLevel = new EnumMap<>(SkillType.class);
        skillXp = Skills.instance.defaultSkillXp();
    }

    public void load() {
        enableActionbar();
    }

    private void enableActionbar() {
        statsBar = new StatsBar(this);
        statsBar.start();
    }

    public Actionbar getStatsBar() {
        return statsBar;
    }

    public Map<SkillType, Integer> getSkillLevel() {
        return skillLevel;
    }

    public Map<SkillType, Double> getSkillXp() {
        return skillXp;
    }
}
