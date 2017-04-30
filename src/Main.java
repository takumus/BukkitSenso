import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.SItem;
import sItem.SItemManager;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import scenes.GameBase;
import utils.MetadataManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        MetadataManager.init(this);
        SItemManager.init(this);
        SPlayerManager.init(this);
        Bukkit.getServer().getPluginManager().registerEvents(new GameBase(this) {
            @Override
            public void onSPlayerDeath(SPlayer sPlayer, SItem weapon) {
                if(weapon != null) {
                    Bukkit.getServer().broadcastMessage(weapon.getHolder().getName() + " -> " + sPlayer.getName() + "(" + weapon.getName() + ")");
                }else {
                    Bukkit.getServer().broadcastMessage("? ->" + sPlayer.getName());
                }
            }
        }, this);
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
        if (sender instanceof Player) {
            SPlayer sp = SPlayerManager.getSPlayer((Player) sender);
            if (args[0].equalsIgnoreCase("begin")) {
                sp.setSItemsEnabled(true);
                Bukkit.getServer().broadcastMessage("begin");
            }else if(args[0].equalsIgnoreCase("end")) {
                sp.setSItemsEnabled(false);
                Bukkit.getServer().broadcastMessage("end");
            }
        }
        return true;
    }
}