package org.rd806.zhisuan11.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMenu {

    private final Inventory MainMenu;
    private final Inventory ServerRule;

    public GameMenu() {
        MainMenu = Bukkit.createInventory(null, 27, "主菜单");
        ServerRule = Bukkit.createInventory(null, 27, "服务器守则");
    }


    // 主菜单界面
    public Inventory createMainMenu(Player player) {
        // 服务器守则
        AddItemToMenu(MainMenu, 0, Material.BOOK, "服务器守则", "§7点击查看服务器守则");
        // 回城按钮
        AddItemToMenu(MainMenu, 1, Material.COMPASS, "回城按钮", "§7点击回到世界出生点");

        // 玩家信息（左下角）
        MainMenu.setItem(18, createPlayerHead(player));
        // 插件信息（右下角）
        AddItemToMenu(MainMenu, 26, Material.REDSTONE_BLOCK, "关于插件", "点击访问插件仓库");

        return MainMenu;
    }

    // 服务器守则界面
    public Inventory createServerRule(Player player) {
        // 服务器守则内容
        AddItemToMenu(ServerRule, 0, Material.PAPER, "守则",
                "----------",
                "遵守法律法规",
                "禁止炸服等恶意破坏服务器的行为！");
        // 玩家信息（左下角）
        ServerRule.setItem(18, createPlayerHead(player));
        // 返回按钮（底部居中）
        AddItemToMenu(ServerRule, 22, Material.BARRIER, "返回");
        // 插件信息（右下角）
        AddItemToMenu(ServerRule, 26, Material.REDSTONE_BLOCK, "关于插件", "点击访问插件仓库");

        return ServerRule;
    }

    // 向菜单中添加物品
    // String... 类型可以一次性添加多个字符串，但必须作为最后一个参数
    private void AddItemToMenu(Inventory menu, int slot, Material material, String itemName, String... itemLore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(itemName);
            // 追加lore内容
            List<String> lore = itemMeta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.addAll(Arrays.asList(itemLore));
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        menu.setItem(slot, itemStack);
    }

    // 创建玩家头像
    private ItemStack createPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();

        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skullMeta.setDisplayName(player.getName());
            skullMeta.setLore(Arrays.asList(
                "§7点击查看您的玩家信息",
                "",
                "§e玩家: §f" + player.getName(),
                "§e等级: §f" + player.getLevel(),
                "§e生命值: §f" + String.format("%.1f", player.getHealth()) + "❤"
            ));
            head.setItemMeta(skullMeta);
        }
        return head;
    }


    // 打开主菜单
    public void openMenu(Player player, Inventory menu) {
        player.openInventory(menu);
    }
    // 关闭主菜单
    public void closeMenu(Player player) {
        player.closeInventory();
    }

}
