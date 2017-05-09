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
    private static Runnable onComplete;
    private static Runnable onTick;
    public static void init(JavaPlugin plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (counting) _onTick();
        }, 0L, 20L);
    }
    private static void _onTick() {
        currentTime ++;
        if (onTick != null) onTick.run();
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            sp.getPlayer().setExp((float)currentTime / time);
            sp.getPlayer().setLevel(time - currentTime);
        });
        if (time <= currentTime) {
            //終了
            stop();
        }
    }
    public static void start(int time, Runnable complete) {
        start(time, complete, null);
    }
    public static void start(int time, Runnable complete, Runnable tick) {
        if (counting) return;
        STimer.time = time;
        STimer.currentTime = 0;
        onComplete = complete;
        onTick = tick;
        counting = true;
    }
    public static void stop() {
        if (!counting) return;
        counting = false;
        onComplete.run();
        onComplete = null;
        onTick = null;
        currentTime = 0;
        time = 0;
    }
    public static int getRemaining() {
        return time - currentTime;
    }
    public static int getCurrentTime() {
        return currentTime;
    }
    public static int getTime() {
        return time;
    }
}