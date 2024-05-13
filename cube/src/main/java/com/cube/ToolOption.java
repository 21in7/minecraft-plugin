package com.cube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;



public class ToolOption {
    public static final List<String> COMMON_OPTIONS = Arrays.asList(
        "닭 처치 시 %d~%d%% 확률로 전리품 %d~%d개 추가 획득",
        "토끼 처치 시 %d~%d%% 확률로 전리품 %d~%d개 추가 획득",
        "돼지 처치 시 %d~%d%% 확률로 전리품 %d~%d개 추가 획득",
        "양 처치 시 %d~%d%% 확률로 전리품 %d~%d개 추가 획득",
        "소 처치 시 %d~%d%% 확률로 전리품 %d~%d개 추가 획득",
        "몬스터 처치 시 %d~%d%% 확률로 전리품 %d~%d개 추가 획득",
        "농작물 수확 시 %d~%d%% 확률로 뼛가루 %d~%d개 드랍",
        "경험치 획득 시 %d~%d%% 확률로 획득량 %d~%d 증가"
    );
    public static final Map<Material, List<String>> SPECIAL_OPTIONS = new HashMap<>();

    static {
        SPECIAL_OPTIONS.put(Material.DIAMOND_PICKAXE, Arrays.asList(
            "[곡괭이] 광물 블록 채굴 시 주괴(구워진) 상태로 드랍",
            "[곡괭이] 블록 채굴 시 [상·하] 1칸의 블럭 동시 채굴",
            "[곡괭이] 블록 채굴 시 [상·하·좌·우] 1칸의 블럭 동시 채굴",
            "[곡괭이] 광물 블록을 채굴 할때 %d~%d%% 확률로 %d~%d등급 광물 드랍"
        ));
        SPECIAL_OPTIONS.put(Material.DIAMOND_HOE, Arrays.asList(
            "[괭이] 농작물 수확 할때 %d~%d%% 확률로 작물 %d~%d개 추가 획득",
            "[괭이] 농작물 수확 시 %d~%d%% 확률로 씨앗 %d~%d개 추가 획득"
        ));
        SPECIAL_OPTIONS.put(Material.DIAMOND_SHOVEL, Arrays.asList(
            "[삽] 모래 블록 채굴 시 유리 블록으로 드랍"
        ));
    }
    static {
        SPECIAL_OPTIONS.put(Material.NETHERITE_PICKAXE, Arrays.asList(
            "[곡괭이] 광물 블록 채굴 시 주괴(구워진) 상태로 드랍",
            "[곡괭이] 블록 채굴 시 [상·하] 1칸의 블럭 동시 채굴",
            "[곡괭이] 블록 채굴 시 [상·하·좌·우] 1칸의 블럭 동시 채굴",
            "[곡괭이] 광물 블록을 채굴 할때 %d~%d%% 확률로 %d~%d등급 광물 드랍"
        ));
        SPECIAL_OPTIONS.put(Material.NETHERITE_HOE, Arrays.asList(
            "[괭이] 농작물 수확 할때 %d~%d%% 확률로 작물 %d~%d개 추가 획득",
            "[괭이] 농작물 수확 시 %d~%d%% 확률로 씨앗 %d~%d개 추가 획득"
        ));
        SPECIAL_OPTIONS.put(Material.NETHERITE_SHOVEL, Arrays.asList(
            "[삽] 모래 블록 채굴 시 유리 블록으로 드랍"
        ));
    }

            // OptionValues 클래스 정의
    public static class OptionValues {
        int minChance;
        int maxChance;
        int minLoot;
        int maxLoot;


        public OptionValues(int minChance, int maxChance, int minLoot, int maxLoot) {
            this.minChance = minChance;
            this.maxChance = maxChance;
            this.minLoot = minLoot;
            this.maxLoot = maxLoot;

        }
    }

    // 아이템의 lore에서 옵션 정보를 추출하는 메소드
    public static OptionValues parseOptionString(List<String> lore) {
        Pattern pickaxePattern = Pattern.compile("\\[곡괭이\\] 광물 블록을 채굴 할때 (\\d+)% 확률로 (\\d+)등급 광물 드랍");
        Pattern hoePattern = Pattern.compile("\\[괭이\\] 농작물 수확 할때 (\\d+)% 확률로 작물 (\\d+)개 추가 획득");
        for (String loreEntry : lore) {
            //Bukkit.getLogger().info("처리 중인 lore 항목: " + loreEntry);
            Matcher pickaxeMatcher = pickaxePattern.matcher(loreEntry);
            Matcher hoeMatcher = hoePattern.matcher(loreEntry);
            //Bukkit.getLogger().info("정규 표현식 매칭 시도: " + loreEntry);
            if (pickaxeMatcher.find()) {
                int chance = Integer.parseInt(pickaxeMatcher.group(1));
                int lootGrade = Integer.parseInt(pickaxeMatcher.group(2));
                //Bukkit.getLogger().info(String.format("파싱된 옵션 - 확률: %d, 등급: %d", chance, lootGrade));
                return new OptionValues(chance, chance, lootGrade, lootGrade);
            } else if (hoeMatcher.find()){
                int chance = Integer.parseInt(hoeMatcher.group(1));
                int lootGrade = Integer.parseInt(hoeMatcher.group(2));
                //Bukkit.getLogger().info(String.format("[Cube] 파싱된 옵션 - 확률: %d, 등급: %d", chance, lootGrade));
                return new OptionValues(chance, chance, chance, lootGrade);
            }else {
                //Bukkit.getLogger().info("매칭 실패: " + loreEntry);
            }
        }
        //Bukkit.getLogger().info("옵션 정보가 lore에서 발견되지 않음");
        return null; // 옵션 정보가 없는 경우 null 반환
    }
 
