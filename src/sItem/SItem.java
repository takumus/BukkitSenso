package sItem;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayer.SPlayer;

/**
 * Created by takumus on 2017/04/28.
 */
abstract public class SItem implements Listener{
    private SPlayer holder;
    private int index;
    private boolean enabled;
    private String name;
    private JavaPlugin plugin;
    public SItem(String name) {
        this.name = name;
    }
    public void initHolder(SPlayer holder, int index) {
        this.holder = holder;
        this.index = index;
    }
    abstract public void initItem();
    public void destroy() {

    }
    public boolean getEnabled() {
        return this.enabled;
    }
    public void setEnabled(boolean value) {
        this.enabled = value;
    }
    public void setItem(ItemStack item) {
        this.holder.getPlayer().getInventory().setItem(this.index, item);
    }
    public String getName() {
        return this.name;
    }
    public SPlayer getHolder() {
        return this.holder;
    }
    public int getIndex() {
        return this.index;
    }
}
