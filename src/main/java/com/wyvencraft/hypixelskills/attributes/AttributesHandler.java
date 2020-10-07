package com.wyvencraft.hypixelskills.attributes;

import com.wyvencraft.hypixelskills.HypixelAddon;
import com.wyvencraft.hypixelskills.HypixelPlayer;

import java.util.EnumMap;
import java.util.Map;

public class AttributesHandler {

    public static AttributesHandler instance;

    public AttributesHandler() {
        instance = this;
    }

    public Map<Attribute, Double> getDefaultBaseAttributes() {
        Map<Attribute, Double> defaultAttributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            defaultAttributes.put(attribute, attribute.base);
        }
        return defaultAttributes;
    }

    public Map<Attribute, Double> getDefaultBonusAttributes() {
        Map<Attribute, Double> bonusAttributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            bonusAttributes.put(attribute, 0.0d);
        }
        return bonusAttributes;
    }

    public double getBaseAttribute(HypixelPlayer hypixelPlayer, Attribute attribute) {
        return hypixelPlayer.getBaseAttributes().get(attribute);
    }

    public double getBonusAttribute(HypixelPlayer hypixelPlayer, Attribute attribute) {
        return hypixelPlayer.getBonusAttributes().get(attribute);
    }

    public double getAttribute(HypixelPlayer hypixelPlayer, Attribute attribute) {
        return hypixelPlayer.getTotalAttributeLevel(attribute);
    }

    public void add(HypixelPlayer hypixelPlayer, Attribute attribute, double amount, boolean bonus) {
        double currLevel = bonus ? hypixelPlayer.getBonusAttributes().get(attribute) : hypixelPlayer.getBaseAttributes().get(attribute);
        if (!bonus) hypixelPlayer.getBaseAttributes().put(attribute, currLevel + amount);
        else hypixelPlayer.getBonusAttributes().put(attribute, currLevel + amount);

        hypixelPlayer.apply(attribute);

        if (!bonus) HypixelAddon.instance.getStatsManager().savePlayer(hypixelPlayer, true);
    }

    public void set(HypixelPlayer hypixelPlayer, Attribute attribute, double amount, boolean bonus) {
        if (!bonus) hypixelPlayer.getBaseAttributes().put(attribute, amount);
        else hypixelPlayer.getBonusAttributes().put(attribute, amount);

        hypixelPlayer.apply(attribute);

        if (!bonus) HypixelAddon.instance.getStatsManager().savePlayer(hypixelPlayer, true);

    }

    public void take(HypixelPlayer hypixelPlayer, Attribute attribute, double amount, boolean bonus) {
        double currLevel = bonus ? hypixelPlayer.getBonusAttributes().get(attribute) : hypixelPlayer.getBaseAttributes().get(attribute);
        if (!bonus) hypixelPlayer.getBaseAttributes().put(attribute, currLevel - amount);
        else hypixelPlayer.getBonusAttributes().put(attribute, currLevel - amount);
        hypixelPlayer.apply(attribute);

        if (!bonus) HypixelAddon.instance.getStatsManager().savePlayer(hypixelPlayer, true);
    }

    public int getMaxManaPool(HypixelPlayer hypixelPlayer) {
        return 100 + (int) getAttribute(hypixelPlayer, Attribute.INTELLIGENCE);
    }

    public int getManaPool(HypixelPlayer hypixelPlayer) {
        return hypixelPlayer.getCurrentMana();
    }

    public static Attribute getAttribute(String arg) {
        Attribute attr;
        try {
            attr = Attribute.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }

        return attr;
    }
}
