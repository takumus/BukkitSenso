package sItem;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sPlayers.SPlayer;

/**
 * Created by takumus on 2017/04/28.
 */
abstract public class SItem{
    private SPlayer holder;
    private int index;
    private boolean enabled;
    private String name;
    private ItemStack item;
    double _reloadTick;
    double _reloadMaxTick;
    boolean _reloading;
    public SItem(String name, ItemStack item, String id) {
        this.name = name;

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.getName());
        item.setItemMeta(meta);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(item);
        s.getTag().setBoolean(id, true);
        this.item = CraftItemStack.asBukkitCopy(s);
        this._reloadMaxTick = this._reloadTick = 0;
        SItemManager.addSItem(this);
    }
    public void initHolder(SPlayer holder, int index) {
        this.holder = holder;
        this.index = index;
    }
    public void initItem() {
        this.holder.getPlayer().getInventory().setItem(this.index, this.item);
    }
    public ItemStack getItem() {
        Inventory inventory = this.holder.getPlayer().getInventory();
        return inventory.getItem(this.index);
    }
    public void destroy() {
        SItemManager.removeSItem(this);
    }
    public void reload(int tick) {
        this._reloadMaxTick = tick;
        this._reloadTick = tick;
        this._reloading = true;
    }
    public boolean getEnabled() {
        return this.enabled;
    }
    public void setEnabled(boolean value) {
        this.enabled = value;
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
    public boolean isReloading() {
        return this._reloading;
    }
}
