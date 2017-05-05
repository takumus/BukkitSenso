package teams;

import org.bukkit.DyeColor;
import sPlayer.SPlayer;
import utils.ColorMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takumus on 2017/05/05.
 */
public class STeam {
    private DyeColor color;
    private String name;
    private List<SPlayer> members;

    public STeam(DyeColor color, String name) {
        this.color = color;
        this.name = name;
        this.members = new ArrayList<>();
    }
    public STeam(DyeColor color) {
        this(color, ColorMap.getName(color));
    }
    public boolean addSPlayer(SPlayer sp) {
        if (this.members.contains(sp)) return false;
        this.members.add(sp);
        return true;
    }
    public boolean removeSPlayer(SPlayer sp) {
        if (!this.members.contains(sp)) return false;
        this.members.remove(sp);
        return true;
    }
    public void clear() {
        this.members.clear();
    }
}
