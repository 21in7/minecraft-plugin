package com.cube;

import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {    
    @Override
    public void onEnable() {
        // 플러그인이 활성화될 때 실행될 코드
        getServer().getPluginManager().registerEvents(new CustomEnchantListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new CustomCropHarvestListener(), this);
        getLogger().info("플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        // 플러그인이 비활성화될 때 실행될 코드
        getLogger().info("플러그인이 비활성화되었습니다.");
    }
}