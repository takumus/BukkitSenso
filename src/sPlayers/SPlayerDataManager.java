package sPlayers;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumus on 2017/05/07.
 */
public class SPlayerDataManager {
    private static FileConfiguration config;
    private static File configFile;
    public static void init(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "players.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public static void saveMeta(SPlayer sp) {
        Map<String, String> meta = sp.getPermanentMeta();
        ConfigurationSection section = config.createSection(sp.getUUID());
        meta.entrySet().forEach((es) -> {
            section.set(es.getKey(), es.getValue());
        });
    }
    public static void loadMeta(SPlayer sp) {
        Map<String, String> meta = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection(sp.getUUID());
        sp.getMeta().set("permanent", meta);
        if (section == null) return;

        section.getKeys(false).forEach((key) -> {
            meta.put(key, section.getString(key));
        });
    }
    public static void save() {
        try{
            config.save(configFile);
        }catch(IOException exception) {

        }
    }
}
