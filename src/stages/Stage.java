package stages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takumus on 2017/04/30.
 */
public class Stage {
    private List<Spawn> spawns;
    private String name;
    private String type;
    public Stage(String name, String type) {
        this.spawns = new ArrayList<>();
        this.name = name;
        this.type = type.toLowerCase();
    }
    public void addSpawn(Spawn spawn) {
        this.spawns.add(spawn);
    }
    public List<Spawn> getSpawns() {
        return this.spawns;
    }
    public String getName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }
}
