package sItem.items.superBow;

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

/**
 * Created by takumus on 2017/05/04.
 */
public class SuperBowController extends SItemController{
    private void shootArrow(SPlayer sp, Vector v) {
        Arrow newArrow = sp.getPlayer().launchProjectile(Arrow.class);
        MetadataManager.setMetadata(newArrow, "super_bow_arrow", "true");
        v.multiply(100);
        newArrow.setVelocity(v);

        sp.playSound(Sound.ENTITY_BLAZE_HURT, 1f, 0.1f, true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShoot(EntityShootBowEvent e) {
        SPlayer sp = SPlayerManager.getSPlayer(e.getEntity());
        if (sp == null) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(sp.getPlayer().getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("super_bow")) return;
        if (!sp.getSItems().get(SuperBow.class).getEnabled()) return;

        Vector v = e.getProjectile().getVelocity();
        Vector nv = v.clone().normalize();

        double power = v.distance(new Vector());

        this.shootArrow(sp, nv);

        if (power > 2.6) {
            DelayedTask.task(() -> {
                this.shootArrow(sp, nv);
            }, 2L);
        }

        e.setCancelled(true);
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

        victim.damage(damager.getSItems().get(SuperBow.class), 10D);

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
