package sItem;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sPlayers.SPlayer;

/**
 * Created by takumus on 2017/04/28.
 */
abstract public class SItem implements Listener{
    private SPlayer holder;
    private int index;
    private boolean enabled;
    private String name;
    private ItemStack item;
    public SItem(String name, ItemStack item, String id) {
        this.name = name;

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.getName());
        item.setItemMeta(meta);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(item);
        s.getTag().setBoolean(id, true);
        this.item = CraftItemStack.asBukkitCopy(s);
    }
    public void initHolder(SPlayer holder, int index) {
        this.holder = holder;
        this.index = index;
    }
    public void initItem() {
        this.holder.getPlayer().getInventory().setItem(this.index, this.item);
    }
    public void destroy() {

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
}
