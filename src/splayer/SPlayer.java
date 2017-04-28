package sPlayer;

import org.bukkit.entity.Player;
import sItem.SItem;
import sItem.items.Sword;

import java.util.ArrayList;

/**
 * Created by takumus on 2017/04/26.
 */
public class SPlayer {
    private Player player;
    private ArrayList<SItem> sItems;
    public SPlayer(Player player) {
        this.player = player;
        this.sItems = new ArrayList<>();
        this.addSItem(new Sword(), 0);
    }
    public Player getPlayer() {
        return this.player;
    }
    public String getName() {
        return this.player.getDisplayName();
    }
    public void addSItem(SItem sItem, int id) {
        this.sItems.add(sItem);
        sItem.init(this, id);
    }
    public void clearSItems() {
        this.sItems.forEach((sItem) -> sItem.destroy());
        this.sItems.clear();
    }
}
