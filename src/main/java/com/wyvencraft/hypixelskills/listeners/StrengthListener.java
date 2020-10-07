package com.wyvencraft.hypixelskills.listeners;

import com.wyvencraft.hypixelskills.HypixelPlayer;
import com.wyvencraft.hypixelskills.skills.SkillType;
import com.wyvencraft.hypixelskills.skills.Skills;
import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.attributes.Attribute;
import com.wyvencraft.wyvencore.attributes.AttributesHandler;
import com.wyvencraft.wyvencore.player.PlayerStats;
import com.wyvencraft.wyvencore.utils.DamageIndicator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class StrengthListener implements Listener {
    Core plugin = Core.instance;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity().hasMetadata("NPC")) return;

        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
            if (e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                PlayerStats ps = plugin.getStatsManager().getPlayerStats(damager.getUniqueId());

                e.setDamage(getFinalDamage(ps, e.getDamage(), e.getEntity()));
            }

            if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                if (proj.getShooter() instanceof Player) {
                    Player shooter = (Player) proj.getShooter();
                    PlayerStats ps = plugin.getStatsManager().getPlayerStats(shooter.getUniqueId());

                    e.setDamage(getFinalDamage(ps, e.getDamage(), e.getEntity()));
                }
            }
        }
    }

    private double getFinalDamage(HypixelPlayer hp, double damage, Entity target) {

        double skillBuff = Skills.instance.getSkillLevel(hp, SkillType.COMBAT);
        double combatBuff = ((skillBuff / 100) * hp.getEffectiveDamage(damage));
        double finalDamage = hp.getEffectiveDamage(damage) + combatBuff;

        // IF PLAYER HITS A CRITICAL HIT.
        double critChance = AttributesHandler.instance.getAttribute(hp, Attribute.CRIT_CHANCE) / 100;
        if (critChance >= Math.random()) {
            double critDamage = AttributesHandler.instance.getAttribute(hp, Attribute.CRIT_DAMAGE);
            double crit = finalDamage * (1 + critDamage / 100);
            DamageIndicator.instance.takeDamage(target, crit, true);
            return crit;
        }
        DamageIndicator.instance.takeDamage(target, finalDamage, false);

        return finalDamage;
    }
}
