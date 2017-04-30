package stages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takumus on 2017/04/30.
 */
public class Stage {
    private List<Spawn> spawns;
    private String name;
    public Stage(String name) {
        this.spawns = new ArrayList<>();
        this.name = name;
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
}
