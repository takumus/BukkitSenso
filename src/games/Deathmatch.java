package games;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sItem.SItem;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import utils.SMeta;
import stages.Stage;
import utils.ColorMap;
import utils.CreatorUtils;
import utils.Effects;
import utils.Utils;

/**
 * Created by takumus on 2017/04/30.
 */
public class Deathmatch extends GameBase {
    public Deathmatch() {
        super("tdm");
    }
    private void spawn(SPlayer sp) {
        sp.setDyeColor(ColorMap.getRandomDyeColor());
        sp.clearInventory();
        sp.setSItemsEnabled(true);
        sp.getPlayer().setGameMode(GameMode.SURVIVAL);
        sp.getPlayer().setWalkSpeed(0.3f);
        sp.getPlayer().getEquipment().setChestplate(
                CreatorUtils.createArmour(
                        Material.LEATHER_CHESTPLATE,
                        sp.getDyeColor()
                )
        );
        sp.getPlayer().getEquipment().setLeggings(
                CreatorUtils.createArmour(
                        Material.LEATHER_LEGGINGS,
                        sp.getDyeColor()
                )
        );
        sp.getPlayer().getEquipment().setBoots(
                CreatorUtils.createArmour(
                        Material.LEATHER_BOOTS,
                        sp.getDyeColor()
                )
        );
        SMeta meta = sp.getMeta();
        meta.set(MetaKey.KILL_CAMERA_COUNT, 0);
        meta.set(MetaKey.STATUS, Status.PLAY);
        meta.set(MetaKey.NO_DAMAGE, false);
        meta.set(MetaKey.KILLER, null);
        meta.set(MetaKey.KILLED_LOCATION, null);
    }
    private void killCamera(SPlayer sp) {
        SMeta meta = sp.getMeta();
        Location killedPosForCamera = meta.getLocation(MetaKey.KILLED_LOCATION_FOR_KILL_CAMERA);
        int count = meta.getInt(MetaKey.KILL_CAMERA_COUNT);
        meta.set(MetaKey.KILL_CAMERA_COUNT, count + 1);

        sp.blood(meta.getLocation(MetaKey.KILLED_LOCATION).clone().add(0, Math.random() + 0.2, 0), 30);

        SPlayer killer = meta.getSPlayer(MetaKey.KILLER);
        if (count > 40) {
            double progress = (count - 40) / 80D;
            sp.sendTitle(
                    ChatColor.YELLOW + "Respawning...",
                    "[" + Utils.stringProgressBar(10, progress) + "]", 5
            );
        }
        //自殺だったら
        if (killer.equals(sp)) {
            sp.getPlayer().teleport(killedPosForCamera);
            if (count > 120) {
                this.spawn(sp);
            }
            return;
        }

        //他殺の場合キルカメモード
        Location targetLocation = killer.getMeta().getString(MetaKey.STATUS).equals(Status.PLAY) ? killer.getPlayer().getLocation() : killer.getMeta().getLocation(MetaKey.KILLED_LOCATION);
        sp.lookAt(targetLocation);
        if (count < 40) {
            if (count % 10 == 0) Effects.strikeLightning(meta.getLocation(MetaKey.KILLED_LOCATION));
        }
        if (count < 40) {
            double vy = (Math.cos(count / 30D * Math.PI * 2 + Math.PI) + 1) / 2;
            sp.getPlayer().teleport(killedPosForCamera.add(0, vy, 0));
        }else if (count < 120){

            double r = (count - 40) / 80D * Math.PI * 2;
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
        sp.lookAt(targetLocation);
    }
    @Override
    public boolean begin(Stage stage) {
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            this.spawn(sp);
        });
        return true;
    }

    @Override
    public boolean end() {
        return false;
    }

    @Override
    public void onTick() {
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            sp.getPlayer().setSneaking(true);
            sp.getPlayer().getWorld().setTime(0);
            sp.getPlayer().getWorld().setStorm(false);
            sp.getPlayer().getWorld().setThundering(false);
            sp.getPlayer().setFoodLevel(30);
            SMeta meta = sp.getMeta();
            if (meta.getString(MetaKey.STATUS).equals(Status.KILL_CAMERA)) {
                this.killCamera(sp);
            }
        });
    }

    @Override
    public void onSPlayerDeath(SPlayer victim, SItem weapon) {
        SPlayer killer = weapon.getHolder();
        if (victim.equals(killer)) {
            // 自殺
            victim.sendTitle(
                    killer.getChatColor() + "You " + ChatColor.WHITE + "Killed " + ChatColor.GRAY + "yourself...!",
                    ChatColor.RED + weapon.getName(), 20 * 3,
                    0, 20
            );
            this.message(killer.getNameWithColor() + ChatColor.GRAY + " killed " + ChatColor.WHITE + "oneself" + ChatColor.GRAY + ChatColor.ITALIC + " (" + weapon.getName() + ")");
        }else {
            // 他殺
            victim.sendTitle(
                    killer.getChatColor() + "You " + ChatColor.WHITE + "Killed by " + killer.getNameWithColor(),
                    ChatColor.RED + weapon.getName(), 20 * 3,
                    0, 20
            );
            killer.sendTitle(
                    "",
                    killer.getChatColor() + "You " + ChatColor.WHITE + "Killed " + victim.getNameWithColor(), 10,
                    0, 5
            );
            this.message(killer.getNameWithColor() + ChatColor.GRAY + " killed " + victim.getNameWithColor() + ChatColor.GRAY + ChatColor.ITALIC + " (" + weapon.getName() + ")");

        }

        victim.getMeta().set(MetaKey.STATUS, Status.KILL_CAMERA);
        victim.getMeta().set(MetaKey.KILL_CAMERA_COUNT, 0);
        victim.getMeta().set(MetaKey.NO_DAMAGE, true);
        victim.getMeta().set(MetaKey.KILLED_LOCATION, victim.getPlayer().getLocation().clone());
        victim.getMeta().set(MetaKey.KILLED_LOCATION_FOR_KILL_CAMERA, victim.getPlayer().getLocation().clone());
        victim.getMeta().set(MetaKey.KILLER, weapon.getHolder());

        victim.clearInventory();
        victim.getPlayer().setHealth(20D);
        victim.addPotion(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1));
        victim.addPotion(new PotionEffect(PotionEffectType.WITHER, 30, 1));
        victim.getPlayer().setGameMode(GameMode.SPECTATOR);

        victim.playSound(Sound.ENTITY_ENDERMEN_DEATH, 1, 1, false);
        killer.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1, false);
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
        victim.blood(100);
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