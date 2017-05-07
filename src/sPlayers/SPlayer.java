package sPlayers;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import sItem.SItem;
import sItem.items.grenade.Grenade;
import sItem.items.superBow.SuperBow;
import sItem.items.masterSword.MasterSword;
import sTeams.STeam;
import utils.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumus on 2017/04/26.
 */
public class SPlayer {
    private static Scoreboard emptyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Player player;
    private Map<Class, SItem> sItems;
    private SItem lastDamagesWeapon;
    private SPlayerStatus status;
    private SMeta metadata;
    private DyeColor dyeColor;
    private ChatColor chatColor;
    private MaterialData coloredMaterial;
    private STeam sTeam;
    private int score;
    private int kill;
    private int death;
    public SPlayer(Player player) {
        this.player = player;
        this.sItems = new HashMap<>();
        this.addSItem(new MasterSword());
        this.addSItem(new SuperBow());
        this.addSItem(new Grenade());
        this.player.setHealth(20D);
        this.metadata = new SMeta();
        this.setDyeColor(DyeColor.LIGHT_BLUE);
        this.score = 0;
        this.kill = 0;
        this.death = 0;
        SPlayerDataManager.loadMeta(this);
    }
    public String getUUID() {
        return this.getPlayer().getUniqueId().toString();
    }
    public Player getPlayer() {
        return this.player;
    }
    public String getName() {
        return this.player.getDisplayName();
    }
    public String getNameWithColor() {
        return this.getStringWithColor(this.getName());
    }
    public String getNameWithColor(boolean bold) {
        return this.getStringWithColor(false, this.getName());
    }
    public String getStringWithColor(String string) {
        return this.getStringWithColor(true, string);
    }
    public String getStringWithColor(boolean bold, String string) {
        return ChatColor.RESET + "" + this.getChatColor().toString() + (bold?ChatColor.BOLD:"") + string + ChatColor.RESET;
    }
    public void addSItem(SItem sItem) {
        this.addSItem(sItem, this.sItems.size());
    }
    public void addSItem(SItem sItem, int id) {
        this.sItems.put(sItem.getClass(), sItem);
        sItem.initHolder(this, id);
    }
    public void clearSItems() {
        this.sItems.values().forEach((sItem) -> sItem.destroy());
        this.sItems.clear();
        this.player.getInventory().clear();
    }
    public void clearInventory() {
        this.getPlayer().getInventory().clear();
    }
    public Map<Class, SItem> getSItems() {
        return this.sItems;
    }
    public void setSItemsEnabled(boolean value) {
        this.sItems.values().forEach((sItem) -> {
            sItem.setEnabled(value);
            if (value) {
                sItem.initItem();
            }
        });
    }
    public void damage(SItem weapon, double damage) {
        this.lastDamagesWeapon = weapon;
        this.player.damage(damage);
        this.clearLastDamageWeapon();
    }
    public SItem getLastDamagesWeapon() {
        return this.lastDamagesWeapon;
    }
    public void clearLastDamageWeapon() {
        this.lastDamagesWeapon = null;
    }

    public void sendTitle(String main, String sub, int time) {
        this.sendTitle(main, sub, time, 0, 0);
    }
    public void sendTitle(String main, String sub, int time, int fadeInTime, int fadeOutTime) {
        this.player.sendTitle(main, sub, fadeInTime, time, fadeOutTime);
    }

    public void setStatus(SPlayerStatus status) {
        this.status = status;
    }
    public SPlayerStatus getStatus() {
        return this.status;
    }

    public SMeta getMeta() {
        return this.metadata;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }
    public ChatColor getChatColor() {
        return this.chatColor;
    }
    public void setDyeColor(DyeColor color) {
        this.dyeColor = color;
        this.coloredMaterial = new Wool(color);
        this.chatColor = ColorMap.getChatColor(this.dyeColor);
        this.getPlayer().setPlayerListName(this.getNameWithColor());
    }
    public void playSound(Sound sound, float volume, float pitch, boolean world) {
        Player p = this.getPlayer();
        if (world) {
            p.getWorld().playSound(p.getLocation(), sound, volume, pitch);
        }else {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    public void addPotion(PotionEffect effect) {
        this.getPlayer().addPotionEffect(effect);
    }
    public void removePotion(PotionEffectType effectType) {
        this.getPlayer().removePotionEffect(effectType);
    }
    public void removeAllPotion() {
        this.getPlayer().getActivePotionEffects().forEach((effect) -> {
            this.removePotion(effect.getType());
        });
    }
    public void lookAt(Location target) {
        Utils.lookAt(this.getPlayer(), target);
    }
    public void lookAt(Entity target) {
        Utils.lookAt(this.getPlayer(), target.getLocation());
    }
    public void message(String message) {
        this.getPlayer().sendMessage(message);
    }

    public void blood(Location location, int amount) {
        Effects.blood(location, amount, coloredMaterial);
    }
    public void blood(int amount) {
        Effects.blood(this.getPlayer().getLocation().add(0, 0.5, 0), amount, coloredMaterial);
    }
    public void joinSTeam(STeam sTeam) {
        this.sTeam = sTeam;
    }
    public void leaveSTeam() {
        if (this.sTeam != null) this.sTeam.removeSPlayer(this);
    }
    public STeam getSTeam() {
        return  this.sTeam;
    }

    public void addItemToMain(int i, ItemStack item) {
        this.getPlayer().getInventory().setItem(i, item);
    }
    public void addItemToSub(ItemStack item) {
        Inventory inv = this.getPlayer().getInventory();
        for (int i = 9; i < 36; i ++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
                inv.setItem(i, item);
                return;
            }
        }
    }
    public void showScoreboard(Scoreboard scoreboard) {
        this.getPlayer().setScoreboard(scoreboard);
    }
    public void hideScoreboard() {
        this.getPlayer().setScoreboard(emptyScoreboard);
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void addScore(int score) {
        this.score += score;
    }
    public int getScore() {
        return this.score;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }
    public void addKill(int kill) {
        this.kill += kill;
    }
    public int getKill() {
        return this.kill;
    }

    public void setDeath(int death) {
        this.death = death;
    }
    public void addDeath(int death) {
        this.death += death;
    }
    public int getDeath() {
        return this.death;
    }
    public void showDamageArrow(SPlayer damager) {
        if (damager.equals(this)) return;
        DamageArrow.show(this, damager);
    }
    public void hideDamageArrow() {
        DamageArrow.hide(this);
    }
    public void setKillMessage(String message) {
        if (message.equals("")) {
            message = "Respawning...";
        }
        this.message("Your kill message is '" + message + "'");
        this.getPermanentMeta().put("killmessage", message);
    }
    public String getKillMessage() {
        String message = this.getPermanentMeta().get("killmessage");
        if (message == null) return "Respawning...";
        return message;
    }
    public Map<String, String> getPermanentMeta() {
        return (Map<String, String>)this.getMeta().getObject("permanent");
    }
}
