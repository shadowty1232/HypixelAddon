package com.wyvencraft.hypixelskills.listeners;

import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.player.PlayerStats;
import com.wyvencraft.wyvencore.skills.SkillType;
import com.wyvencraft.wyvencore.skills.Skills;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.function.Consumer;

public class BlockListener implements Listener {

    Core plugin = Core.instance;

    PlayerStats ps;
    Material material;
    SkillType skill;
    double xp = 0.0;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (e.getBlock().hasMetadata("PLACED")) return;

        Player p = e.getPlayer();
        Block block = e.getBlock();

        if (plugin.getSettings().xpYield.containsKey(block.getType())) {
            e.setDropItems(false);

            if (material == null || material != block.getType()) {
                material = block.getType();
                skill = plugin.getSettings().xpYield.get(material).keySet().stream().findFirst().get();
                xp = plugin.getSettings().xpYield.get(material).get(skill);
            }

            if (ps == null || ps.getPlayer().getUniqueId() != p.getUniqueId()) {
                ps = plugin.getStatsManager().getPlayerStats(p.getUniqueId());
            }

            // CALCULATE LOOT BONUS
            double enchantBonus = 0.0d;
            if (p.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                int enchLevel = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                enchantBonus = (double) enchLevel * 10;
            }

            Collection<ItemStack> drops = block.getDrops();

            // HYPIXEL SKYBLOCK ALGORITHM (CALCULATE % of double drops)
            double skillBonus = Skills.instance.getSkillLevel(ps, skill) * 0.04;
            double averageAmount = block.getDrops().size() * (1 + skillBonus) * (1 + (enchantBonus / 100)) / 100;
            double random = Math.random();
            if (averageAmount >= random) {
                if (Skills.instance.getSkillLevel(ps, skill) < 26) {
                    drops.addAll(block.getDrops());
                } else {
                    for (int i = 0; i < 3; i++) {
                        drops.addAll(block.getDrops());
                    }
                }
            }

            if (skill == SkillType.FARMING) {
                if (block.getBlockData() instanceof Ageable) {
                    Ageable crop = (Ageable) block.getBlockData();

                    if (crop.getAge() >= crop.getMaximumAge()) {
                        if (p.getInventory().getItemInMainHand().getType().name().endsWith("_HOE")) {
                            e.setCancelled(true);
                            crop.setAge(0);
                            block.setType(crop.getMaterial());
                        } else {
                            block.setType(Material.AIR);
                        }

                    } else if (p.getInventory().getItemInMainHand().getType().name().endsWith("_HOE")) {
                        e.setCancelled(true);
                    }
                }
            }

            // DROPS ALL ITEMS
            drops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation().add(0, .5, 0), drop));

            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 3f);
            ps.getStatsBar().gainXp(skill, xp);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE || e.getPlayer().isOp()) return;
        e.getBlockPlaced().setMetadata("PLACED", new FixedMetadataValue(plugin, e.getPlayer().getUniqueId()));
    }


    Consumer<BlockBreakEvent> consumer = new Consumer<BlockBreakEvent>() {
        @Override
        public void accept(BlockBreakEvent event) {
            
        }
    };
}

