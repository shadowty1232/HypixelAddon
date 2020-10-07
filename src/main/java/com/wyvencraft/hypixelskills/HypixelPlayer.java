package com.wyvencraft.hypixelskills;

import com.wyvencraft.hypixelskills.attributes.Attribute;
import com.wyvencraft.hypixelskills.attributes.AttributesHandler;
import com.wyvencraft.hypixelskills.skills.SkillType;

import com.wyvencraft.wyvencore.player.WyvenPlayer;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class HypixelPlayer extends WyvenPlayer {

    private transient StatsBar statsBar;

    private transient double hpPerSecond;
    private transient double baseDamage;
    private transient double effektiveDamage;

    private int currentMana;

    private final Map<Attribute, Double> baseAttributes;
    private final Map<Attribute, Double> bonusAttributes;

    private final Map<SkillType, Integer> skillLevel;
    private final Map<SkillType, Double> skillXp;

    public HypixelPlayer(Player player) {
        super(player);
        this.baseAttributes = AttributesHandler.instance.getDefaultBaseAttributes();
        this.bonusAttributes = AttributesHandler.instance.getDefaultBonusAttributes();
        this.skillLevel = new EnumMap<>(SkillType.class);
        this.skillXp = new EnumMap<>(SkillType.class);
    }

    public void load() {
        for (Attribute attr : Attribute.values()) {
            apply(attr);
        }

        enableActionbar();
    }

    @Override
    public void reset() {
        new HypixelPlayer(getPlayer());
    }

    private void enableActionbar() {
        statsBar = new StatsBar(this);
        statsBar.start();
    }

    public double getDmgReduction() {
        double defenseSkill = getTotalAttributeLevel(Attribute.DEFENSE);
        return defenseSkill / (defenseSkill + Attribute.HEALTH.base);
    }

    public double getHpPerSecond() {
        return hpPerSecond;
    }

    public double getEffectiveDamage(double damage) {
        return effektiveDamage + damage;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public int getEffHealth() {
        return (int) (getTotalAttributeLevel(Attribute.HEALTH) * ((getTotalAttributeLevel(Attribute.DEFENSE) + Attribute.HEALTH.base) / 100));
    }

    public void setHpPerSecond(int amount) {
        hpPerSecond += amount;
    }

    public double getTotalAttributeLevel(Attribute attribute) {
        return getBaseAttributes().get(attribute) + getBonusAttributes().get(attribute);
    }

    public Map<Attribute, Double> getBaseAttributes() {
        return baseAttributes;
    }

    public Map<Attribute, Double> getBonusAttributes() {
        return bonusAttributes;
    }

    public int getCurrentMana() {
        return this.currentMana;
    }

    public void addMana(int mana) {
        this.currentMana += mana;
        this.currentMana = Math.min(currentMana, maxMana());
    }

    public void subtractMana(int mana) {
        this.currentMana -= mana;
        this.currentMana = Math.max(0, currentMana);
    }

    public Actionbar getStatsBar() {
        return statsBar;
    }

    public int maxMana() {
        return (int) (100 + getTotalAttributeLevel(Attribute.INTELLIGENCE));
    }

    public void apply(Attribute attribute) {
        switch (attribute) {
            case SPEED:
                double baseSpeedPercent = getBaseAttributes().get(attribute);
                if (attribute.baseMax != -1 && baseSpeedPercent > attribute.baseMax)
                    baseSpeedPercent = attribute.baseMax;
                double movementspeed = (baseSpeedPercent + bonusAttributes.get(attribute)) / 1000;
                getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(movementspeed);
                break;
            case HEALTH:
                double health = getBaseAttributes().get(attribute) + bonusAttributes.get(attribute);
                getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                getPlayer().setHealthScale(20);

                this.hpPerSecond = 0.8 + (health / 100);

                break;
            case ATTACK_SPEED:
                double basePercent = getBaseAttributes().get(attribute);
                if (attribute.baseMax != -1 && basePercent > attribute.baseMax) basePercent = attribute.baseMax;
                double attackspeed = (basePercent + bonusAttributes.get(attribute)) / 1000;
                getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4 + attackspeed);
                break;
            case STRENGTH:
                double baseStrength = getBaseAttributes().get(attribute);
                this.baseDamage = (5 + (Math.round(baseStrength / 5)) * (1 + baseStrength / 100));
                double strength = getTotalAttributeLevel(attribute);
                this.effektiveDamage = (5 + (Math.round(strength / 5)) * (1 + strength / 100));
            default:
                break;
        }
    }


    public Map<SkillType, Integer> getSkillLevel() {
        return skillLevel;
    }

    public Map<SkillType, Double> getSkillXp() {
        return skillXp;
    }
}
