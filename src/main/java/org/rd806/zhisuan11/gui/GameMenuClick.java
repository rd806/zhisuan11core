package org.rd806.zhisuan11.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.rd806.zhisuan11.Zhisuan11core;

public class GameMenuClick implements Listener {

    @EventHandler
    // 菜单点击事件
    public void MenuClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        InventoryView view = event.getView();

        // 获取点击物品
        ItemStack clicked = event.getCurrentItem();

        // 点击为空，返回
        if (clicked == null) return;

        if (view.getTitle().equals("主菜单")) {
            // 防止玩家移动物品
            event.setCancelled(true);
            switch (clicked.getType()) {
                case BOOK:
                    Inventory ServerRule = Zhisuan11core.main.gameMenu.createServerRule(player);
                    Zhisuan11core.main.gameMenu.closeMenu(player);
                    Zhisuan11core.main.gameMenu.openMenu(player, ServerRule);
                    break;
                case COMPASS:
                    player.teleport(player.getWorld().getSpawnLocation());
                    player.sendMessage("已传送至世界出生点");
                    break;
                case REDSTONE_BLOCK:
                    player.sendMessage("插件作者：RunicDolphin806");
                    player.sendMessage("仓库地址：https://github.com/5882886/My-Minecraft-Server");
                    Zhisuan11core.main.gameMenu.closeMenu(player);
                    break;
                default:
                    break;
            }
        } else if (view.getTitle().equals("服务器守则")) {
            // 防止玩家移动物品
            event.setCancelled(true);
            switch (clicked.getType()) {
                case BARRIER:
                    Zhisuan11core.main.gameMenu.closeMenu(player);
                    Inventory MainMenu = Zhisuan11core.main.gameMenu.createMainMenu(player);
                    Zhisuan11core.main.gameMenu.openMenu(player, MainMenu);
                    break;
                case REDSTONE_BLOCK:
                    player.sendMessage("插件作者：RunicDolphin806");
                    player.sendMessage("仓库地址：https://github.com/5882886/My-Minecraft-Server");
                    Zhisuan11core.main.gameMenu.closeMenu(player);
                    break;
                default:
                    break;
            }
        }
    }
}
