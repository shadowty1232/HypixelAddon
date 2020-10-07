package com.wyvencraft.hypixelskills;

import java.util.List;
import java.util.Map;

public class Skill {

    private final SkillType skill;
    private final Map<Integer, List<SkillReward>> rewards;
    private final List<SkillReward> cumulative;
    private final String abilityName;

    public Skill(SkillType _skill, Map<Integer, List<SkillReward>> _rewards, List<SkillReward> _cumulative, String _abilityName) {
        this.skill = _skill;
        this.rewards = _rewards;
        this.cumulative = _cumulative;
        this.abilityName = _abilityName;
    }

    public SkillType getSkill() {
        return skill;
    }

    public List<SkillReward> getCumulative() {
        return cumulative;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public SkillType getSkills() {
        return skill;
    }

    public List<SkillReward> getRewards(int level) {
        return rewards.get(level);
    }
}
