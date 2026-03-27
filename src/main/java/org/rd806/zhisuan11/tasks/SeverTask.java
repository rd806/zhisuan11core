package org.rd806.zhisuan11.tasks;

import org.rd806.zhisuan11.Zhisuan11core;

// 启动插件定时执行的内容
public class SeverTask {

    private final Zhisuan11core plugin = Zhisuan11core.main;

    // 初始化任务
    public void InitialTasks() {
        // 掉落物清理
        plugin.clearDropItems.loadConfig();
    }

    // 定时任务
    public void ScheduleTasks() {
        // 游戏内侧边栏
        plugin.sidebar.startUpdate();
        // 清理掉落物
        plugin.clearDropItems.startClear();
        // 服务器公告
        plugin.broadcast.sendTask();
        // 检查玩家延迟
        plugin.checkPing.sendTask();
    }

    // 结束任务
    public void CloseTasks() {
        // 关闭数据库连接
    }
}
