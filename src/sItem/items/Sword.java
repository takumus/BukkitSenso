package sItem.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import sItem.SItem;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by takumus on 2017/04/28.
 */
public class Sword extends SItem{
    private ItemStack sword;
    private static double DAMAGE = 100D;
    public Sword() {
        super("マスターソード...!");
        this.sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta =  this.sword.getItemMeta();
        meta.setDisplayName(this.getName());
        meta.setLore(Arrays.asList("master_sword"));
        this.sword.setItemMeta(meta);
    }
    @Override
    public void initItem() {
        this.setItem(this.sword);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    private void onClick(EntityDamageByEntityEvent e) {
        if (!this.getEnabled()) return;

        Player me = this.getHolder().getPlayer();
        if (!e.getDamager().equals(me)) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        ItemStack hand = me.getInventory().getItemInMainHand();
        if (hand.getType().equals(Material.AIR)) return;

        List<String> lore = hand.getItemMeta().getLore();
        if (lore == null) return;

        if (!lore.contains("master_sword")) return;

        SPlayer victim = SPlayerManager.getSPlayer((Player)entity);
        e.setCancelled(true);
        victim.damage(this, Sword.DAMAGE);
    }
}