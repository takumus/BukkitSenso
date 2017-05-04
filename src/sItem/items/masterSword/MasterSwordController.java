package sItem.items.masterSword;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import sItem.SItemController;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;

/**
 * Created by takumus on 2017/05/04.
 */
public class MasterSwordController extends SItemController{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {

        SPlayer damager = SPlayerManager.getSPlayer(e.getDamager());
        if (damager == null) return;

        SPlayer victim = SPlayerManager.getSPlayer(e.getEntity());
        if (victim == null) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(damager.getPlayer().getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("master_sword")) return;

        victim.damage(damager.getSItems().get(MasterSword.class), MasterSword.DAMAGE);

        e.setCancelled(true);
    }
}
