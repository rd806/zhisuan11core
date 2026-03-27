package org.rd806.zhisuan11.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.rd806.zhisuan11.core.JoinItem;
import org.rd806.zhisuan11.Zhisuan11core;


public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {

        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        //   /zs help
        if (args[0].equals("help")) {
            sender.sendMessage(ChatColor.DARK_GRAY + "插件帮助");
            sender.sendMessage(ChatColor.BLUE + "/zs info   - 插件信息");
            sender.sendMessage(ChatColor.BLUE + "/zs reload - 重载插件配置文件");
            return true;
        }

        //   /zs info
        if (args[0].equals("info")) {
            sender.sendMessage(ChatColor.DARK_GRAY + "=============================================");
            sender.sendMessage(ChatColor.BLUE + "- 本插件为天津大学2024级智算11班服务器插件");
            sender.sendMessage(ChatColor.BLUE + "- 如遇bug请至Github或\"11班MC\"微信群提交");
            sender.sendMessage(ChatColor.BLUE + "- Github仓库：https://github.com/5882886/My-Minecraft-Server");
            sender.sendMessage(ChatColor.DARK_GRAY + "=============================================");
            return true;
        }

        //  /zs broadcast ...
        if (args[0].equals("broadcast")) {
            //  /zs broadcast send
            if (args[1].equals("send")) {
                Zhisuan11core.main.broadcast.SendBroadcast();
                return true;
            }
        }

        //  /zs menu ...
        if (args[0].equals("menu")) {
            if (player == null) return false;
            //  /zs menu main
            if (args[1].equals("main")) {
                Inventory MainMenu = Zhisuan11core.main.gameMenu.createMainMenu(player);
                Zhisuan11core.main.gameMenu.openMenu(player, MainMenu);
                return true;
            }
        }

        //  /zs setspawn
        if (args[0].equals("setspawn")) {
            if (player != null) {
                Location location = player.getLocation();
                player.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                player.sendMessage("已将您所在的位置设为此世界出生点");
                return true;
            }
        }

        //  /zs kit ...
        if (args[0].equals("kit")) {
            Player targetPlayer;
            // /zs kit targetPlayer
            if (args.length == 2) {
                if (player != null) {
                    targetPlayer = player.getServer().getPlayer(args[1]);
                } else {
                    sender.sendMessage("目标玩家不存在！");
                    return false;
                }
            } else {
                targetPlayer = player;
            }
            // JoinItem 不是静态类，需要先创建实例再调用
            JoinItem joinItem = new JoinItem();
            joinItem.giveFirstJoinItems(targetPlayer);
            return true;
        }

        //  /zs cleardropitems ...
        if (args[0].equals("cleardropitems")) {
            if (args.length == 2) {
                //  /zs cleardropitems <worldName>
                String worldName = args[1];
                if (Zhisuan11core.main.clearDropItems.clearSpecificWorlds(worldName)) {
                    return true;
                } else {
                    sender.sendMessage("世界不存在或未启用！");
                    return false;
                }
            }
            Zhisuan11core.main.clearDropItems.clearAllWorlds();
            return true;
        }

        //  /zs reload
        if (args[0].equals("reload")) {
            sender.sendMessage(ChatColor.BLUE + "[zhisuan11core] 正在重载插件配置……");
            Zhisuan11core.main.reloadConfig();
            Zhisuan11core.main.severTask.InitialTasks();
            sender.sendMessage(ChatColor.BLUE + "[zhisuan11core] 配置文件重载完成！");
            return true;
        }

        sender.sendMessage("欢迎使用zhisuan11core MC服务器插件！");
        return false;
    }
}
