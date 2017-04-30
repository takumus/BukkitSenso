package stages;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumus on 2017/04/30.
 */
public class Spawn {
    private Location location;
    private Map<String, String> meta;
    public Spawn(Location location) {
        this.location = location;
        this.meta = new HashMap<>();
    }
    public void setLocation(double x, double y, double z, float yaw, float pitch, String world) {
        this.location = new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return this.location;
    }
    public void setMeta(String key, String value) {
        this.meta.put(key, value);
    }
    public String getMeta(String key) {
        return this.meta.get(key);
    }
    public Map<String, String> getAllMeta() {
        return this.meta;
    }
}
