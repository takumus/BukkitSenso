package utils;

import games.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;

/**
 * Created by takumus on 2017/05/07.
 */
public class DamageArrow {
    private static String[] arrows = {"↑", "↗", "→", "↘", "↓", "↙", "←", "↖"};
    private static double unit = Math.PI * 2 / arrows.length;
    private static double offset = unit / 2;
    public static void init(JavaPlugin plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            GameManager.getCurrentPlayers().forEach((sp) -> {
                if (!sp.getMeta().getBoolean("damage_arrow")) return;
                int count = sp.getMeta().getInt("damage_arrow_count");
                Location loc = sp.getMeta().getLocation("damage_arrow_location");
                count ++;
                if (count > 20 * 3) {
                    sp.getMeta().set("damage_arrow", false);
                    return;
                }
                sp.getMeta().set("damage_arrow_count", count);
                sendArrow(sp, loc);
            });
        }, 0L, 1L);
    }
    private static void sendArrow(SPlayer victim, Location damager) {
        Location vL = victim.getPlayer().getLocation();
        double dx = damager.getX() - vL.getX();
        double dz = damager.getZ() - vL.getZ();
        double rad = normalize(Math.atan2(dz, dx) - Math.PI / 2);
        double rad2 = normalize(vL.getYaw() / 180 * Math.PI);
        double diff = normalize(rad - rad2 + offset);
        String arrow = arrows[(int)(diff / unit)];
        victim.sendTitle(ChatColor.RED + arrow, "", 10, 0, 10);
    }
    public static void hide(SPlayer sp) {
        sp.getMeta().set("damage_arrow", false);
    }
    public static void show(SPlayer sp, Location target) {

        sp.getMeta().set("damage_arrow", true);
        sp.getMeta().set("damage_arrow_location", target.clone());
        sp.getMeta().set("damage_arrow_count", 0);
    }
    private static double normalize(double rad) {
        rad = rad % (Math.PI * 2);
        if (rad < 0) rad += Math.PI * 2;
        return rad;
    }
}
