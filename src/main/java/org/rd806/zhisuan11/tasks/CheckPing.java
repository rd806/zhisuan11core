package org.rd806.zhisuan11.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.rd806.zhisuan11.Zhisuan11core;

public class CheckPing {
    private BukkitTask check;

    public void sendTask() {
        if (check != null) {
            check.cancel();
        }

        check = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkPing(player);
                }
            }
        }.runTaskTimer(Zhisuan11core.main, 0L, 20L * 60);
    }

    private void checkPing(Player player) {
        if (player.getPing() > 300) {
            player.sendMessage("您的延迟较大，请检查网络连接……");
        }
    }
}
