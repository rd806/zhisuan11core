package org.rd806.zhisuan11.core;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.rd806.zhisuan11.Zhisuan11core;

import java.util.List;

public class JoinInfo implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // 获取玩家名称信息
        var player = event.getPlayer();
        String playerName = player.getName();

        // 向加入的玩家发送一个标题
        String title = Zhisuan11core.main.getConfig().getString("JoinTitle.title", "&b欢迎来到服务器！");
        String subtitle = Zhisuan11core.main.getConfig().getString("JoinTitle.subtitle", "&b祝您游玩愉快!");

        // 解析MC的颜色代码
        title = ChatColor.translateAlternateColorCodes('&', title);
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);

        int fadeIn = Zhisuan11core.main.getConfig().getInt("JoinTitle.fade-in", 1);
        int stay = Zhisuan11core.main.getConfig().getInt("JoinTitle.stay", 5);
        int fadeOut = Zhisuan11core.main.getConfig().getInt("JoinTitle.fade-out", 1);

        player.sendTitle(title, subtitle, fadeIn * 20, stay * 20, fadeOut * 20);


        // 向加入的玩家发送信息
        // 创建信息列表
        List<String> joinMessages = Zhisuan11core.main.getConfig().getStringList("JoinMessage");

        for (String message : joinMessages) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            player.sendMessage(message.replace("%player%", playerName));
        }
    }
}
