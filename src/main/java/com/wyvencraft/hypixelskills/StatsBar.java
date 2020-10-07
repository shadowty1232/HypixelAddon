package com.wyvencraft.hypixelskills;

import com.wyvencraft.wyvencore.Core;
import org.bukkit.scheduler.BukkitRunnable;

public class StatsBar extends Actionbar {

    public StatsBar(WyvenPlayer wyvenPlayer) {
        super(wyvenPlayer);
    }

    @Override
    public void start() {
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (wyvenPlayer.getPlayer() == null || !wyvenPlayer.getPlayer().isOnline())
                    cancel();

                // ACTIONBAR
                if (gainTimer > 0) {
                    sendActionBar(Plugin.getSettings().bar2);
                    gainTimer--;
//                } else if (wyvenPlayer.getTotalAttributeLevel(Attribute.DEFENSE) > 0) {
//                    sendActionBar(Plugin.getSettings().bar3);
                } else {
                    sendActionBar(Plugin.getSettings().bar1);
                }

                if (wyvenPlayer.getPlayer().isDead()) return;

//                if (Plugin.getSettings().requireManaToSprint && wyvenPlayer.getPlayer().isSprinting()) {
//                    if (wyvenPlayer.getPlayer().isJumping()) wyvenPlayer.subtractMana(5);
//                    else wyvenPlayer.subtractMana(2);
//
//                } else {
//                    if (counter > 1) {
//                        // MANA REGENERATION
//                        if (wyvenPlayer.getCurrentMana() < wyvenPlayer.maxMana()) {
//                            wyvenPlayer.addMana(wyvenPlayer.maxMana() / 50);
//                        }
//                    }
//                }
//
//                if (Plugin.getSettings().requireManaToSprint)
//                    if (wyvenPlayer.getCurrentMana() == 0) {
//                        wyvenPlayer.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.08);
//                        wyvenPlayer.getPlayer().setSprinting(false);
//                    }
//
//                if (wyvenPlayer.getCurrentMana() > 10) {
//                    if (wyvenPlayer.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() == 0.08)
//                        wyvenPlayer.apply(Attribute.SPEED);
//                }
//
//                if (wyvenPlayer.getCurrentMana() >= Plugin.getSettings().minManaToRegen) {
//                    if (wyvenPlayer.getPlayer().getHealth() < wyvenPlayer.getTotalAttributeLevel(Attribute.HEALTH)) {
//                        if (counter > 1) {
//
//                            EntityRegainHealthEvent erhe = new EntityRegainHealthEvent(wyvenPlayer.getPlayer(), wyvenPlayer.getHpPerSecond(), EntityRegainHealthEvent.RegainReason.CUSTOM);
//                            Bukkit.getPluginManager().callEvent(erhe);
//
//                            if (!erhe.isCancelled()) {
//                                double health = wyvenPlayer.getPlayer().getHealth() + wyvenPlayer.getHpPerSecond();
//                                health = Math.min(health, wyvenPlayer.getTotalAttributeLevel(Attribute.HEALTH));
//
//                                wyvenPlayer.getPlayer().setHealth(health);
//                            }
//                        }
//                    }
//                }

                if (counter > 1) counter = 0;
                counter++;
            }
        }.runTaskTimer(Core.instance, 0, 10);
    }
}
