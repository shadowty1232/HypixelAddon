package com.wyvencraft.hypixelskills;

import com.wyvencraft.hypixelskills.attributes.Attribute;
import com.wyvencraft.hypixelskills.attributes.AttributesHandler;
import com.wyvencraft.hypixelskills.skills.SkillType;
import com.wyvencraft.hypixelskills.skills.Skills;

import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.common.Lang;
import org.bukkit.Bukkit;

public abstract class Actionbar {

    public final static Core Plugin = Core.instance;

    public final HypixelPlayer hypixelPlayer;

    public Actionbar(HypixelPlayer _hypixelPlayer) {
        hypixelPlayer = _hypixelPlayer;
    }

    public abstract void start();

    public int gainTimer = 0;
    public SkillType skill;
    private double xp = 0;

    public void gainXp(SkillType skill, double xp) {
        if (Skills.instance.getSkillLevel(hypixelPlayer, skill) == 50) return;
        Skills.instance.gainXp(hypixelPlayer, skill, xp);
        this.skill = skill;
        this.xp = xp;
        this.gainTimer = 2;

        saveDataTimer();
    }

    public void sendActionBar(String bar) {
        if (bar.equals(Plugin.getSettings().bar3))
            bar = bar.replace("{health}", String.valueOf((int) hypixelPlayer.getPlayer().getHealth()))
                    .replace("{maxHealth}", String.valueOf((int) AttributesHandler.instance.getAttribute(hypixelPlayer, Attribute.HEALTH)))
                    .replace("{mana}", String.valueOf(AttributesHandler.instance.getManaPool(hypixelPlayer)))
                    .replace("{maxMana}", String.valueOf(AttributesHandler.instance.getMaxManaPool(hypixelPlayer)))
                    .replace("{defense}", String.valueOf((int) AttributesHandler.instance.getAttribute(hypixelPlayer, Attribute.DEFENSE)));

        else if (bar.equals(Plugin.getSettings().bar2))
            bar = bar.replace("{health}", String.valueOf((int) hypixelPlayer.getPlayer().getHealth()))
                    .replace("{maxHealth}", String.valueOf((int) AttributesHandler.instance.getAttribute(hypixelPlayer, Attribute.HEALTH)))
                    .replace("{mana}", String.valueOf(AttributesHandler.instance.getManaPool(hypixelPlayer)))
                    .replace("{maxMana}", String.valueOf(AttributesHandler.instance.getMaxManaPool(hypixelPlayer)))
                    .replace("{gained}", String.valueOf(xp))
                    .replace("{skill}", skill.name().toLowerCase())
                    .replace("{xp}", Plugin.DECIMALFORMAT.format(Skills.instance.getSkillXp(hypixelPlayer, skill)))
                    .replace("{xpNeeded}", String.valueOf(Skills.instance.getXpToNextLevel(
                            Skills.instance.getSkillLevel(hypixelPlayer, skill))));
        else
            bar = bar.replace("{health}", String.valueOf((int) hypixelPlayer.getPlayer().getHealth()))
                    .replace("{maxHealth}", String.valueOf((int) AttributesHandler.instance.getAttribute(hypixelPlayer, Attribute.HEALTH)))
                    .replace("{mana}", String.valueOf(AttributesHandler.instance.getManaPool(hypixelPlayer)))
                    .replace("{maxMana}", String.valueOf(AttributesHandler.instance.getMaxManaPool(hypixelPlayer)));

        hypixelPlayer.getPlayer().sendActionBar(Lang.color(bar));
    }

    boolean isSavingData = false;

    public void saveDataTimer() {
        if (isSavingData) return;

        isSavingData = true;

        Bukkit.getScheduler().runTaskLater(Plugin, () -> {
            Plugin.getStatsManager().savePlayer(hypixelPlayer, true);
            isSavingData = false;
        }, 200);
    }
}
