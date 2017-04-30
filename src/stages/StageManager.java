package stages;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takumus on 2017/04/30.
 */
public class StageManager {
    private static FileConfiguration config;
    private static File configFile;
    private static List<Stage> stages;
    public static void init(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "data.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        stages = new ArrayList<>();

        Spawn spawn = new Spawn(new Location(Bukkit.getServer().getWorld("world"), 0, 10, 0));
        spawn.setMeta("a", "1");
        spawn.setMeta("b", "2");

        Stage stage = new Stage("test");
        stage.addSpawn(spawn);
        stage.addSpawn(spawn);

        Stage stage2 = new Stage("test2");
        stage2.addSpawn(spawn);
        stage2.addSpawn(spawn);

        stages.add(stage);
        stages.add(stage2);

        saveConfig();
        loadConfig();
    }
    public static void saveConfig() {
        stages.clear();
        stages.forEach((s) -> {
            ConfigurationSection section = config.createSection(s.getName().toLowerCase());
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
        });
        try{
            config.save(configFile);
        }catch(IOException exception) {

        }
    }
    public static void loadConfig() {
        System.out.println("load config");
        config.getKeys(false).forEach((stageName) -> {
            ConfigurationSection stageSection = config.getConfigurationSection(stageName);
            Stage stage = new Stage(stageSection.getString("name"));
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
            stages.add(stage);
        });
        System.out.println(stages);
    }
}
