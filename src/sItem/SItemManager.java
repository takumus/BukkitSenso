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
    public static void addSItem(SItemController item) {
        Bukkit.getServer().getPluginManager().registerEvents(item, plugin);
    }
    public static void removeItem(SItemController item) {
        HandlerList.unregisterAll(item);
    }
    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
