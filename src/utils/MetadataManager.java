package utils;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created by takumus on 2017/04/30.
 */
public class MetadataManager {
    private static JavaPlugin plugin;
    public static void init(JavaPlugin plugin) {
        MetadataManager.plugin = plugin;
    }
    public static void setMetadata(Entity e, String key, String data) {
        e.setMetadata(key, new FixedMetadataValue(plugin, data));
    }
    public static String getMetadata(Entity e, String key) {
        List<MetadataValue> md = e.getMetadata(key);
        if (md == null || md.size() < 1) return "null";
        return md.get(0).asString();
    }
}
