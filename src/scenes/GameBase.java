package scenes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.SItem;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import stages.Stage;

/**
 * Created by takumus on 2017/04/30.
 */
abstract public class GameBase implements Listener{
    public GameBase(JavaPlugin plugin) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin,  () -> {
            this.onTick();
        }, 0L, 1L);
    }
    abstract  public boolean start(Stage stage);
    abstract public void onTick();
    abstract public void onSPlayerDeath(SPlayer sPlayer, SItem weapon);
    @EventHandler(priority = EventPriority.HIGHEST)
    private void preventDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.isCancelled()) return;
        SPlayer victim = SPlayerManager.getSPlayer((Player) event.getEntity());
        if (event.getFinalDamage() >= victim.getPlayer().getHealth()) {
            event.setDamage(0);
            victim.getPlayer().setHealth(0.5);
            this.onSPlayerDeath(victim, victim.getLastDamagesWeapon());
        }
        ((Player) event.getEntity()).setNoDamageTicks(0);
        ((Player) event.getEntity()).setMaximumNoDamageTicks(0);
    }
    @EventHandler(priority = EventPriority.LOW)
    private void preventDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void 

    public void message(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }
}
