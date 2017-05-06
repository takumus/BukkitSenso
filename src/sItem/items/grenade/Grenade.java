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
        super("Snowball Grenade", new ItemStack(Material.WOOD_SPADE), "snowball_grenade");
    }
}