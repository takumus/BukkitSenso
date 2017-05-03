package utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.material.MaterialData;

/**
 * Created by takumus on 2017/05/03.
 */
public class Effects {
    public static void blood(Location location) {
        Particle p = Particle.BLOCK_CRACK;
        location = location.clone();
        location.add(0, 1, 0);
        location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 100, 0, 0, 0, 1, new MaterialData(Material.REDSTONE_BLOCK));
    }
}
