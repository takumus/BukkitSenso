package utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.material.MaterialData;

public class Effects {
    private static final MaterialData BLOOD_MATERIAL_DATA = new MaterialData(Material.REDSTONE_BLOCK);
    public static void blood(Location location, int amount) {
        blood(location, amount, BLOOD_MATERIAL_DATA);
    }
    public static void blood(Location location, int amount, MaterialData data) {
        location.getWorld().spawnParticle(
                Particle.BLOCK_CRACK,
                location,
                amount,
                0, 0, 0,
                1,
                data
        );
    }

    public static void strikeLightning(Location location) {
        location.getWorld().strikeLightningEffect(location);
    }
}
