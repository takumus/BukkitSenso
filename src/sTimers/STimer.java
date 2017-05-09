package sTimers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayers.SPlayerManager;

/**
 * Created by takumus on 2017/05/09.
 */
public class STimer {
    private static int time;
    private static int currentTime;
    private static boolean counting;
    private static Runnable runnable;
    public static void init(JavaPlugin plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (counting) onTick();
        }, 0L, 20L);
    }
    public static void onTick() {
        currentTime ++;
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            sp.getPlayer().setExp((float)currentTime / time);
            sp.getPlayer().setLevel(time - currentTime);
        });
        if (time <= currentTime) {
            //終了
            stop();
        }
    }
    public static void start(int time, Runnable runnable) {
        if (counting) return;
        STimer.time = time;
        STimer.currentTime = 0;
        STimer.runnable = runnable;
        counting = true;
    }
    public static void stop() {
        if (!counting) return;
        counting = false;
        STimer.runnable.run();
    }
}