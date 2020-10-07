package com.wyvencraft.hypixelskills.listeners;

import com.wyvencraft.hypixelskills.HypixelPlayer;
import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.player.PlayerStats;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    Core plugin = Core.instance;

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        HypixelPlayer hp = plugin.getStatsManager().getPlayerStats(e.getPlayer().getUniqueId());
        hp.addMana(hp.maxMana());
    }
}
