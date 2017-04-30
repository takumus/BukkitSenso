package sItem.items;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import sItem.SItem;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import sun.jvm.hotspot.oops.MetadataField;
import utils.MetadataManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by takumus on 2017/04/28.
 */
public class SuperBow extends SItem{
    private ItemStack bow;
    private static double DAMAGE = 100D;
    public SuperBow() {
        super("スーパー弓");
        this.bow = new ItemStack(Material.BOW);
        ItemMeta meta =  this.bow.getItemMeta();
        meta.setDisplayName(this.getName());
        this.bow.setItemMeta(meta);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(this.bow);
        s.getTag().setBoolean("super_bow", true);
        this.bow =  CraftItemStack.asBukkitCopy(s);
    }
    @Override
    public void initItem() {
        this.setItem(this.bow);
        this.getHolder().getPlayer().getInventory().setItem(9, new ItemStack(Material.ARROW, 64));
    }
    @EventHandler (priority = EventPriority.LOWEST)
    private void onShoot(EntityShootBowEvent e) {
        if (!this.getEnabled()) return;

        Player me = this.getHolder().getPlayer();
        if (!e.getEntity().equals(me)) return;

        ItemStack hand = me.getInventory().getItemInMainHand();
        if (hand.getType().equals(Material.AIR)) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(hand);
        if (!s.getTag().getBoolean("super_bow")) return;

        e.setCancelled(true);

        Arrow newArrow = me.launchProjectile(Arrow.class);
        MetadataManager.setMetadata(newArrow, "super_bow_arrow", "true");
        Vector v = e.getProjectile().getVelocity();
        v.multiply(1000);
        newArrow.setVelocity(v);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    private void onClick(EntityDamageByEntityEvent e) {
        if (!this.getEnabled()) return;

        Entity damager = e.getDamager();
        if (!MetadataManager.getMetadata(damager, "super_bow_arrow").equalsIgnoreCase("true")) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        SPlayer victim = SPlayerManager.getSPlayer((Player)entity);
        e.setCancelled(true);
        victim.damage(this, 10D);
    }
}