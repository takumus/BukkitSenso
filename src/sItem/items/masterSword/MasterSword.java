package sItem.items.masterSword;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sItem.SItem;

/**
 * Created by takumus on 2017/04/28.
 */
public class MasterSword extends SItem{
    public final static double DAMAGE = 100D;
    public MasterSword() {
        super("Master MasterSword...!", new ItemStack(Material.DIAMOND_SWORD), "master_sword");
    }
}