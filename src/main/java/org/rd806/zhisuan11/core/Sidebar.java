package org.rd806.zhisuan11.core;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.rd806.zhisuan11.Zhisuan11core;

import java.util.Objects;

public class Sidebar {

    private BukkitTask update;
    private final static long lastTick = System.currentTimeMillis();

    // 实时更新侧边栏
    public void startUpdate() {
        if (update != null) {
            update.cancel();
        }

        update = new BukkitRunnable() {
            @Override
            // 创建调用接口
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerSidebar(player);
                }
            }
        }.runTaskTimer(Zhisuan11core.main, 0L, 20L);

    }

    // 更新侧边栏
    private void PlayerSidebar(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("ServerInfo");

        // 如果玩家没有记分板，重新创建
        if (objective == null) {
            createSidebar(player);
            return;
        }
        // 清空旧内容（通过取消注册并重新创建）
        objective.unregister();
        updateSidebar(scoreboard, player);
    }

    // 创建新侧边栏
    private void createSidebar(Player player) {
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();

        updateSidebar(scoreboard, player);
        // 玩家显示侧边栏
        player.setScoreboard(scoreboard);
    }

    // 侧边栏内容封装函数
    private void setSidebar(Objective objective, String text, int score) {
        Score scoreEntry = objective.getScore(text);
        scoreEntry.setScore(score);
    }

    // 侧边栏生成
    private void updateSidebar(Scoreboard scoreboard, Player player) {
        Objective objective = scoreboard.registerNewObjective(
                "ServerInfo",
                Criteria.DUMMY,
                Zhisuan11core.main.getConfig().getString("Sidebar.title", "§6§l服务器信息")
        );
        // 设置显示位置
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setSidebar(objective, "§9", 9);
        setSidebar(objective, "§7欢迎，§a" + player.getName() + " ！", 8);
        setSidebar(objective, "§7", 7);
        setSidebar(objective, "§7在线玩家: §a" + Bukkit.getOnlinePlayers().size(), 6);
        setSidebar(objective, "§5", 5);
        setSidebar(objective, "§7服务器TPS: " + getAllTPS(), 4);
        setSidebar(objective, "§3", 3);
        setSidebar(objective, "§7您的延迟: §a" + player.getPing() + "ms", 2);
        setSidebar(objective, "§1", 1);
        setSidebar(objective, Zhisuan11core.main.getConfig().getString("Sidebar.ip","www.example.com"), 0);
    }

    // 获取服务器 TPS
    private String getAllTPS() {
        try {
            // 仅在Paper服务端生效
            Server server = Bukkit.getServer();
            double[] tps = (double[]) server.getClass()
                    .getMethod("getTPS")
                    .invoke(server);
            double TPSResult = (tps[0] + tps[1] + tps[2]) / 3;
            return String.format("%.1f", TPSResult);
        } catch (Exception e) {
            double tps;
            long now = System.currentTimeMillis();
            long timeDiff = now - lastTick;

            // 每 tick 应该是 50ms，计算实际 TPS
            tps = Math.min(20.0, 1000.0 / (timeDiff / 50.0));

            return String.format("%.1f", tps);
        }
    }
}
