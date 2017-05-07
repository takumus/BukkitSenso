package sItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.items.grenade.GrenadeController;
import sItem.items.masterSword.MasterSwordController;
import sItem.items.superBow.SuperBowController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takumus on 2017/04/28.
 */
public class SItemManager {
    private static JavaPlugin plugin;
    private static List<SItemController> sItemControllers;
    private static List<SItem> sItems;
    private SItemManager() {
    }
    public static void init(JavaPlugin plugin) {
        SItemManager.plugin = plugin;
        sItemControllers = new ArrayList<>();
        sItems = new ArrayList<>();
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            sItemControllers.forEach((controller) -> {
                controller._onTick();
            });
            sItems.forEach((si) -> {
                if (si._reloading) {
                    si._reloadTick --;
                    ItemStack item = si.getItem();
                    if (item == null) return;
                    short d = 0;
                    if (si._reloadTick > 0) {
                        d = (short)(item.getType().getMaxDurability() * (si._reloadTick / si._reloadMaxTick));
                    }else if (si._reloadTick < 0) {
                        si._reloading = false;
                    }
                    item.setDurability(d);
                }
            });
        }, 0L, 1L);

        //コントローラー初期化
        addSItemController(new GrenadeController());
        addSItemController(new SuperBowController());
        addSItemController(new MasterSwordController());
    }
    public static void addSItemController(SItemController item) {
        Bukkit.getServer().getPluginManager().registerEvents(item, plugin);
        sItemControllers.add(item);
    }
    public static void removeSItemController(SItemController item) {
        HandlerList.unregisterAll(item);
        sItemControllers.remove(item);
    }
    public static void addSItem(SItem item) {
        sItems.add(item);
    }
    public static void removeSItem(SItem item) {
        sItems.remove(item);
    }
    public static void clearSItems() {
        sItems.clear();
    }
    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
