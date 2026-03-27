package org.rd806.zhisuan11.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.rd806.zhisuan11.Zhisuan11core;

import java.util.ArrayList;
import java.util.List;

public class ClearDropItems {

    private String enable;
    private int interval;
    private List<Integer> warnings;
    private String message;
    private String warning;
    private BukkitTask clear;

    public ClearDropItems() {
        enable = "false";
        interval = 900;
        warnings = new ArrayList<>();
    }

    int secondsLeft = interval;

    public void startClear() {
        if (clear != null) {
            clear.cancel();
        }

        clear = new BukkitRunnable() {
            @Override
            public void run() {
                for (int warning : warnings) {
                    if (secondsLeft == warning) {
                        broadcastWarning(warning);
                        break;
                    }
                }
                // 每秒执行一次
                secondsLeft--;

                if (secondsLeft <= 0) {
                    clearAllWorlds();
                    // 重置清理间隔
                    secondsLeft = interval;
                }
            }
        }.runTaskTimer(Zhisuan11core.main, 0L ,20L);
    }

    // 初始化配置
    public void loadConfig() {
        enable = Zhisuan11core.main.getConfig().getString("ClearDropItems.enabled", "false");
        interval = Zhisuan11core.main.getConfig().getInt("ClearDropItems.interval", 900);
        // 设置警告时间点
        warnings = Zhisuan11core.main.getConfig().getIntegerList("ClearDropItems.warnings");
        if (warnings.isEmpty()) {
            warnings.add(60);
            warnings.add(30);
            warnings.add(10);
        }

        message = Zhisuan11core.main.getConfig().getString("ClearDropItems.message",
                "&a[zhisuan11core] &e已清理&b %count% &e个掉落物！");
        warning = Zhisuan11core.main.getConfig().getString("ClearDropItems.warning",
                "&e[zhisuan11core] &c警告！&e掉落物将在&b %seconds% &e秒后清理！");
    }

    // 清理所有世界的掉落物
    public void clearAllWorlds() {
        if (enable.equals("true")) {
            int count = 0;
            for (World world : Bukkit.getWorlds()) {
                for (Item item : world.getEntitiesByClass(Item.class)) {
                    count ++;
                    item.remove();
                }
            }
            String broadcastMsg = message.replace("%count%", String.valueOf(count));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMsg));
            Zhisuan11core.main.getLogger().info("清理掉落物成功！");
        }
    }

    // 清理指定世界的掉落物
    public boolean clearSpecificWorlds(String worldName) {
        if (enable.equals("true")) {
            int count = 0;
            World world = Bukkit.getWorld(worldName);
            if (world == null) return false;

            for (Item item : world.getEntitiesByClass(Item.class)) {
                count ++;
                item.remove();
            }
            String broadcastMsg = message.replace("%count%", String.valueOf(count));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMsg));
            return true;
        }
        return false;
    }

    // 清理前预警
    private void broadcastWarning(int seconds) {
        String broadcastMsg = warning.replace("%seconds%", String.valueOf(seconds));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMsg));
    }

}
