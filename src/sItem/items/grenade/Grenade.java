package sItem.items.grenade;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sItem.SItem;

/**
 * Created by takumus on 2017/04/28.
 */
public class Grenade extends SItem{
    private ItemStack spade;
    private static double DAMAGE = 15D;
    public Grenade() {
        super("Snowball Grenade");
        this.spade = new ItemStack(Material.WOOD_SPADE);
        ItemMeta meta =  this.spade.getItemMeta();
        meta.setDisplayName(this.getName());
        this.spade.setItemMeta(meta);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(this.spade);
        s.getTag().setBoolean("snowball_grenade", true);
        this.spade = CraftItemStack.asBukkitCopy(s);
    }
    @Override
    public void initItem() {
        this.setItem(this.spade);
    }
}