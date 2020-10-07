package com.wyvencraft.hypixelskills;

import com.wyvencraft.hypixelskills.commands.SkillsCMD;
import org.bukkit.plugin.java.JavaPlugin;

public class HypixelSkills extends WyvenCore, JavaPlugin {

    public final static WyvenCore CORE;

    @Override
    private void onEnable() {
        registerListeners();
        registerCommands();
    }

    @Override
    private void registerListeners() {

    }

    @Override
    private void registerCommands() {
        this.getCommand("skills").setExecutor(new SkillsCMD());
    }
}
