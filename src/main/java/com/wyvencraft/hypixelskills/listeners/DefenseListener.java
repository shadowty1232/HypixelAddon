package com.wyvencraft.hypixelskills.listeners;

import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.attributes.Attribute;
import com.wyvencraft.wyvencore.attributes.AttributesHandler;
import com.wyvencraft.wyvencore.player.PlayerStats;
import org.bukkit.GameMode;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DefenseListener implements Listener {

    Core plugin = Core.instance;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerAttacked(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Mob) {
            Player p = (Player) e.getEntity();
            if (p.getGameMode() == GameMode.CREATIVE) e.setCancelled(true);

            PlayerStats ps = plugin.getStatsManager().getPlayerStats(p.getUniqueId());

            double finalDamage = e.getDamage();
            if (AttributesHandler.instance.getAttribute(ps, Attribute.DEFENSE) > 0) {
                double reducted = (ps.getDmgReduction() * finalDamage);
                finalDamage -= reducted;
//                Debug.log("dmgReduction " + plugin.decimalFormat.format(reducted));
            }
//            Debug.log("finalDmg " + plugin.decimalFormat.format(finalDamage));

            e.setDamage(finalDamage);
        }
    }
}
