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
    private ItemStack sword;
    public final static double DAMAGE = 100D;
    public MasterSword() {
        super("Master MasterSword...!");
        this.sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta =  this.sword.getItemMeta();
        meta.setDisplayName(this.getName());
        this.sword.setItemMeta(meta);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(this.sword);
        s.getTag().setBoolean("master_sword", true);
        this.sword = CraftItemStack.asBukkitCopy(s);
    }
    @Override
    public void initItem() {
        this.setItem(this.sword);
    }
}