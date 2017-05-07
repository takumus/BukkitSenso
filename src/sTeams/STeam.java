package sTeams;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;
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
    private int score;
    private int kill;
    private int death;
    public STeam(DyeColor color, String name) {
        this.color = color;
        this.chatColor = ColorMap.getChatColor(color);
        this.name = name;
        this.members = new ArrayList<>();
        this._selectorId = 0;
        this.score = 0;
        this.kill = 0;
        this.death = 0;
    }
    public STeam(DyeColor color) {
        this(color, ColorMap.getName(color));
    }
    public boolean addSPlayer(SPlayer sp) {
        if (this.members.contains(sp)) return false;
        if (sp.getSTeam() != null) sp.getSTeam().removeSPlayer(sp);
        sp.joinSTeam(this);
        this.members.add(sp);
        //メッセージ配信
        sp.message("You joined to " + this.getNameWithColor());
        SPlayerManager.message(
                ChatColor.BOLD + sp.getName() + ChatColor.RESET + " joined to " + this.getNameWithColor(),
                sp
        );
        TeamSelector.updateTeam();
        return true;
    }
    public boolean removeSPlayer(SPlayer sp) {
        if (!this.members.contains(sp)) return false;
        this.members.remove(sp);
        //メッセージ配信
        sp.message("You left from " + this.getNameWithColor());
        SPlayerManager.message(
                ChatColor.BOLD + sp.getName() + ChatColor.RESET + " left from " + this.getNameWithColor(),
                sp
        );
        TeamSelector.updateTeam();
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

    public String getNameWithColor() {
        return this.getStringWithColor(this.name);
    }
    public String getStringWithColor(String string) {
        return ChatColor.RESET + "" + this.getChatColor() + "" + ChatColor.BOLD + string + ChatColor.RESET;
    }
    public void message(String message) {
        this.members.forEach((sp) -> {
            sp.message(message);
        });
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void addScore(int score) {
        this.score += score;
    }
    public int getScore() {
        return this.score;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }
    public void addKill(int kill) {
        this.kill += kill;
    }
    public int getKill() {
        return this.kill;
    }

    public void setDeath(int death) {
        this.death = death;
    }
    public void addDeath(int death) {
        this.death += death;
    }
    public int getDeath() {
        return this.death;
    }
}
