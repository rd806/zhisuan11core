package org.rd806.zhisuan11.core;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rd806.zhisuan11.Zhisuan11core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JoinItem implements Listener {

    @EventHandler

    // 初始物品套装
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // 检查是否为第一次登录
        if (!player.hasPlayedBefore()) {
            giveFirstJoinItems(player);
        }
    }

    public void giveFirstJoinItems(Player player) {
        // 获取first-join-item的节点内容并返回一个列表itemsConfig
        // List<?> 代表列表中包含多种数据类型
        List<?> itemsConfig = Zhisuan11core.main.getConfig().getList("FirstJoinItems");
        giveItems(player, itemsConfig);
    }

    // 给予物品
    private void giveItems(Player player, List<?> itemsConfig) {
        // 空情况判断
        if (itemsConfig == null || itemsConfig.isEmpty()) {
            player.sendMessage("初始物品为空！");
            return;
        }
        List<ItemStack> itemsToGive = new ArrayList<>();
        // 遍历配置中的物品
        for (Object itemObj : itemsConfig) {
            @SuppressWarnings("unchecked")
            Map<String, Object> itemMap = (Map<String, Object>) itemObj;
            // 用 Map 格式创建物品
            ItemStack item = createItemFromMap(itemMap);
            if (item != null) {
                itemsToGive.add(item);
            }
        }

        // 加入物品至玩家物品栏
        if (!itemsToGive.isEmpty()) {
            for (ItemStack item : itemsToGive) {
                // 先尝试添加到背包，如果背包满了就掉落在地上
                Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

                // 如果背包满了，物品掉落在地上
                for (ItemStack leftover : leftovers.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), leftover);
                }
            }
            // 发送消息
            String message = Zhisuan11core.main.getConfig().getString("messages.first-join-item-given", "&a初始物品已发放，请查收！");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        else {
            Zhisuan11core.main.getLogger().warning("初始物品组为空，请检查配置文件！");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "物品配置有误，请联系管理员。"));
        }
    }

    private ItemStack createItemFromMap(Map<String, Object> itemMap) {
        try {
            // 获取材质
            Object materialObj = itemMap.get("material");
            if (materialObj == null) {
                Zhisuan11core.main.getLogger().warning("Map物品配置缺少 material 字段！");
                return null;
            }

            String materialName = materialObj.toString();
            Material material = Material.matchMaterial(materialName.toUpperCase());
            if (material == null) {
                Zhisuan11core.main.getLogger().warning("Map物品 material 存在非法字段：" + materialName);
                return null;
            }

            // 获取数量
            int amount = 1;
            Object amountObj = itemMap.get("amount");
            if (amountObj != null) {
                if (amountObj instanceof Integer) {
                    amount = (Integer) amountObj;
                } else if (amountObj instanceof String) {
                    try {
                        amount = Integer.parseInt((String) amountObj);
                    } catch (NumberFormatException e) {
                        Zhisuan11core.main.getLogger().warning("物品数量格式错误: " + amountObj);
                    }
                } else if (amountObj instanceof Number) {
                    amount = ((Number) amountObj).intValue();
                }
            }

            // 创建物品
            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                // 设置物品名称
                Object nameObj = itemMap.get("name");
                if (nameObj != null) {
                    String name = nameObj.toString();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                }

                // 设置物品Lore
                Object loreObj = itemMap.get("lore");
                if (loreObj != null) {
                    List<String> lore = new ArrayList<>();

                    if (loreObj instanceof List<?> loreList) {
                        // 如果lore是列表格式
                        for (Object lineObj : loreList) {
                            if (lineObj != null) {
                                lore.add(ChatColor.translateAlternateColorCodes('&', lineObj.toString()));
                            }
                        }
                    } else if (loreObj instanceof String) {
                        // 如果lore是单行字符串
                        lore.add(ChatColor.translateAlternateColorCodes('&', loreObj.toString()));
                    }

                    if (!lore.isEmpty()) {
                        meta.setLore(lore);
                    }
                }

                item.setItemMeta(meta);
            }

            return item;
        } catch (Exception e) {
            Zhisuan11core.main.getLogger().warning("从Map创建物品时发生错误: " + e.getMessage());
            return null;
        }
    }
}
