package com.cube;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            ItemStack itemInHand = player.getInventory().getItemInMainHand(); // 플레이어가 들고 있는 아이템

            if (itemInHand != null && itemInHand.hasItemMeta()) {
                ItemMeta meta = itemInHand.getItemMeta();
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (String loreEntry : lore) {
                        // 옵션 문자열 분석
                        Pattern pattern = Pattern.compile("닭 처치 시 (\\d+)% 확률로 전리품 (\\d+)개 추가 획득");
                        Matcher matcher = pattern.matcher(loreEntry);
                        if (matcher.find() && event.getEntityType() == EntityType.CHICKEN) {
                            int chance = Integer.parseInt(matcher.group(1));
                            int lootAmount = Integer.parseInt(matcher.group(2));
                            Random random = new Random();
                            int randomChance = random.nextInt(100) + 1;
                            if (randomChance <= chance) {
                                ItemStack additionalLoot = new ItemStack(Material.FEATHER, lootAmount); // 추가 전리품
                                event.getDrops().add(additionalLoot);
                            }
                        }
                    }
                }
            }
        }
    }
}