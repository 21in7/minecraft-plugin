package com.cube;


import net.momirealms.customcrops.api.event.CropBreakEvent;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import dev.lone.itemsadder.api.CustomStack;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;









public class CustomCropHarvestListener implements Listener {

    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        //Location cropLocation = event2.getLocation();
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();

        
        //CustomCropsAPI api = CustomCropsAPI.getInstance();

        //CCPot pot = api.getPotAt(blockLocation);
        //Bukkit.getLogger().info("[Cube] pot" + pot);
        //Bukkit.getLogger().info("[Cube] api" + api);
        
        //CCGrowingCrop crop = api.getCropAt(cropLocation);
        //Bukkit.getLogger().info("[Cube] blockLocation: " + blockLocation);
        //Bukkit.getLogger().info("[Cube] Crop Location: " + crop);

        

        // 농작물이 완전히 성장했는지 확인
        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();
            if (ageable.getAge() == ageable.getMaximumAge()) {
                handleHarvest(block, itemInHand);
                Bukkit.getLogger().info("[Cube] .");
            }
        }
    }

    @EventHandler
    public void oncropBreak(CropBreakEvent event) {
        Location location = event.getLocation();
        Block block = location.getWorld().getBlockAt(location);
        String cropItemID = event.getCropItemID();
        //Bukkit.getLogger().info("[Cube] cropItemID : " + cropItemID);
        //Bukkit.getLogger().info("[Cube] location : " + location);
        //Bukkit.getLogger().info("[Cube] block : " + block);
        
        if (block.getType() != Material.AIR) {

        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if ("customcrops:pineapple_stage_4".equals(cropItemID)) {
                customhandleHarvest(block, itemInHand, cropItemID);
                //Bukkit.getLogger().info("[Cube] 커스텀 작물");
            } else if ("customcrops:cabbage_stage_4".equals(cropItemID)) {
                customhandleHarvest(block, itemInHand, cropItemID);
            } else if ("customcrops:chinese_cabbage_stage_4".equals(cropItemID)) {
                customhandleHarvest(block, itemInHand, cropItemID);
            } else if ("customcrops:corn_stage_4".equals(cropItemID)) {
                customhandleHarvest(block, itemInHand, cropItemID);
            } else if ("customcrops:eggplant_stage_4".equals(cropItemID)) {
                customhandleHarvest(block, itemInHand, cropItemID);
            } else if ("customcrops:garlic_stage_4".equals(cropItemID)) {
                customhandleHarvest(block, itemInHand, cropItemID);
            }
        }
    }


    private void handleHarvest(Block block, ItemStack itemInHand) {
        // 플레이어가 특별 옵션이 있는 도구를 들고 있는지 확인
        if (ToolOption.hasSpecialOption(itemInHand, "[괭이] 농작물 수확 할때")) {
            ToolOption.OptionValues optionValues = ToolOption.parseOptionString(itemInHand.getItemMeta().getLore());
            if (optionValues != null) {
                Random random = new Random();
                if (random.nextInt(100) < optionValues.minChance) {
                // 기본 블록에 대한 처리
                    ItemStack drop = new ItemStack(block.getType(), optionValues.maxLoot);
                    block.getWorld().dropItemNaturally(block.getLocation(), drop);
                    Bukkit.getLogger().info("[Cube] 기본 아이템을 드롭합니다.");
                }
            }
        }
    }
    private void customhandleHarvest(Block block, ItemStack itemInHand, String cropItemID) {
        // 플레이어가 특별 옵션이 있는 도구를 들고 있는지 확인
        if (ToolOption.hasSpecialOption(itemInHand, "[괭이] 농작물 수확 할때")) {
            ToolOption.OptionValues optionValues = ToolOption.parseOptionString(itemInHand.getItemMeta().getLore());
            if (optionValues != null) {
                Random random = new Random();
                
                // ItemsAdder 커스텀 블록 확인
                //CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);


                if ("customcrops:pineapple_stage_4".equals(cropItemID)) {
                    CustomStack customStack = CustomStack.getInstance("customcrops:pineapple");
                    if (customStack != null && random.nextInt(100) < optionValues.minChance) {
                        ItemStack customItem = customStack.getItemStack();
                        customItem.setAmount(optionValues.maxLoot);
                        block.getWorld().dropItemNaturally(block.getLocation(), customItem);
                        //Bukkit.getLogger().info("[Cube] 커스텀 작물 드랍");
                    }
                } else if ("customcrops:cabbage_stage_4".equals(cropItemID)) {
                    CustomStack customStack = CustomStack.getInstance("customcrops:cabbage");
                    if (customStack != null && random.nextInt(100) < optionValues.minChance) {
                        ItemStack customItem = customStack.getItemStack();
                        customItem.setAmount(optionValues.maxLoot);
                        block.getWorld().dropItemNaturally(block.getLocation(), customItem);
                    }
                } else if ("customcrops:chinese_cabbage_stage_4".equals(cropItemID)) {
                    CustomStack customStack = CustomStack.getInstance("customcrops:chinese_cabbage");
                    if (customStack != null && random.nextInt(100) < optionValues.minChance) {
                        ItemStack customItem = customStack.getItemStack();
                        customItem.setAmount(optionValues.maxLoot);
                        block.getWorld().dropItemNaturally(block.getLocation(), customItem);
                    }
                } else if ("customcrops:corn_stage_4".equals(cropItemID)) {
                    CustomStack customStack = CustomStack.getInstance("customcrops:corn");
                    if (customStack != null && random.nextInt(100) < optionValues.minChance) {
                        ItemStack customItem = customStack.getItemStack();
                        customItem.setAmount(optionValues.maxLoot);
                        block.getWorld().dropItemNaturally(block.getLocation(), customItem);
                    }
                } else if("customcrops:eggplant_stage_4".equals(cropItemID)) {
                    CustomStack customStack = CustomStack.getInstance("customcrops:eggplant");
                    if (customStack != null && random.nextInt(100) < optionValues.minChance) {
                        ItemStack customItem = customStack.getItemStack();
                        customItem.setAmount(optionValues.maxLoot);
                        block.getWorld().dropItemNaturally(block.getLocation(), customItem);
                    }
                } else if ("customcrops:garlic_stage_4".equals(cropItemID)) {
                    CustomStack customStack = CustomStack.getInstance("customcrops:garlic");
                    if (customStack != null && random.nextInt(100) < optionValues.minChance) {
                        ItemStack customItem = customStack.getItemStack();
                        customItem.setAmount(optionValues.maxLoot);
                        block.getWorld().dropItemNaturally(block.getLocation(), customItem);
                    }
                }
            }
        }
    }
}