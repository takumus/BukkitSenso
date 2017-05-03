package sItem.items;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sItem.SItem;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;

/**
 * Created by takumus on 2017/04/28.
 */
public class Sword extends SItem{
    private ItemStack sword;
    private static double DAMAGE = 100D;
    public Sword() {
        super("Master Sword...!");
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
    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!this.getEnabled()) return;

        Player me = this.getHolder().getPlayer();
        if (!e.getDamager().equals(me)) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(me.getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("master_sword")) return;

        SPlayer victim = SPlayerManager.getSPlayer((Player)entity);
        victim.damage(this, Sword.DAMAGE);

        e.setCancelled(true);
    }
}