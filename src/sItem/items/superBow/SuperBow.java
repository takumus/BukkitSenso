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
    public static double DAMAGE = 10D;
    public SuperBow() {
        super("Super Bow", new ItemStack(Material.BOW), "super_bow");
    }

    @Override
    public void initItem() {
        super.initItem();
        this.getHolder().addItemToSub(new ItemStack(Material.ARROW, 64));
    }
}