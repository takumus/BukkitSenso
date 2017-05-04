package stages;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by takumus on 2017/04/30.
 */
public class StageManager {
    private static FileConfiguration config;
    private static File configFile;
    private static Map<String, Stage> stages;
    public static void init(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "data.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        stages = new HashMap<>();

        loadConfig();
        saveConfig();
    }
    public static void command(String[] args, CommandSender sender) {

    }
    public static boolean addSpawn(Location location, String[] meta) {
        Spawn spawn = new Spawn(location);
        boolean errorWithMeta = false;
        Arrays.asList(meta).forEach((metaStr) -> {
            String[] kv = metaStr.split(":");
            //if (pair.length != 2) return false;
            spawn.setMeta(kv[0], kv[1]);
        });
        return true;
    }
    public static Stage getStage(String name, String type) {
        return stages.get(name + ":" + type);
    }
    public static Stage createStage(String name, String type) {
        Stage stage = new Stage(name, type.toLowerCase());
        stages.put(name + ":" + type, stage);
        return stage;
    }
    public static void saveConfig() {
        System.out.println("save config");
        stages.values().forEach((s) -> {
            ConfigurationSection section = config.createSection(s.getName().toLowerCase() + ":" +  s.getType().toLowerCase());
            List<Map<String, Object>> sm = new ArrayList<>();
            s.getSpawns().forEach((sp) -> {
                Map<String, Object> mm = new HashMap<>();
                mm.put("location", sp.getLocation().getWorld().getName() + "," + sp.getLocation().getX() + "," + sp.getLocation().getY() + "," + sp.getLocation().getZ() + "," + sp.getLocation().getYaw() + "," + sp.getLocation().getPitch());

                Map<String, String> mmm = new HashMap<>();
                sp.getAllMeta().entrySet().forEach((es) -> {
                    mmm.put(es.getKey(), es.getValue());
                });
                mm.put("meta", mmm);
                sm.add(mm);
            });
            section.set("spawns", sm);
            section.set("name", s.getName());
            section.set("type", s.getType());
        });
        try{
            config.save(configFile);
        }catch(IOException exception) {

        }
    }
    public static void loadConfig() {
        System.out.println("load config");
        stages.clear();
        config.getKeys(false).forEach((stageName) -> {
            ConfigurationSection stageSection = config.getConfigurationSection(stageName);
            Stage stage = new Stage(stageSection.getString("name"), stageSection.getString("type"));
            stageSection.getList("spawns").forEach((spawnObject) -> {
                Map<String, Object> spawnHashMap = (Map<String, Object>)spawnObject;
                String locationStr = (String) spawnHashMap.get("location");
                String[] locationArray = locationStr.split(",");
                Location location = new Location(
                        Bukkit.getServer().getWorld(locationArray[0]),
                        Double.parseDouble(locationArray[1]),
                        Double.parseDouble(locationArray[2]),
                        Double.parseDouble(locationArray[3]),
                        Float.parseFloat(locationArray[4]),
                        Float.parseFloat(locationArray[5])
                );
                Spawn spawn = new Spawn(location);
                ((Map<String, String>)spawnHashMap.get("meta")).entrySet().forEach((meta) -> {
                    spawn.setMeta(meta.getKey(), meta.getValue());
                });
                stage.addSpawn(spawn);
            });
            stages.put(stage.getName().toLowerCase() + stage.getType().toLowerCase(), stage);
        });
        System.out.println(stages.size() + " stages loaded.");
    }
}
