package sItem.items.grenade;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import sItem.SItemController;
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;
import utils.DelayedTask;
import utils.MetadataManager;

import java.util.UUID;

/**
 * Created by takumus on 2017/05/04.
 */
public class GrenadeController extends SItemController{
    @Override
    public void onTick() {

    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onRightClick(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        SPlayer sp = SPlayerManager.getSPlayer(e.getPlayer());
        if (sp == null) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(sp.getPlayer().getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("snowball_grenade")) return;

        Grenade item = (Grenade) sp.getSItems().get(Grenade.class);
        if (!item.getEnabled()) return;
        if (item.isReloading()) return;

        Snowball ball = sp.getPlayer().launchProjectile(Snowball.class);
        MetadataManager.setMetadata(ball, "snowball_grenade", "true");
        item.reload(20);

        sp.playSound(Sound.ENTITY_FIREWORK_LAUNCH, 1f, 1f, true);

        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (!MetadataManager.getMetadata(damager, "snowball_explode").equalsIgnoreCase("true")) return;

        SPlayer victim = SPlayerManager.getSPlayer(e.getEntity());
        if (victim == null) return;

        SPlayer shooter = SPlayerManager.getSPlayer(UUID.fromString(MetadataManager.getMetadata(damager, "snowball_holder_uuid")));
        if (shooter == null) return;

        Grenade grenade = (Grenade) shooter.getSItems().get(Grenade.class);
        if (!grenade.getEnabled()) return;

        victim.damage(grenade, e.getDamage() * 0.5);

        DelayedTask.task(() -> {
            shooter.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f, false);
        }, 2L);

        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent e) {
        Projectile entity = e.getEntity();
        if (!MetadataManager.getMetadata(entity, "snowball_grenade").equalsIgnoreCase("true")) return;

        SPlayer shooter = SPlayerManager.getSPlayer((Entity) entity.getShooter());
        if (shooter == null) return;
        if (!shooter.getSItems().get(Grenade.class).getEnabled()) return;

        entity.remove();

        TNTPrimed ball = entity.getWorld().spawn(entity.getLocation(), TNTPrimed.class);
        MetadataManager.setMetadata(ball, "snowball_explode", "true");
        MetadataManager.setMetadata(ball, "snowball_holder_uuid", shooter.getPlayer().getUniqueId().toString());
        ball.setFuseTicks(0);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onBreakByTNT(EntityExplodeEvent e) {
        Entity tnt = e.getEntity();
        if (!MetadataManager.getMetadata(tnt, "snowball_explode").equalsIgnoreCase("true")) return;

        // SPlayer shooter = SPlayerManager.getSPlayer(UUID.fromString(MetadataManager.getMetadata(tnt, "snowball_holder_uuid")));
        // if(!shooter.equals(this.getHolder())) return;

        e.blockList().clear();
    }
}
