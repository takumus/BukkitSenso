package scenes;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sItem.SItem;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import sPlayer.SPlayerMeta;
import stages.Stage;
import utils.Effects;

/**
 * Created by takumus on 2017/04/30.
 */
public class Deathmatch extends GameBase {
    public Deathmatch(JavaPlugin plugin) {
        super(plugin);
        this.start(null);
    }
    private void spawn(SPlayer sp) {
        sp.setSItemsEnabled(true);
        sp.getPlayer().setGameMode(GameMode.SURVIVAL);
        sp.getPlayer().setWalkSpeed(0.3f);
        SPlayerMeta meta = sp.getMeta();
        meta.set(MetaKey.KILL_CAMERA_COUNT, 0);
        meta.set(MetaKey.STATUS, Status.PLAY);
        meta.set(MetaKey.NO_DAMAGE, false);
        meta.set(MetaKey.KILLER, null);
        meta.set(MetaKey.KILLED_LOCATION, null);
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
            SPlayerMeta meta = sp.getMeta();
            if (meta.getString(MetaKey.STATUS).equals(Status.KILL_CAMERA)) {
                int count = meta.getInt(MetaKey.KILL_CAMERA_COUNT);
                SPlayer killer = meta.getSPlayer(MetaKey.KILLER);
                Location targetLocation = killer.getMeta().getString(MetaKey.STATUS).equals(Status.PLAY) ? killer.getPlayer().getLocation() : killer.getMeta().getLocation(MetaKey.KILLED_LOCATION);
                sp.lookAt(targetLocation);
                Location killedPosForCamera = meta.getLocation(MetaKey.KILLED_LOCATION_FOR_KILL_CAMERA);
                if (count < 40) {
                    if (count % 10 == 0) Effects.strikeLightning(meta.getLocation(MetaKey.KILLED_LOCATION));
                }
                if (count < 30) {
                    double vy = (Math.cos(count / 30D * Math.PI * 2 + Math.PI) + 1) / 2;
                    sp.getPlayer().teleport(killedPosForCamera.add(0, vy, 0));
                }else if (count < 120){
                    double r = (count - 30) / 50D * Math.PI * 2;
                    r = r > Math.PI * 2 ? Math.PI * 2 : r;
                    double v = (Math.cos(r + Math.PI) + 1) / 2;
                    Vector dir = sp.getPlayer().getLocation().getDirection();
                    dir.multiply(v * 1.5 + (0.1 * r));
                    double distance = sp.getPlayer().getLocation().distance(targetLocation);
                    if (distance > 5) sp.getPlayer().teleport(killedPosForCamera.add(dir));
                }else {
                    this.spawn(sp);
                    return;
                }
                Effects.blood(meta.getLocation(MetaKey.KILLED_LOCATION).clone().add(0, Math.random() + 0.2, 0), 30);
                sp.lookAt(targetLocation);

                meta.set(MetaKey.KILL_CAMERA_COUNT, count + 1);
            }
        });
    }

    @Override
    public void onSPlayerDeath(SPlayer victim, SItem weapon) {
        if(weapon != null) {
            this.message(weapon.getHolder().getNameWithColor() + ChatColor.GRAY + " -> " + victim.getNameWithColor() + ChatColor.GRAY + ChatColor.ITALIC + " (" + weapon.getName() + ")");
            victim.sendTitle(
                    ChatColor.WHITE + "Killed by " + victim.getNameWithColor(),
                    ChatColor.RED + weapon.getName(), 20 * 3,
                    0, 20
            );
            weapon.getHolder().sendTitle(
                    "",
                    ChatColor.WHITE + "killed " + victim.getNameWithColor(), 10,
                    0, 5
            );
        }else {
            this.message(ChatColor.MAGIC + "?????" + ChatColor.RESET + ChatColor.GRAY + " -> " + victim.getNameWithColor());
            victim.sendTitle(
                    ChatColor.WHITE + "Killed by " + ChatColor.GRAY + ChatColor.MAGIC + "?????",
                    "", 20 * 3,
                    0, 20
            );
        }

        victim.clearInventory();

        victim.getMeta().set(MetaKey.STATUS, Status.KILL_CAMERA);
        victim.getMeta().set(MetaKey.KILL_CAMERA_COUNT, 0);
        victim.getMeta().set(MetaKey.NO_DAMAGE, true);
        victim.getMeta().set(MetaKey.KILLED_LOCATION, victim.getPlayer().getLocation().clone());
        victim.getMeta().set(MetaKey.KILLED_LOCATION_FOR_KILL_CAMERA, victim.getPlayer().getLocation().clone());
        victim.getMeta().set(MetaKey.KILLER, weapon.getHolder());

        victim.getPlayer().setHealth(20D);
        victim.addPotion(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 1, 1));
        victim.addPotion(new PotionEffect(PotionEffectType.WITHER, 20 * 1, 1));
        victim.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.isCancelled()) return;
        SPlayer victim = SPlayerManager.getSPlayer((Player) event.getEntity());
        if (victim.getMeta().getBoolean(MetaKey.NO_DAMAGE)) {
            event.setCancelled(true);
            return;
        }
        if (victim.getLastDamagesWeapon() == null) {
            event.setCancelled(true);
            return;
        }
        Effects.blood(victim.getPlayer().getLocation().add(0, 1, 0), 100);
    }
}
class MetaKey {
    public static String NO_DAMAGE = "no_damage";
    public static String STATUS = "status";
    public static String KILL_CAMERA_COUNT = "kill_camera_count";
    public static String KILLED_LOCATION = "killed_location";
    public static String KILLED_LOCATION_FOR_KILL_CAMERA = "killed_location_for_kill_camera";
    public static String KILLER = "killer";
}
class Status {
    public static String KILL_CAMERA = "kill_camera";
    public static String PLAY = "playing";
}