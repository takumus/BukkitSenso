package utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by takumus on 2017/05/06.
 */
public class DelayedTask {
    private static JavaPlugin plugin;
    public static void init(JavaPlugin plugin) {
        DelayedTask.plugin = plugin;
    }
    public static void task(Runnable runnable, long tick) {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, tick);
    }
}
