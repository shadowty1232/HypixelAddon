package com.wyvencraft.hypixelskills.attributes;

import com.wyvencraft.wyvencore.Core;

public enum Attribute {

    HEALTH(Core.instance.getSettings().defaultHealth, Core.instance.getSettings().maxBaseHealth, false),
    DEFENSE(Core.instance.getSettings().defaultDefense, Core.instance.getSettings().maxBaseDefense, false),
    STRENGTH(Core.instance.getSettings().defaultStrength, Core.instance.getSettings().maxBaseStrength, false),
    SPEED(Core.instance.getSettings().defaultSpeed, Core.instance.getSettings().maxBaseSpeed, true),
    CRIT_CHANCE(Core.instance.getSettings().defaultCritChance, Core.instance.getSettings().maxBaseCritChance, true),
    CRIT_DAMAGE(Core.instance.getSettings().defaultCritDamage, Core.instance.getSettings().maxBaseCritDamage, true),
    INTELLIGENCE(Core.instance.getSettings().defaultIntelligence, Core.instance.getSettings().maxBaseIntelligence, false),
    ATTACK_SPEED(Core.instance.getSettings().defaultAttackSpeed, Core.instance.getSettings().maxBaseAttackSpeed, true);

    public double base;
    public double baseMax;
    public boolean percentage;

    Attribute(double base, double baseMax, boolean percentage) {
        this.base = base;
        this.baseMax = baseMax;
        this.percentage = percentage;
    }
}
