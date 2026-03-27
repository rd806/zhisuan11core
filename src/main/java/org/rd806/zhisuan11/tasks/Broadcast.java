package org.rd806.zhisuan11.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.rd806.zhisuan11.Zhisuan11core;

import java.util.List;

public class Broadcast {
    private String enable;
    private int interval;
    private BukkitTask send;

    public Broadcast() {
        enable = "true";
        interval = 900;
    }

    public void sendTask() {
        if (send != null) {
            send.cancel();
        }

        send = new BukkitRunnable() {
            @Override
            public void run() {
                SendBroadcast();
            }
        }.runTaskTimer(Zhisuan11core.main, 20L * 15, 20L * interval);
    }


    public void SendBroadcast() {
        enable = Zhisuan11core.main.getConfig().getString("Broadcast.enabled", "true");

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            if (enable.equals("true")) {
                interval = Zhisuan11core.main.getConfig().getInt("Broadcast.interval", 900);
                List<String> announcement = Zhisuan11core.main.getConfig().getStringList("Broadcast.content");
                for (String message : announcement) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                    Zhisuan11core.main.getServer().broadcastMessage(message);
                }
            } else {
                Zhisuan11core.main.getLogger().warning("未启用公告功能！");
            }
        } else {
            Zhisuan11core.main.getLogger().info("当前服务器无玩家在线，暂停公告发送！");
        }
    }
}
