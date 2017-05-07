package utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import sPlayers.SPlayer;

/**
 * Created by takumus on 2017/05/07.
 */
public class DamageArrow {
    private static String[] arrows = {"↑", "↗", "→", "↘", "↓", "↙", "←", "↖"};
    private static double unit = Math.PI * 2 / arrows.length;
    private static double offset = unit / 2;
    public static void show(SPlayer victim, Location damager) {
        Location vL = victim.getPlayer().getLocation();

        double dx = damager.getX() - vL.getX();
        double dz = damager.getZ() - vL.getZ();

        double rad = normalize(Math.atan2(dz, dx) - Math.PI / 2);
        double rad2 = normalize(vL.getYaw() / 180 * Math.PI);
        double diff = normalize(rad - rad2 + offset);

        String arrow = arrows[(int)(diff / unit)];
        victim.sendTitle(ChatColor.RED + arrow, "", 10, 0, 10);
    }
    private static double normalize(double rad) {
        rad = rad % (Math.PI * 2);
        if (rad < 0) rad += Math.PI * 2;
        return rad;
    }
}
