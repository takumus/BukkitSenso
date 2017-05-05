package utils;

import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * Created by takumus on 2017/05/05.
 */
public class CreatorUtils {
    public static ItemStack createArmour(Material material, DyeColor color, NBTTagCompound tag) {
        ItemStack i = new ItemStack(material, 1);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(i);
        s.setTag(tag);
        i = CraftItemStack.asBukkitCopy(s);
        LeatherArmorMeta meta = (LeatherArmorMeta)i.getItemMeta();
        meta.setColor(color.getColor());
        i.setItemMeta(meta);
        return i;
    }
    public static ItemStack createArmour(Material material, DyeColor color) {
        return createArmour(material, color, new NBTTagCompound());
    }
    public static PigZombie createNoAIZombie(Location location) {
        location.setPitch(0);
        PigZombie z = (PigZombie) location.getWorld().spawnEntity(location, EntityType.PIG_ZOMBIE);
        z.setBaby(false);
        z.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
        EntityLiving ez = ((CraftLivingEntity)z).getHandle();
        NBTTagCompound compound = new NBTTagCompound();
        ez.c(compound);
        compound.setByte("NoAI", (byte) 1);
        ez.f(compound);
        ez.setNoGravity(true);
        return z;
    }
}
