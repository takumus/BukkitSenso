package sItem;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takumus on 2017/04/28.
 */
public class SItemManager {
    private static JavaPlugin plugin;
    private static List<SItemController> sItems;
    private SItemManager() {
    }
    public static void init(JavaPlugin plugin) {
        SItemManager.plugin = plugin;
        sItems = new ArrayList<>();
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            sItems.forEach((sItem) -> {
                sItem.onTick();
            });
        }, 0L, 1L);
    }
    public static void addSItem(SItemController item) {
        Bukkit.getServer().getPluginManager().registerEvents(item, plugin);
        sItems.add(item);
    }
    public static void removeItem(SItemController item) {
        HandlerList.unregisterAll(item);
        sItems.add(item);
    }
    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
