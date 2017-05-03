package sItem.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sItem.SItem;
import sItem.SItemManager;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import utils.MetadataManager;

import java.util.UUID;

/**
 * Created by takumus on 2017/04/28.
 */
public class Grenade extends SItem{
    private ItemStack spade;
    private static double DAMAGE = 15D;
    public Grenade() {
        super("Snowball Grenade");
        this.spade = new ItemStack(Material.WOOD_SPADE);
        ItemMeta meta =  this.spade.getItemMeta();
        meta.setDisplayName(this.getName());
        this.spade.setItemMeta(meta);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(this.spade);
        s.getTag().setBoolean("snowball_grenade", true);
        this.spade = CraftItemStack.asBukkitCopy(s);
    }
    @Override
    public void initItem() {
        this.setItem(this.spade);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onRightClick(PlayerInteractEvent e) {
        if (!this.getEnabled()) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        SPlayer me = this.getHolder();
        if (!e.getPlayer().equals(this.getHolder().getPlayer())) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(me.getPlayer().getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("snowball_grenade")) return;

        SPlayer sp = SPlayerManager.getSPlayer(me.getPlayer());

        Snowball ball = me.getPlayer().launchProjectile(Snowball.class);
        MetadataManager.setMetadata(ball, "snowball_grenade", "true");

        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!this.getEnabled()) return;

        Entity damager = e.getDamager();
        if (!MetadataManager.getMetadata(damager, "snowball_explode").equalsIgnoreCase("true")) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        SPlayer shooter = SPlayerManager.getSPlayer(UUID.fromString(MetadataManager.getMetadata(damager, "snowball_holder_uuid")));
        if(!shooter.equals(this.getHolder())) return;

        SPlayer victim = SPlayerManager.getSPlayer((Player)entity);

        victim.damage(this, e.getDamage() * 2);

        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(SItemManager.getPlugin(), () -> {
            this.getHolder().playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f, false);
        }, 2L);

        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!this.getEnabled()) return;

        Projectile entity = e.getEntity();
        if (!MetadataManager.getMetadata(entity, "snowball_grenade").equalsIgnoreCase("true")) return;

        if(!entity.getShooter().equals(this.getHolder().getPlayer())) return;

        entity.remove();

        TNTPrimed ball = entity.getWorld().spawn(entity.getLocation(), TNTPrimed.class);
        MetadataManager.setMetadata(ball, "snowball_explode", "true");
        MetadataManager.setMetadata(ball, "snowball_holder_uuid", this.getHolder().getPlayer().getUniqueId().toString());
        ball.setFuseTicks(0);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onBreakByTNT(EntityExplodeEvent e) {
        if (!this.getEnabled()) return;

        Entity tnt = e.getEntity();
        if (!MetadataManager.getMetadata(tnt, "snowball_explode").equalsIgnoreCase("true")) return;

        SPlayer shooter = SPlayerManager.getSPlayer(UUID.fromString(MetadataManager.getMetadata(tnt, "snowball_holder_uuid")));
        if(!shooter.equals(this.getHolder())) return;

        e.blockList().clear();
    }
}