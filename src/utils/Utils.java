package utils;

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
}
