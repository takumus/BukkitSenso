package sItem.items.superBow;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import sItem.SItemController;
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;
import utils.DelayedTask;
import utils.MetadataManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by takumus on 2017/05/04.
 */
public class SuperBowController extends SItemController{
    private List<Entity> arrows;
    public SuperBowController () {
        this.arrows = new ArrayList<>();
    }
    @Override
    public void onTick() {
        Iterator<Entity> ei = this.arrows.iterator();
        while(ei.hasNext()){
            Entity e = ei.next();
            int living = Integer.parseInt(MetadataManager.getMetadata(e, "arrow_living_time"));
            if (living > 5) {
                e.remove();
                ei.remove();
                continue;
            }
            living ++;
            MetadataManager.setMetadata(e, "arrow_living_time", living + "");
        }
    }
    private void shootArrow(SPlayer sp, Vector v) {
        Arrow newArrow = sp.getPlayer().launchProjectile(Arrow.class);
        MetadataManager.setMetadata(newArrow, "super_bow_arrow", "true");
        MetadataManager.setMetadata(newArrow, "arrow_living_time", "0");
        v.multiply(100);
        newArrow.setVelocity(v);
        newArrow.setGravity(false);
        sp.playSound(Sound.ENTITY_BLAZE_HURT, 1f, 0.1f, true);
        this.arrows.add(newArrow);
    }
    private double distanceLine(Location p1, Location p2, Location p3)
    {
        Vector p3p2 = p3.subtract(p2).toVector();
        Vector p1p2 = p1.subtract(p2).toVector();
        double d = p3p2.getCrossProduct(p1p2).length();
        double l = p2.distance(p3);
        return d / l;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShoot(EntityShootBowEvent e) {
        SPlayer sp = SPlayerManager.getSPlayer(e.getEntity());
        if (sp == null) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(sp.getPlayer().getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("super_bow")) return;

        e.setCancelled(true);

        SuperBow item = (SuperBow) sp.getSItems().get(SuperBow.class);
        if (!item.getEnabled()) return;
        if (item.isReloading()) return;

        item.reload(10);

        Vector v = e.getProjectile().getVelocity();
        Vector nv = v.clone().normalize();

        double power = v.distance(new Vector());

        this.shootArrow(sp, nv);

        if (power > 2.6) {
            DelayedTask.task(() -> {
                this.shootArrow(sp, nv);
            }, 2L);
        }
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity damagerEntity = e.getDamager();
        if (!MetadataManager.getMetadata(damagerEntity, "super_bow_arrow").equalsIgnoreCase("true")) return;

        SPlayer damager = SPlayerManager.getSPlayer((Entity) ((Projectile)damagerEntity).getShooter());
        if (damager == null) return;
        if (!damager.getSItems().get(SuperBow.class).getEnabled()) return;

        SPlayer victim = SPlayerManager.getSPlayer(e.getEntity());
        if (victim == null) return;

        double headDistance = distanceLine(victim.getPlayer().getLocation().add(0, 1.79, 0),damagerEntity.getLocation(),damagerEntity.getLocation().add(damagerEntity.getVelocity()));

        if(headDistance < 0.043) {
            victim.damage(damager.getSItems().get(SuperBow.class), SuperBow.DAMAGE * 1.8);
        }else {
            victim.damage(damager.getSItems().get(SuperBow.class), SuperBow.DAMAGE);
        }

        DelayedTask.task(() -> {
            damager.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f, false);
        }, 2L);

        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (!MetadataManager.getMetadata(entity, "super_bow_arrow").equalsIgnoreCase("true")) return;

        entity.remove();
    }
}
