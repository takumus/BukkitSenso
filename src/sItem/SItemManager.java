package sItem;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by takumus on 2017/04/28.
 */
public class SItemManager {
    private static JavaPlugin plugin;
    private SItemManager() {
    }
    public static void init(JavaPlugin plugin) {
        SItemManager.plugin = plugin;
    }
    public static void addSItem(SItem item) {
        Bukkit.getServer().getPluginManager().registerEvents(item, plugin);
    }
    public static void removeItem(SItem item) {
        HandlerList.unregisterAll(item);
    }
}
