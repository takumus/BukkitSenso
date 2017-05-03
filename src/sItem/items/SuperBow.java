package sItem.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import sItem.SItem;
import sItem.SItemManager;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import utils.MetadataManager;

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
    @EventHandler (priority = EventPriority.LOWEST)
    public void onShoot(EntityShootBowEvent e) {
        if (!this.getEnabled()) return;

        Player me = this.getHolder().getPlayer();
        if (!e.getEntity().equals(me)) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(me.getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("super_bow")) return;

        Arrow newArrow = me.launchProjectile(Arrow.class);
        MetadataManager.setMetadata(newArrow, "super_bow_arrow", "true");
        Vector v = e.getProjectile().getVelocity().clone().normalize();
        v.multiply(100);
        newArrow.setVelocity(v);

        //this.getHolder().playSound(Sound.ENTITY_ARROW_SHOOT);
        this.getHolder().playSound(Sound.ENTITY_BLAZE_HURT, 1f, 0.1f, true);

        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!this.getEnabled()) return;

        Entity damager = e.getDamager();
        if (!MetadataManager.getMetadata(damager, "super_bow_arrow").equalsIgnoreCase("true")) return;

        if(!((Projectile)damager).getShooter().equals(this.getHolder().getPlayer())) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        SPlayer victim = SPlayerManager.getSPlayer((Player)entity);
        victim.damage(this, 10D);
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(SItemManager.getPlugin(), () -> {
            this.getHolder().playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f, false);
        }, 2L);

        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!this.getEnabled()) return;

        Entity entity = e.getEntity();
        if (!MetadataManager.getMetadata(entity, "super_bow_arrow").equalsIgnoreCase("true")) return;

        entity.remove();
    }
}