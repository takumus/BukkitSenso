package utils;

import org.bukkit.Location;
import sPlayers.SPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumus on 2017/04/30.
 */
public class SMeta {
    private Map<String, Object> metadata;
    public SMeta() {
        this.metadata = new HashMap<>();
    }
    public void clear() {
        this.metadata.clear();
    }
    public int getInt(String key) {
        return ((Integer)this.metadata.get(key)).intValue();
    }
    public double getDouble(String key) {
        return ((Double)this.metadata.get(key)).doubleValue();
    }
    public boolean getBoolean(String key) {
        if (this.metadata.get(key) == null) return false;
        return ((Boolean)this.metadata.get(key)).booleanValue();
    }
    public String getString(String key) {
        return (String)this.metadata.get(key);
    }
    public Location getLocation(String key) {
        return (Location) this.metadata.get(key);
    }
    public SPlayer getSPlayer(String key) {
        return (SPlayer) this.metadata.get(key);
    }
    public Object getObject(String key) {
        return this.metadata.get(key);
    }
    public void set(String key, String value) {
        this.metadata.put(key, value);
    }
    public void set(String key, Object value) {
        this.metadata.put(key, value);
    }
    public void set(String key, int value) {
        this.metadata.put(key, new Integer(value));
    }
    public void set(String key, double value) {
        this.metadata.put(key, new Double(value));
    }
    public void set(String key, boolean value) {
        this.metadata.put(key, new Boolean(value));
    }
}
