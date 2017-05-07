package sItem.items.masterSword;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import sItem.SItemController;
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;

/**
 * Created by takumus on 2017/05/04.
 */
public class MasterSwordController extends SItemController{
    @Override
    public void onTick() {

    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {

        SPlayer damager = SPlayerManager.getSPlayer(e.getDamager());
        if (damager == null) return;

        SPlayer victim = SPlayerManager.getSPlayer(e.getEntity());
        if (victim == null) return;

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(damager.getPlayer().getInventory().getItemInMainHand());
        if (s.getTag() == null || !s.getTag().getBoolean("master_sword")) return;

        MasterSword sword = (MasterSword) damager.getSItems().get(MasterSword.class);
        if (!sword.getEnabled()) return;

        victim.damage(sword, MasterSword.DAMAGE);

        e.setCancelled(true);
    }
}
