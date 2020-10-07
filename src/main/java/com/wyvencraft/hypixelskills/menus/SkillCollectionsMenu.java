package com.wyvencraft.hypixelskills.menus;


import com.wyvencraft.hypixelskills.skills.SkillType;
import com.wyvencraft.hypixelskills.skills.Skills;
import com.wyvencraft.wyvencore.Core;
import com.wyvencraft.wyvencore.collections.Collection;
import com.wyvencraft.wyvencore.collections.Collections;
import com.wyvencraft.wyvencore.common.ItemBuilder;
import com.wyvencraft.wyvencore.common.Lang;
import com.wyvencraft.wyvencore.menus.Menu;
import com.wyvencraft.wyvencore.menus.MenuSettings;
import com.wyvencraft.wyvencore.menus.menuproviders.CollectionMenuProvider;
import io.github.portlek.smartinventory.Page;
import io.github.portlek.smartinventory.SmartInventory;
import io.github.portlek.smartinventory.event.abs.SmartEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkillCollectionsMenu extends Menu {

    public static SkillCollectionsMenu instance;

    public SkillCollectionsMenu(Core _plugin, SmartInventory _inventory, InventoryType _type) {
        super(_plugin, _inventory, _type);
        instance = this;
    }

    @Override
    public void load() {
        FileConfiguration config = Plugin.getConfig("collections");

        List<String> collectionItemLore = Plugin.getConfig("lang").getStringList("COLLECTIONS.COLLECTION_ITEM_LORE");
        MenuSettings settings = getMenuSettings("SkillCollection");

        ItemStack lockedCollection = new ItemBuilder().toItemBuilder(file.getConfigurationSection(type.name() + ".SkillCollection.lockedCollection")).build();

        for (String skillType : config.getKeys(false)) {
            SkillType skill = Skills.getSkill(skillType);
            if (skill == null) continue;
            List<Collection> collectionList = new ArrayList<>();
            for (String colID : config.getConfigurationSection(skillType).getKeys(false)) {
                ConfigurationSection section = config.getConfigurationSection(skillType + "." + colID);

                Collection collection = Collections.instance.loadCollection(colID, section, collectionItemLore, skill);

                if (collection == null) continue;

                collectionList.add(collection);
            }


            Page MENU = Page.build(Inventory,
                    new CollectionMenuProvider(collectionList,
                            settings.getGuiItems(),
                            lockedCollection))
                    .row(settings.getRows())
                    .title(Lang.color(settings.getTitle().replace("{skill}", skillType)))
                    .parent(settings.getParent())
                    .whenBottomClick(SmartEvent::cancel);
            Pages.put(skillType.toLowerCase(), MENU);

            Collections.instance.collections.addAll(collectionList);
        }
    }
}
