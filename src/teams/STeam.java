package teams;

import org.bukkit.ChatColor;
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
    private ChatColor chatColor;
    private String name;
    private List<SPlayer> members;
    int _selectorId;
    public STeam(DyeColor color, String name) {
        this.color = color;
        this.chatColor = ColorMap.getChatColor(color);
        this.name = name;
        this.members = new ArrayList<>();
        this._selectorId = 0;
    }
    public STeam(DyeColor color) {
        this(color, ColorMap.getName(color));
    }
    public boolean addSPlayer(SPlayer sp) {
        if (this.members.contains(sp)) return false;
        if (sp.getSTeam() != null) sp.getSTeam().removeSPlayer(sp);
        sp.setSTeam(this);
        this.members.add(sp);
        sp.message("You joined to " + this.chatColor + this.getName());
        return true;
    }
    public boolean removeSPlayer(SPlayer sp) {
        if (!this.members.contains(sp)) return false;
        this.members.remove(sp);
        sp.message("You left from " + this.chatColor + this.getName());
        return true;
    }
    public void clear() {
        this.members.clear();
    }
    public String getName() {
        return this.name;
    }
    public DyeColor getDyeColor() {
        return this.color;
    }
    public ChatColor getChatColor() {
        return this.chatColor;
    }
    public List<SPlayer> getMembers () {
        return this.members;
    }
}
