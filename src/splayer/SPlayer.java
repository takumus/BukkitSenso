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
        this.addSItem(new Sword());
    }
    public Player getPlayer() {
        return this.player;
    }
    public String getName() {
        return this.player.getDisplayName();
    }
    public void addSItem(SItem sItem) {
        this.addSItem(sItem, this.sItems.size());
    }
    public void addSItem(SItem sItem, int id) {
        this.sItems.add(sItem);
        sItem.initHolder(this, id);
    }
    public void clearSItems() {
        this.sItems.forEach((sItem) -> sItem.destroy());
        this.sItems.clear();
        this.player.getInventory().clear();
    }
    public ArrayList<SItem> getSItems() {
        return this.sItems;
    }
    public void setSItemsEnabled(boolean value) {
        this.sItems.forEach((sItem) -> sItem.setEnabled(value));
    }
}
