package org.rd806.zhisuan11.core;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.rd806.zhisuan11.Zhisuan11core;

import java.util.Objects;

// 玩家重生逻辑
public class Respawn implements Listener {

    FileConfiguration config = Zhisuan11core.main.getConfig();

    @EventHandler
    public void spawn(PlayerRespawnEvent event) {
        if (Objects.requireNonNull(config.getString("Spawn.WorldSpawn")).equals("true")) {
            Player player = event.getPlayer();
            // 获取世界出生点
            Location spawnLocation = player.getWorld().getSpawnLocation();
            // 传送玩家到出生点
            player.teleport(spawnLocation);
            player.sendMessage("已在世界出生点重生！");
        }
    }
}
