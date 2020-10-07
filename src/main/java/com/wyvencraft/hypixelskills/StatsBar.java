package com.wyvencraft.hypixelskills;

import com.wyvencraft.hypixelskills.attributes.Attribute;
import com.wyvencraft.wyvencore.Core;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class StatsBar extends Actionbar {

    public StatsBar(HypixelPlayer hypixelPlayer) {
        super(hypixelPlayer);
    }

    @Override
    public void start() {
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (hypixelPlayer.getPlayer() == null || !hypixelPlayer.getPlayer().isOnline())
                    cancel();

                // ACTIONBAR
                if (gainTimer > 0) {
                    sendActionBar(Plugin.getSettings().bar2);
                    gainTimer--;
                } else if (hypixelPlayer.getTotalAttributeLevel(Attribute.DEFENSE) > 0) {
                    sendActionBar(Plugin.getSettings().bar3);
                } else {
                    sendActionBar(Plugin.getSettings().bar1);
                }

                if (hypixelPlayer.getPlayer().isDead()) return;

                if (Plugin.getSettings().requireManaToSprint && hypixelPlayer.getPlayer().isSprinting()) {
                    if (hypixelPlayer.getPlayer().isJumping()) hypixelPlayer.subtractMana(5);
                    else hypixelPlayer.subtractMana(2);

                } else {
                    if (counter > 1) {
                        // MANA REGENERATION
                        if (hypixelPlayer.getCurrentMana() < hypixelPlayer.maxMana()) {
                            hypixelPlayer.addMana(hypixelPlayer.maxMana() / 50);
                        }
                    }
                }

                if (Plugin.getSettings().requireManaToSprint)
                    if (hypixelPlayer.getCurrentMana() == 0) {
                        hypixelPlayer.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.08);
                        hypixelPlayer.getPlayer().setSprinting(false);
                    }

                if (hypixelPlayer.getCurrentMana() > 10) {
                    if (hypixelPlayer.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() == 0.08)
                        hypixelPlayer.apply(Attribute.SPEED);
                }

                if (hypixelPlayer.getCurrentMana() >= Plugin.getSettings().minManaToRegen) {
                    if (hypixelPlayer.getPlayer().getHealth() < hypixelPlayer.getTotalAttributeLevel(Attribute.HEALTH)) {
                        if (counter > 1) {

                            EntityRegainHealthEvent erhe = new EntityRegainHealthEvent(hypixelPlayer.getPlayer(), hypixelPlayer.getHpPerSecond(), EntityRegainHealthEvent.RegainReason.CUSTOM);
                            Bukkit.getPluginManager().callEvent(erhe);

                            if (!erhe.isCancelled()) {
                                double health = hypixelPlayer.getPlayer().getHealth() + hypixelPlayer.getHpPerSecond();
                                health = Math.min(health, hypixelPlayer.getTotalAttributeLevel(Attribute.HEALTH));

                                hypixelPlayer.getPlayer().setHealth(health);
                            }
                        }
                    }
                }

                if (counter > 1) counter = 0;
                counter++;
            }
        }.runTaskTimer(Core.instance, 0, 10);
    }
}
