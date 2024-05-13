package com.cube;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cube.ToolOption.OptionValues;

import org.bukkit.entity.Player;


public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        int lootingLevel = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        if (itemInHand.hasItemMeta()) { // 아이템에 메타 데이터가 있는지 확인
            ItemMeta meta = itemInHand.getItemMeta(); // 아이템의 메타 데이터 가져오기
            if (meta.hasLore()) { // 아이템 메타 데이터에 lore가 있는지 확인
                List<String> loreList = meta.getLore(); // lore 리스트 가져오기
                //Bukkit.getLogger().info("Lore 리스트 내용:"); // 로그에 표시할 메시지
                for (String loreItem : loreList) { // lore 리스트의 각 항목에 대해
                    //Bukkit.getLogger().info(loreItem); // 로그에 lore 항목 출력
                }
            } else {
                //Bukkit.getLogger().info("이 아이템에는 lore가 설정되어 있지 않습니다."); // lore가 없는 경우 메시지 출력
            }
        } else {
            //Bukkit.getLogger().info("이 아이템에는 ItemMeta가 없습니다."); // ItemMeta가 없는 경우 메시지 출력
        }

        // 모래 블록을 채굴했는지 확인
        if (event.getBlock().getType() == Material.SAND) {
            // 플레이어가 삽을 들고 있는지 확인
            if (itemInHand.getType() == Material.NETHERITE_SHOVEL || itemInHand.getType() == Material.DIAMOND_SHOVEL) {
                // 특별한 옵션이 있는지 확인
                if (ToolOption.hasSpecialOption(itemInHand, "[삽] 모래 블록 채굴 시 유리 블록으로 드랍")) {
                    // 이벤트 취소 및 유리 블록 드랍
                    event.setCancelled(true);
                    // 모래 블록 위치에 유리 블록 드랍
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GLASS));
                    // 모래 블록을 공기로 변경하여 제거
                    event.getBlock().setType(Material.AIR);
                }
            }
        }
        boolean optionApplied = false;
        boolean hasSilkTouch = itemInHand.containsEnchantment(Enchantment.SILK_TOUCH);
        //Bukkit.getLogger().info("섬세한 손길 인챈트 적용 여부 : " + hasSilkTouch);
        if (!hasSilkTouch){
            if (ToolOption.hasSpecialOption(itemInHand, "[곡괭이] 광물 블록을 채굴 할때")) {
                if (itemInHand.getType() == Material.DIAMOND_PICKAXE || itemInHand.getType() == Material.NETHERITE_PICKAXE) {
                    Material blockType = event.getBlock().getType();
                    if (isOre(blockType)){
                        OptionValues optionValues = ToolOption.parseOptionString(itemInHand.getItemMeta().getLore());
                        //Bukkit.getLogger().info("옵션 파싱 결과: " + optionValues);
                        if (optionValues != null) {
                            Random random = new Random();
                            if (random.nextInt(100) < optionValues.minChance) {
                                List<Material> dropMaterials = getDropMaterialByGrade(optionValues.minLoot, optionValues.maxLoot);
                                if (!dropMaterials.isEmpty()) {
                                    // 옵션 정보가 존재하면, 등급에 따른 광물 드랍 로직
                                    Material dropMaterial = dropMaterials.get(random.nextInt(dropMaterials.size()));
                                    //Bukkit.getLogger().info("드랍될 아이템: " + dropMaterial);
                                    //Bukkit.getLogger().info("minLoot :" + optionValues.minLoot);
                                    //Bukkit.getLogger().info("maxLoot: " + optionValues.maxLoot);
                                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(dropMaterial));
                                    optionApplied = true;
                                }
                            }
                            
                        }
                    }
                }
            } else if (ToolOption.hasSpecialOption(itemInHand, "[곡괭이] 광물 블록 채굴 시 주괴(구워진) 상태로 드랍")) {
                if (itemInHand.getType() == Material.DIAMOND_PICKAXE || itemInHand.getType() == Material.NETHERITE_PICKAXE) {
                    Material blockType = event.getBlock().getType();
                    ItemStack dropItem = null;
                    int amount = 1;
                    int experience = 0;
                
                    // 광물 블록에 따른 주괴(구워진) 상태 아이템 설정
                    switch (blockType) {
                        case IRON_ORE:
                            dropItem = new ItemStack(Material.IRON_INGOT);
                            experience = 1;
                            break;
                        case GOLD_ORE:
                            dropItem = new ItemStack(Material.GOLD_INGOT);
                            experience = 1;
                            break;
                        case COPPER_ORE:
                            dropItem = new ItemStack(Material.COPPER_INGOT);
                            experience = 1;
                            break;
                        case ANCIENT_DEBRIS:
                            dropItem = new ItemStack(Material.NETHERITE_SCRAP);
                            experience = 2;
                            break;
                        case DEEPSLATE_COPPER_ORE:
                            dropItem = new ItemStack(Material.COPPER_INGOT);
                            experience = 1;
                            break;
                        case DEEPSLATE_IRON_ORE:
                            dropItem = new ItemStack(Material.IRON_INGOT);
                            experience = 1;
                            break;
                        case DEEPSLATE_GOLD_ORE:
                            dropItem = new ItemStack(Material.GOLD_INGOT);
                            experience = 1;
                            break;
                        default:
                            return;
                    }
                    if (dropItem != null) {
                        // 행운 인챈트 레벨에 따라 드랍 수량 조정
                        amount += new Random().nextInt(lootingLevel + 1);
                        dropItem.setAmount(amount);
                        // 기본 드랍 아이템 취소
                        event.setDropItems(false);
                        // 주괴(구워진) 상태로 아이템 드랍
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), dropItem);
                        event.getBlock().getWorld().spawn(event.getBlock().getLocation(), org.bukkit.entity.ExperienceOrb.class).setExperience(experience);
                    }
                    optionApplied = true;
                }
            } else if (!optionApplied && ToolOption.hasSpecialOption(itemInHand, "[곡괭이] 블록 채굴 시 [상·하] 1칸의 블럭 동시 채굴")) {
                if (itemInHand.getType() == Material.DIAMOND_PICKAXE || itemInHand.getType() == Material.NETHERITE_PICKAXE) {
                    breakAdjacentBlocks(block, player, "vertical");
                    optionApplied = true;
                } else {
                    //Bukkit.getLogger().info("[Cube] 이 옵션은 곡괭이에만 적용 됩니다.");
                }
            } else if (!optionApplied && ToolOption.hasSpecialOption(itemInHand, "[곡괭이] 블록 채굴 시 [상·하·좌·우] 1칸의 블럭 동시 채굴")) {
                if (itemInHand.getType() == Material.DIAMOND_PICKAXE || itemInHand.getType() == Material.NETHERITE_PICKAXE) {
                    breakAdjacentBlocks(block, player, "all");
                    optionApplied = true;
                } else {
                    //Bukkit.getLogger().info("[Cube] 이 옵션은 곡괭이에만 적용 됩니다.");
                }
            }
        } 
                // "[곡괭이] 블록 채굴 시 [상·하] 1칸의 블럭 동시 채굴" 옵션 확인
