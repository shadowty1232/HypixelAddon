package com.wyvencraft.hypixelskills;

import com.wyvencraft.wyvencore.player.PlayerStats;
import com.wyvencraft.wyvencore.utils.CmdAction;

import java.util.List;

public class SkillReward {

    private final String id;
    private final String displayname;
    private final List<String> rewards;

    public SkillReward(String id, String displayname, List<String> rewards) {
        this.id = id;
        this.displayname = displayname;
        this.rewards = rewards;
    }

    public String getId() {
        return id;
    }

    public String getDisplayname() {
        return displayname;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public void executeRewards(PlayerStats ps) {
        for (String reward : rewards) {
            String[] split = reward.split("] ", 2);
            CmdAction.execute(ps, split[0], split[1]);
        }
    }
}
