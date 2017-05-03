package scenes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sItem.SItem;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import stages.Stage;
import utils.Effects;

/**
 * Created by takumus on 2017/04/30.
 */
public class Deathmatch extends GameBase {
    public Deathmatch(JavaPlugin plugin) {
        super(plugin);
    }
    private void spawn(SPlayer sp) {
        sp.setSItemsEnabled(true);
    }
    @Override
    public boolean start(Stage stage) {
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            this.spawn(sp);
        });
        return false;
    }

    @Override
    public void onTick() {
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            sp.getPlayer().setSneaking(true);
            sp.getPlayer().getWorld().setTime(0);
            sp.getPlayer().getWorld().setStorm(false);
            sp.getPlayer().getWorld().setThundering(false);
        });
    }

    @Override
    public void onSPlayerDeath(SPlayer sPlayer, SItem weapon) {
        if(weapon != null) {
            this.message(weapon.getHolder().getNameWithColor() + ChatColor.GRAY + " -> " + sPlayer.getNameWithColor() + ChatColor.GRAY + ChatColor.ITALIC + " (" + weapon.getName() + ")");
            sPlayer.sendTitle(
                    ChatColor.WHITE + "Killed by " + sPlayer.getNameWithColor(),
                    ChatColor.RED + weapon.getName(), 20 * 3,
                    0, 20
            );
        }else {
            this.message(ChatColor.MAGIC + "?????" + ChatColor.RESET + ChatColor.GRAY + " -> " + sPlayer.getNameWithColor());
            sPlayer.sendTitle(
                    ChatColor.WHITE + "Killed by " + ChatColor.GRAY + ChatColor.MAGIC + "?????",
                    "", 20 * 3,
                    0, 20
            );
        }
        sPlayer.addPotion(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1));
        //sPlayer.addPotion(new PotionEffect(PotionEffectType.WITHER, 20 * 5, 1));
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.isCancelled()) return;
        SPlayer victim = SPlayerManager.getSPlayer((Player) event.getEntity());
        Effects.blood(victim.getPlayer().getLocation());
    }

}
