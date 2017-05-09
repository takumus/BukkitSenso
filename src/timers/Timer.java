package timers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by takumus on 2017/05/09.
 */
public class Timer {
    private static int time;
    private static int currentTime;
    private static boolean counting;
    public static void init(JavaPlugin plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (counting) onTick();
        }, 0L, 20L);
    }
    public static void onTick() {
        currentTime ++;
        if (time <= currentTime) {
            //終了
            stop();
        }
    }
    public static void begin(int time, String tag) {
        Timer.time = time;
        Timer.currentTime = 0;
        counting = true;
    }
    public static void stop() {
        counting = false;
    }
}
