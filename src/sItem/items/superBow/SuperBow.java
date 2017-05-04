package sItem.items.superBow;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sItem.SItem;

/**
 * Created by takumus on 2017/04/28.
 */
public class SuperBow extends SItem{
    private ItemStack bow;
    private static double DAMAGE = 100D;
    public SuperBow() {
        super("Super Bow");
        this.bow = new ItemStack(Material.BOW);
        ItemMeta meta =  this.bow.getItemMeta();
        meta.setDisplayName(this.getName());
        this.bow.setItemMeta(meta);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(this.bow);
        s.getTag().setBoolean("super_bow", true);
        this.bow = CraftItemStack.asBukkitCopy(s);
    }
    @Override
    public void initItem() {
        this.setItem(this.bow);
        this.getHolder().getPlayer().getInventory().setItem(9, new ItemStack(Material.ARROW, 64));
    }
}