/*         if (!optionApplied && ToolOption.hasSpecialOption(itemInHand, "[곡괭이] 블록 채굴 시 [상·하] 1칸의 블럭 동시 채굴")) {
            if (itemInHand.getType() == Material.DIAMOND_PICKAXE || itemInHand.getType() == Material.NETHERITE_PICKAXE) {
                breakAdjacentBlocks(block, player, "vertical");
                optionApplied = true;
            } else {
                Bukkit.getLogger().info("[Cube] 이 옵션은 곡괭이에만 적용 됩니다.");
            }
        }

        // "[곡괭이] 블록 채굴 시 [상·하·좌·우] 1칸의 블럭 동시 채굴" 옵션 확인
        if (!optionApplied && ToolOption.hasSpecialOption(itemInHand, "[곡괭이] 블록 채굴 시 [상·하·좌·우] 1칸의 블럭 동시 채굴")) {
            if (itemInHand.getType() == Material.DIAMOND_PICKAXE || itemInHand.getType() == Material.NETHERITE_PICKAXE) {
                breakAdjacentBlocks(block, player, "all");
                optionApplied = true;
            } else {
                Bukkit.getLogger().info("[Cube] 이 옵션은 곡괭이에만 적용 됩니다.");
            }
        } */

    }

    private void breakAdjacentBlocks(Block block, Player player, String direction) {
        List<BlockFace> faces = new ArrayList<>();
    
        // "vertical" 옵션일 경우 상·하 방향 추가
        if ("vertical".equals(direction)) {
            faces.add(BlockFace.UP);
            faces.add(BlockFace.DOWN);
        }
        // "all" 옵션일 경우 상·하·좌·우 방향 추가
        else if ("all".equals(direction)) {
            faces.add(BlockFace.UP);
            faces.add(BlockFace.DOWN);
            // 플레이어의 방향을 기준으로 좌·우 방향을 결정
            BlockFace playerFacing = player.getFacing();
            switch (playerFacing) {
                case NORTH:
                case SOUTH:
                    faces.add(BlockFace.EAST);
                    faces.add(BlockFace.WEST);
                    break;
                case EAST:
                case WEST:
                    faces.add(BlockFace.NORTH);
                    faces.add(BlockFace.SOUTH);
                    break;
                default:
                    return;
            }
        }
    
        // 설정된 방향에 따라 인접 블록을 깨뜨림
        for (BlockFace face : faces) {
            Block adjacentBlock = block.getRelative(face);
            if (adjacentBlock.getType() != Material.AIR) {
                adjacentBlock.breakNaturally();
            }
        }
    }

    private List<Material> getDropMaterialByGrade(int minGrade, int maxGrade) {
        List<Material> materials = new ArrayList<>();
        switch (minGrade) {
            case 1:
                materials.add(Material.DIAMOND);
                materials.add(Material.EMERALD); // 1등급에 에메랄드 추가
                break;
            case 2:
                materials.add(Material.IRON_INGOT);
                materials.add(Material.GOLD_INGOT); // 2등급에 금 주괴 추가
                break;
            case 3:
                materials.add(Material.COAL);
                materials.add(Material.COPPER_INGOT); // 3등급에 구리 주괴 추가
                break;
            default:
            // 기본적으로 아무것도 추가하지 않음
            break;
        }
        return materials;
    }

    private boolean isOre(Material material) {
        switch (material) {
            case IRON_ORE:
            case GOLD_ORE:
            case COPPER_ORE:
            case ANCIENT_DEBRIS:
            case DEEPSLATE_COPPER_ORE:
            case DEEPSLATE_IRON_ORE:
            case DEEPSLATE_GOLD_ORE:
            case COAL_ORE:
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
            case EMERALD_ORE:
            case NETHER_QUARTZ_ORE:
            case REDSTONE_ORE:
                return true;
            default:
                return false;
        }
    }


}


    