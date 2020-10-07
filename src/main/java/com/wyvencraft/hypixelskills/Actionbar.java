package com.wyvencraft.hypixelskills;

import com.wyvencraft.wyvencore.Core;

import com.wyvencraft.wyvencore.attributes.Attribute;
import com.wyvencraft.wyvencore.attributes.AttributesHandler;
import com.wyvencraft.wyvencore.common.Lang;
import org.bukkit.Bukkit;

public abstract class Actionbar {

    public final static Core Plugin = Core.instance;

    public Actionbar(WyvenPlayer wyvenPlayer) {
        this.wyvenPlayer = wyvenPlayer;
    }

    public final WyvenPlayer wyvenPlayer;

    public abstract void start();

    public int gainTimer = 0;
    public SkillType skill;
    private double xp = 0;

    public void gainXp(SkillType skill, double xp) {
        if (Skills.instance.getSkillLevel(wyvenPlayer, skill) == 50) return;
        Skills.instance.gainXp(wyvenPlayer, skill, xp);
        this.skill = skill;
        this.xp = xp;
        this.gainTimer = 2;

        saveDataTimer();
    }

    public void sendActionBar(String bar) {
        if (bar.equals(Plugin.getSettings().bar3))
            bar = bar.replace("{health}", String.valueOf((int) wyvenPlayer.getPlayer().getHealth()))
                    .replace("{maxHealth}", String.valueOf((int) AttributesHandler.instance.getAttribute(wyvenPlayer, Attribute.HEALTH)))
                    .replace("{mana}", String.valueOf(AttributesHandler.instance.getManaPool(wyvenPlayer)))
                    .replace("{maxMana}", String.valueOf(AttributesHandler.instance.getMaxManaPool(wyvenPlayer)))
                    .replace("{defense}", String.valueOf((int) AttributesHandler.instance.getAttribute(wyvenPlayer, Attribute.DEFENSE)));

        else if (bar.equals(Plugin.getSettings().bar2))
            bar = bar.replace("{health}", String.valueOf((int) wyvenPlayer.getPlayer().getHealth()))
                    .replace("{maxHealth}", String.valueOf((int) AttributesHandler.instance.getAttribute(wyvenPlayer, Attribute.HEALTH)))
                    .replace("{mana}", String.valueOf(AttributesHandler.instance.getManaPool(wyvenPlayer)))
                    .replace("{maxMana}", String.valueOf(AttributesHandler.instance.getMaxManaPool(wyvenPlayer)))
                    .replace("{gained}", String.valueOf(xp))
                    .replace("{skill}", skill.name().toLowerCase())
                    .replace("{xp}", Plugin.DECIMALFORMAT.format(Skills.instance.getSkillXp(wyvenPlayer, skill)))
                    .replace("{xpNeeded}", String.valueOf(Skills.instance.getXpToNextLevel(
                            Skills.instance.getSkillLevel(wyvenPlayer, skill))));
        else
            bar = bar.replace("{health}", String.valueOf((int) wyvenPlayer.getPlayer().getHealth()))
                    .replace("{maxHealth}", String.valueOf((int) AttributesHandler.instance.getAttribute(wyvenPlayer, Attribute.HEALTH)))
                    .replace("{mana}", String.valueOf(AttributesHandler.instance.getManaPool(wyvenPlayer)))
                    .replace("{maxMana}", String.valueOf(AttributesHandler.instance.getMaxManaPool(wyvenPlayer)));

        wyvenPlayer.getPlayer().sendActionBar(Lang.color(bar));
    }

    boolean isSavingData = false;

    public void saveDataTimer() {
        if (isSavingData) return;

        isSavingData = true;

        Bukkit.getScheduler().runTaskLater(Plugin, () -> {
            Plugin.getStatsManager().savePlayer(wyvenPlayer, true);
            isSavingData = false;
        }, 200);
    }
}
