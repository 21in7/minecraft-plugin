package com.cube;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;




public class CustomEnchantListener implements Listener {
    private HashMap<UUID, Boolean> playerState = new HashMap<>();



    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID playerId = player.getUniqueId();
        ItemStack clickedItem = event.getCurrentItem();

        // 아이템을 우클릭해서 상태 저장
        if (event.getClick().isRightClick()) {
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                ItemMeta meta = clickedItem.getItemMeta();
                if (meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
                    playerState.put(playerId, true);
                    //Bukkit.getLogger().info("[Cube] 커스텀 모델 데이터 1번 클릭 상태 저장");
                    // 여기서 아이템을 소모하지 않고 상태만 저장합니다.
                    event.setCancelled(true); // 이벤트를 취소하여 아이템이 소모되지 않도록 합니다.
                    return;
                }
            }
        }

        // 아이템을 우클릭한 상태라면 특정 장비를 좌클릭 할 때 랜덤 옵션 부여
        if (event.getClick().isLeftClick()) {
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                ItemMeta meta = clickedItem.getItemMeta();
                if (playerState.containsKey(playerId) && playerState.get(playerId)) {
                    if (isToolOrWeapon(clickedItem.getType())) {
                        //Bukkit.getLogger().info("[Cube] 도구 또는 무기 확인됨");
                        // 랜덤 옵션 가져오기
                        String randomOption = ToolOption.getRandomOption();

                        if (randomOption != null) {
                            //Bukkit.getLogger().info("[Cube] 랜덤 옵션 생성됨: " + randomOption);
                            List<String> lore = meta.getLore();


                            if (lore == null) {
                                lore = new ArrayList<>();
                                lore.add(randomOption); // 리스트가 비어있으면 새 옵션 추가
                            } else {
                                if (lore.isEmpty()) {
                                    lore.add(randomOption); // 리스트가 비어있으면 새 옵션 추가
                                } else {
                                    lore.set(0, randomOption); // 첫 번째 옵션을 새 옵션으로 대체
                                }
                            }
                            meta.setLore(lore);
                            clickedItem.setItemMeta(meta);


                            // 커스텀 모델 데이터 1번 아이템을 소모합니다.
                            consumeCustomModelDataItem(player, 1);

                            // 인챈트 부여 사운드 재생
                            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                            // 옵션 부여 후 플레이어에게 메시지 전송
                            player.sendMessage("옵션이 부여 되었습니다!\n" + randomOption);

                            // 플레이어의 인벤토리를 업데이트
                            player.updateInventory();
                            playerState.put(playerId, false); // 상태를 초기화합니다.
                        } else {
                            //Bukkit.getLogger().info("[Cube] 랜덤 옵션 생성 실패");
                        }
                    }
                    event.setCancelled(true); // 아이템 손에 안들리게 클릭 취소
                }
            }
            if (playerState.getOrDefault(playerId, false)) {
                playerState.put(playerId, false); // 상태를 초기화합니다.
                //Bukkit.getLogger().info("[Cube] 지정된 아이템이 아니라서 상태 초기화");
                event.setCancelled(true); // 아이템 손에 안들리게 클릭 취소
            }
        }
    }

    // 인벤 닫으면 상태 초기화
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID playerId = player.getUniqueId();
        playerState.put(playerId, false); // 상태를 초기화합니다.
        //Bukkit.getLogger().info("[Cube] 인벤토리 닫아서 상태 초기화");
    }

    // 아이템 소모
    private void consumeCustomModelDataItem(Player player, int customModelData) {
        ItemStack[] items = player.getInventory().getContents();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasCustomModelData() && meta.getCustomModelData() == customModelData) {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1); // 수량이 1보다 많으면, 수량을 하나 줄입니다.
                    } else {
                        player.getInventory().setItem(i, null); // 아이템 제거
                    }
                    break; // 하나만 제거하고 루프 종료
                }
            }
        }
    }

    // 아이템이 도구나 무기인지 확인하는 메소드
    private boolean isToolOrWeapon(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.DIAMOND_PICKAXE ||
                material == Material.DIAMOND_AXE || material == Material.DIAMOND_SHOVEL ||
                material == Material.DIAMOND_HOE || material == Material.NETHERITE_SWORD ||
                material == Material.NETHERITE_PICKAXE || material == Material.NETHERITE_AXE ||
                material == Material.NETHERITE_SHOVEL || material == Material.NETHERITE_HOE;
    }

}