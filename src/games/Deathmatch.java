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
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;
import sScoreboards.SScoreboard;
import sTeams.STeam;
import sTeams.TeamSelector;
import utils.*;
import stages.Stage;

import java.util.*;

/**
 * Created by takumus on 2017/04/30.
 */
public class Deathmatch extends GameBase {
    private Map<String, List<Location>> teamSpawns;
    private SScoreboard scoreboard;
    private List<STeam> teams;
    public Deathmatch() {
        super("tdm");
        this.scoreboard = new SScoreboard("Scores");
    }
    private void spawn(SPlayer sp) {
        sp.getPlayer().setHealth(20D);
        sp.clearInventory();
        sp.setSItemsEnabled(true);
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

        this.teleportToSpawn(sp);
        DelayedTask.task(() -> {
            sp.getPlayer().setGameMode(GameMode.SURVIVAL);
        }, 1L);
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
                    ChatColor.YELLOW + killer.getKillMessage(),
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

    private void teleportToSpawn(SPlayer sp) {
        String name = sp.getSTeam().getName();
        List<Location> spawns = teamSpawns.get(name.toLowerCase());
        Location location = spawns.get((int)(Math.random() * spawns.size()));
        sp.getPlayer().teleport(location);
    }
    private void updateScore() {
        this.teams.forEach((team) -> {
            if (team.getMembers().size() > 0) {
                this.scoreboard.setScore(team.getNameWithColor(), team.getScore());
            }
        });
    }
    //----------------------------------------------------------------------//
    // ゲームに参加
    //----------------------------------------------------------------------//
    @Override
    public void selectTeam(SPlayer sp, STeam sTeam) {
        sTeam.addSPlayer(sp);
        sp.setDyeColor(sTeam.getDyeColor());
        GameManager.addPlayer(sp);
        sp.showScoreboard(this.scoreboard.getScoreboard());
        this.spawn(sp);
        this.updateScore();
    }

    @Override
    public boolean begin(Stage stage) {
        this.teamSpawns = new HashMap<>();
        stage.getSpawns().forEach((spawn) -> {
            String name = spawn.getMeta("team");
            List<Location> spawns = teamSpawns.get(name.toLowerCase());
            if (spawns == null) {
                spawns = new ArrayList<>();
                teamSpawns.put(name.toLowerCase(), spawns);
            }
            spawns.add(spawn.getLocation());
        });
        this.teams = new ArrayList<>();
        this.teamSpawns.keySet().forEach((teamName) -> {
            STeam team = new STeam(ColorMap.getDyeColor(teamName));
            team.setScore(0);
            team.setDeath(0);
            team.setKill(0);
            this.teams.add(team);
        });
        TeamSelector.setTeamsMap(this.teams);
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            TeamSelector.showTeamSelector(sp);
        });
        return true;
    }

    @Override
    public boolean end() {
        GameManager.getCurrentPlayers().forEach((sp) -> {
            sp.setSItemsEnabled(false);
            sp.clearInventory();
        });
        return false;
    }

    @Override
    public void onTick() {
        GameManager.getCurrentPlayers().forEach((sp) -> {
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
                    victim.getStringWithColor("You") + " " + ChatColor.WHITE + "Killed " + ChatColor.GRAY + "yourself...!",
                    ChatColor.RED + weapon.getName(),
                    20 * 3,
                    0, 20
            );
            this.message(killer.getNameWithColor() + ChatColor.GRAY + " killed " + ChatColor.WHITE + "oneself" + ChatColor.GRAY + ChatColor.ITALIC + " (" + weapon.getName() + ")");
        }else {
            // 他殺
            victim.sendTitle(
                    victim.getStringWithColor("You") + " " + ChatColor.WHITE + "Killed by " + killer.getNameWithColor(),
                    ChatColor.RED + weapon.getName(),
                    20 * 3,
                    0, 20
            );
            killer.sendTitle(
                    "",
                    killer.getStringWithColor("You") + " " + ChatColor.WHITE + "Killed " + victim.getNameWithColor(),
                    20,
                    0, 5
            );
            this.message(killer.getNameWithColor() + ChatColor.GRAY + " killed " + victim.getNameWithColor() + ChatColor.GRAY + ChatColor.ITALIC + " (" + weapon.getName() + ")");
        }
        victim.hideDamageArrow();
        killer.hideDamageArrow();

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

        //スコア
        victim.addDeath(1);
        victim.getSTeam().addDeath(1);

        killer.addKill(1);
        killer.getSTeam().addKill(1);
        int score = 100;
        killer.addScore(score);
        killer.getSTeam().addScore(score);

        this.updateScore();
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

        if (victim.getSTeam() == null) {
            event.setCancelled(true);
            return;
        }

        //同チームからのダメージは無効
        if (victim.getSTeam().equals(victim.getLastDamagesWeapon().getHolder().getSTeam()) && !victim.equals(victim.getLastDamagesWeapon().getHolder())) {
            event.setCancelled(true);
            return;
        }

        victim.showDamageArrow(victim.getLastDamagesWeapon().getHolder());
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
    public static String NONE = "none";
    public static String KILL_CAMERA = "kill_camera";
    public static String PLAY = "playing";
}