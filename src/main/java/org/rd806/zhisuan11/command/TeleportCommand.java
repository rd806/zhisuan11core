package org.rd806.zhisuan11.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.rd806.zhisuan11.Zhisuan11core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

// 接管原版的 tp 指令
public class TeleportCommand implements CommandExecutor {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        try {
            Player sendPlayer = (Player) sender;
            Player targetPlayer;

            // 冷却检查
            UUID uuid = sendPlayer.getUniqueId();
            if (cooldowns.containsKey(uuid)) {
                // 30秒冷却
                long COOLDOWN_TIME = Zhisuan11core.main.getConfig().getLong("Teleport.Cooldown", 30) * 1000;
                long timeLeft = (cooldowns.get(uuid) + COOLDOWN_TIME) - System.currentTimeMillis();
                if (timeLeft > 0) {
                    sendPlayer.sendMessage("§c请等待 " + (timeLeft / 1000) + " 秒后再使用传送!");
                    return true;
                }
            }

            // /zs targetPlayer x y z
            if (args.length == 4) {
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                Location location = new Location(sendPlayer.getWorld(), x, y, z);
                //  /tp @s x y z
                if (args[0].equals("@s") || args[0].equals("@p")) {
                    sendPlayer.teleport(location);
                    sendPlayer.sendMessage("你已被传送至: " + x + ", " + y + ", " + z);
                }
                // /tp targetPlayer x y z
                else {
                    targetPlayer = getPlayer(args[0]);
                    if (targetPlayer != null) {
                        targetPlayer.teleport(location);
                        if (sendPlayer == targetPlayer) {
                            sendPlayer.sendMessage("已将" + targetPlayer.getName() + "传送至: " + x + ", " + y + ", " + z);
                        }
                        else {
                            targetPlayer.sendMessage(sendPlayer.getName() + "将你传送至：" + x + ", " + y + ", " + z);
                            sendPlayer.sendMessage("已将" + targetPlayer.getName() + "传送至: " + x + ", " + y + ", " + z);
                        }
                    }
                    else {
                        sendPlayer.sendMessage("目标玩家不存在！");
                    }
                }
            }

            // /tp targetPlayer1 targetPlayer2 将targetPlayer1传送至targetPlayer2
            else if (args.length == 2) {
                Player targetPlayer1 = getPlayer(args[0]);
                Player targetPlayer2 = getPlayer(args[1]);
                if (targetPlayer1 != null && targetPlayer2 != null) {
                    targetPlayer1.teleport(targetPlayer2);
                    sendPlayer.sendMessage("已将" + targetPlayer1.getName() + "传送至" + targetPlayer2.getName());
                    targetPlayer1.sendMessage(sendPlayer.getName() + "将你传送至" + targetPlayer2.getName());
                }
            }

            else if (args.length == 1) {
                //  /tp spawn 传送至出生点
                if (args[0].equals("spawn")) {
                    Location spawnLocation = sendPlayer.getWorld().getSpawnLocation();
                    sendPlayer.teleport(spawnLocation);
                    sendPlayer.sendMessage("你已被传送回世界出生点！");
                }
                //  /tp targetPlayer
                else {
                    targetPlayer = getPlayer(args[0]);
                    if (targetPlayer != null) {
                        sendPlayer.teleport(targetPlayer);
                        sender.sendMessage("你已被传送至：" + targetPlayer.getName());
                    }
                    else {
                        sender.sendMessage("目标玩家不存在！");
                    }
                }
            }

            else {
                sender.sendMessage("命令格式错误，请重试！");
                sender.sendMessage("§6=== Zhisuan11core传送指令 ===");
                sender.sendMessage("§a/tp <玩家> §7- 传送到指定玩家");
                sender.sendMessage("§a/tp @s <x> <y> <z> §7- 传送到指定坐标");
                sender.sendMessage("§a/tp <玩家1> <玩家2> §7- 将玩家1传送到玩家2");
                sender.sendMessage("§a/tp spawn <玩家> §7- 传送到世界出生点");
                return true;
            }

            // 设置冷却
            cooldowns.put(uuid, System.currentTimeMillis());
            return true;

        } catch (Exception e) {
            sender.sendMessage("出现未知错误！");
            return false;
        }
    }
}
