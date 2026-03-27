package org.rd806.zhisuan11;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import org.rd806.zhisuan11.command.MainCommand;
import org.rd806.zhisuan11.command.TeleportCommand;
import org.rd806.zhisuan11.command.CommandTabCompleter;
import org.rd806.zhisuan11.core.JoinInfo;
import org.rd806.zhisuan11.core.JoinItem;
import org.rd806.zhisuan11.core.Respawn;
import org.rd806.zhisuan11.core.Sidebar;
import org.rd806.zhisuan11.gui.GameMenu;
import org.rd806.zhisuan11.gui.GameMenuClick;
import org.rd806.zhisuan11.tasks.Broadcast;
import org.rd806.zhisuan11.tasks.CheckPing;
import org.rd806.zhisuan11.tasks.ClearDropItems;
import org.rd806.zhisuan11.tasks.SeverTask;

import java.util.Objects;


public final class Zhisuan11core extends JavaPlugin {

    // 全局变量代表主类
    public static Zhisuan11core main;

    public GameMenu gameMenu;
    public Sidebar sidebar;
    public Broadcast broadcast;
    public ClearDropItems clearDropItems;
    public CheckPing checkPing;

    public SeverTask severTask;

    // 构造函数
    public Zhisuan11core() {
        // 应该在构造函数中先赋值静态变量
        main = this;
        // 初始化全局类
        gameMenu = new GameMenu();
        sidebar = new Sidebar();

        broadcast = new Broadcast();
        clearDropItems = new ClearDropItems();
        checkPing = new CheckPing();

        severTask = new SeverTask();
    }

    @Override
    public void onEnable() {

        // Plugin startup logic
        getLogger().info("欢迎您使用智算11班服务器插件！");
        getLogger().info("插件仓库：https://github.com/5882886/My-Minecraft-Server！");
        getLogger().info(" ");
        getLogger().info("███████╗██╗░░██╗██╗░██████╗██╗░░░██╗░█████╗░███╗░░██╗░░███╗░░░░███╗░░");
        getLogger().info("╚════██║██║░░██║██║██╔════╝██║░░░██║██╔══██╗████╗░██║░████║░░░████║░░");
        getLogger().info("░░███╔═╝███████║██║╚█████╗░██║░░░██║███████║██╔██╗██║██╔██║░░██╔██║░░");
        getLogger().info("██╔══╝░░██╔══██║██║░╚═══██╗██║░░░██║██╔══██║██║╚████║╚═╝██║░░╚═╝██║░░");
        getLogger().info("███████╗██║░░██║██║██████╔╝╚██████╔╝██║░░██║██║░╚███║███████╗███████╗");
        getLogger().info("╚══════╝╚═╝░░╚═╝╚═╝╚═════╝░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚══╝╚══════╝╚══════╝");
        getLogger().info(" ");


        Objects.requireNonNull(Bukkit.getPluginCommand("zhisuan11")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("tp")).setExecutor(new TeleportCommand());

        Objects.requireNonNull(Bukkit.getPluginCommand("zhisuan11")).setTabCompleter(new CommandTabCompleter());

        // 注册服务器事件
        Bukkit.getPluginManager().registerEvents(new JoinInfo(), this);
        Bukkit.getPluginManager().registerEvents(new JoinItem(), this);
        Bukkit.getPluginManager().registerEvents(new Respawn(), this);
        Bukkit.getPluginManager().registerEvents(new GameMenuClick(), this);

        // 生成配置文件
        saveDefaultConfig();
        reloadConfig();

        // 服务器任务
        severTask.InitialTasks();
        severTask.ScheduleTasks();
    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic
        getLogger().info("感谢您使用智算11班服务器插件！");
        getLogger().info("插件仓库：https://github.com/5882886/My-Minecraft-Server！");
        getLogger().info(" ");
        getLogger().info("███████╗██╗░░██╗██╗░██████╗██╗░░░██╗░█████╗░███╗░░██╗░░███╗░░░░███╗░░");
        getLogger().info("╚════██║██║░░██║██║██╔════╝██║░░░██║██╔══██╗████╗░██║░████║░░░████║░░");
        getLogger().info("░░███╔═╝███████║██║╚█████╗░██║░░░██║███████║██╔██╗██║██╔██║░░██╔██║░░");
        getLogger().info("██╔══╝░░██╔══██║██║░╚═══██╗██║░░░██║██╔══██║██║╚████║╚═╝██║░░╚═╝██║░░");
        getLogger().info("███████╗██║░░██║██║██████╔╝╚██████╔╝██║░░██║██║░╚███║███████╗███████╗");
        getLogger().info("╚══════╝╚═╝░░╚═╝╚═╝╚═════╝░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚══╝╚══════╝╚══════╝");
        getLogger().info(" ");


        severTask.CloseTasks();
    }
}
