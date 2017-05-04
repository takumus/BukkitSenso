package utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Utils {
    public static void lookAt(Entity entity, Location target) {
        Location loc = entity.getLocation();
        Location diff =  target.clone().subtract(loc);
        loc.setDirection(new Vector(diff.getX(), diff.getY(), diff.getZ()));
        entity.teleport(loc);
    }
    public static String stringProgressBar(int length, double progress) {
        double l = progress * length;
        String bar = "";
        for (int i = 0; i < length; i ++) {
            bar += i < l ? (l - i > 0.5D ? ChatColor.RED + "#" : ChatColor.RED + "=") : ChatColor.GRAY + "-";
        }
        return bar + ChatColor.RESET;
    }
}
