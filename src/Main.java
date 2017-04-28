import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.SItemManager;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        SItemManager.init(this);
        SPlayerManager.init(this);
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
        if (sender instanceof Player) {
            SPlayer sp = SPlayerManager.getSPlayer((Player) sender);
            sp.clearSItems();
        }
        return true;
    }
}