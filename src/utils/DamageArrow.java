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
            GameManager.getCurrentPlayers().forEach((victim) -> {
                if (!victim.getMeta().getBoolean("damage_arrow")) return;
                int count = victim.getMeta().getInt("damage_arrow_count");
                SPlayer damager = victim.getMeta().getSPlayer("damage_arrow_damager");
                count ++;
                if (count > 20 * 4) {
                    victim.getMeta().set("damage_arrow", false);
                    return;
                }
                victim.getMeta().set("damage_arrow_count", count);

                Location vL = victim.getPlayer().getLocation();
                Location dL = damager.getPlayer().getLocation();
                double dx = dL.getX() - vL.getX();
                double dz = dL.getZ() - vL.getZ();
                double rad = normalize(Math.atan2(dz, dx) - Math.PI / 2);
                double rad2 = normalize(vL.getYaw() / 180 * Math.PI);
                double diff = normalize(rad - rad2 + offset);
                String arrow = arrows[(int)(diff / unit)];
                victim.sendTitle(ChatColor.RED + arrow, count < 12 ? damager.getNameWithColor(false) : "", 10, 0, 1);
            });
        }, 0L, 1L);
    }
    public static void hide(SPlayer sp) {
        sp.getMeta().set("damage_arrow", false);
    }
    public static void show(SPlayer victim, SPlayer damager) {

        victim.getMeta().set("damage_arrow", true);
        victim.getMeta().set("damage_arrow_damager", damager);
        victim.getMeta().set("damage_arrow_count", 0);
    }
    private static double normalize(double rad) {
        rad = rad % (Math.PI * 2);
        if (rad < 0) rad += Math.PI * 2;
        return rad;
    }
}
