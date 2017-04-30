package sPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumus on 2017/04/30.
 */
public class SPlayerMeta {
    private Map<String, String> metadata;
    public SPlayerMeta() {
        this.metadata = new HashMap<>();
    }
    public void clear() {
        this.metadata.clear();
    }
    public int getInt(String key) {
        return Integer.parseInt(this.metadata.get(key));
    }
    public double getDouble(String key) {
        return Double.parseDouble(this.metadata.get(key));
    }
    public String getString(String key) {
        return this.metadata.get(key);
    }
    public void set(String key, String value) {
        this.metadata.put(key, value);
    }
    public void set(String key, int value) {
        this.metadata.put(key, Integer.toString(value));
    }
    public void set(String key, double value) {
        this.metadata.put(key, Double.toString(value));
    }
}
