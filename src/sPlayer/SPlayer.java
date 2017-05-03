package sPlayer;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sItem.SItem;
import sItem.items.SuperBow;
import sItem.items.Sword;
import utils.ColorMap;
import utils.Utils;

import java.util.ArrayList;

/**
 * Created by takumus on 2017/04/26.
 */
public class SPlayer {
    private Player player;
    private ArrayList<SItem> sItems;
    private SItem lastDamagesWeapon;
    private SPlayerStatus status;
    private SPlayerMeta metadata;
    private DyeColor dyeColor;
    public SPlayer(Player player) {
        this.player = player;
        this.sItems = new ArrayList<>();
        this.addSItem(new Sword());
        this.addSItem(new SuperBow());
        this.player.setHealth(20D);
        this.metadata = new SPlayerMeta();
        dyeColor = DyeColor.GREEN;
    }
    public Player getPlayer() {
        return this.player;
    }
    public String getName() {
        return this.player.getDisplayName();
    }
    public String getNameWithColor() {
        return ChatColor.RESET.toString() + ChatColor.BOLD.toString() + this.getChatColor().toString() + this.player.getDisplayName() + ChatColor.RESET.toString();
    }
    public void addSItem(SItem sItem) {
        this.addSItem(sItem, this.sItems.size());
    }
    public void addSItem(SItem sItem, int id) {
        this.sItems.add(sItem);
        sItem.initHolder(this, id);
    }
    public void clearSItems() {
        this.sItems.forEach((sItem) -> sItem.destroy());
        this.sItems.clear();
        this.player.getInventory().clear();
    }
    public void clearInventory() {
        this.getPlayer().getInventory().clear();
    }
    public ArrayList<SItem> getSItems() {
        return this.sItems;
    }
    public void setSItemsEnabled(boolean value) {
        this.sItems.forEach((sItem) -> {
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

    public SPlayerMeta getMeta() {
        return this.metadata;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }
    public ChatColor getChatColor() {
        return ColorMap.getChatColor(this.dyeColor);
    }
    public void setDyeColor(DyeColor color) {
        this.dyeColor = color;
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
}