        // 옵션 문자열을 실제 값으로 채워서 반환하는 메소드
        public static String getRandomOption() {
            Random random = new Random();
            String template; // template 변수 선언

            // COMMON_OPTIONS, SPECIAL_OPTIONS 중 하나를 랜덤하게 선택
            if (random.nextBoolean()) {
                // COMMON_OPTIONS에서 랜덤하게 선택
                int index = random.nextInt(COMMON_OPTIONS.size());
                template = COMMON_OPTIONS.get(index);
            } else {
                // SPECIAL_OPTIONS에서 랜덤하게 선택
                List<Material> materials = new ArrayList<>(SPECIAL_OPTIONS.keySet());
                Material selectedMaterial = materials.get(random.nextInt(materials.size()));
                List<String> options = SPECIAL_OPTIONS.get(selectedMaterial);
                int index = random.nextInt(options.size());
                template = options.get(index);
            }

            // 확률 범위와 전리품 개수 범위를 정의합니다.
            int minChance = 10; // 최소 확률
            int maxChance = 50; // 최대 확률
            int minLoot = 1; // 최소 전리품 개수
            int maxLoot = 3; // 최대 전리품 개수

            // 랜덤 확률과 전리품 개수를 선택합니다.
            int randomChance = minChance + random.nextInt(maxChance - minChance + 1);
            int randomLoot = minLoot + random.nextInt(maxLoot - minLoot + 1);

            // 옵션 문자열 생성 (실제 인 게임상 적용되는 문자열)
            String option = template.replaceAll("%d~%d", "%d");
            option = String.format(option, randomChance, randomLoot);
            return option;
        }
        public static OptionValues parseFinalOptionString(String option) {
            Pattern pattern = Pattern.compile("\\[곡괭이\\] 광물 블록을 채굴 할때 (\\d+)% 확률로 (\\d+)등급 광물 드랍");
            Matcher matcher = pattern.matcher(option);
            if (matcher.find()) {
                int randomChance = Integer.parseInt(matcher.group(1));
                int randomLoot = Integer.parseInt(matcher.group(2));
                // 여기서는 확률과 전리품 개수가 이미 결정된 상태이므로, min과 max가 동일한 값을 가집니다.
                return new OptionValues(randomChance, randomChance, randomLoot, randomLoot);
            }
            return null; // 옵션 정보가 없는 경우 null 반환
        }
        /**
        * 주어진 아이템 스택에 특정 옵션이 있는지 확인합니다.
        * 
        * @param itemStack 검사할 아이템 스택
        * @param option 확인할 옵션 문자열
        * @return 아이템에 옵션이 있으면 true, 그렇지 않으면 false
        */
        public static boolean hasSpecialOption(ItemStack itemStack, String option) {
            if (itemStack != null && itemStack.hasItemMeta()) {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (String loreEntry : lore) {
                        if (loreEntry.contains(option)) {
                            return true;
                        }
                        // 정규 표현식을 사용하여 동적 옵션 문자열 확인
                        Pattern pickaxePattern = Pattern.compile("\\[곡괭이\\] 광물 블록을 채굴 할때 (\\d+)% 확률로 (\\d+)등급 광물 드랍");
                        Pattern hoePattern = Pattern.compile("\\[괭이\\] 농작물 수확 할때 (\\d+)% 확률로 작물 (\\d+)개 추가 획득");
                        Matcher pickaxeMatcher = pickaxePattern.matcher(loreEntry);
                        Matcher hoeMatcher = hoePattern.matcher(loreEntry);
                        if (pickaxeMatcher.find()) {
                            // 정규 표현식에 매칭되는 경우, 옵션이 존재하는 것으로 간주
                            return true;
                        } else if (hoeMatcher.find()) {
                            return true;
                        }
                    }
                }
            }
            return false; // 아이템에 옵션이 없는 경우 false 반환
        }
}