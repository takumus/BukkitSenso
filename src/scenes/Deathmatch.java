package scenes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.SItem;
import sPlayer.SPlayer;
import stages.Stage;

/**
 * Created by takumus on 2017/04/30.
 */
public class Deathmatch extends GameBase {
    public Deathmatch(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean start(Stage stage) {
        return false;
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onSPlayerDeath(SPlayer sPlayer, SItem weapon) {
        if(weapon != null) {
            this.message(weapon.getHolder().getNameWithColor() + ChatColor.GRAY + " -> " + sPlayer.getNameWithColor() + ChatColor.GRAY + ChatColor.ITALIC + " (" + weapon.getName() + ")"
            );
        }else {
            this.message(ChatColor.MAGIC + "?????" + ChatColor.RESET + ChatColor.GRAY + " -> " + sPlayer.getNameWithColor());
        }
    }
}
