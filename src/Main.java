import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.SItem;
import sItem.SItemManager;
import sItem.items.grenade.GrenadeController;
import sItem.items.masterSword.MasterSwordController;
import sItem.items.superBow.SuperBowController;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import scenes.Deathmatch;
import scenes.GameBase;
import stages.StageManager;
import utils.MetadataManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        StageManager.init(this);
        MetadataManager.init(this);
        SItemManager.init(this);
        SPlayerManager.init(this);
        Bukkit.getServer().getPluginManager().registerEvents(new Deathmatch(this), this);

        SItemManager.addSItem(new GrenadeController());
        SItemManager.addSItem(new SuperBowController());
        SItemManager.addSItem(new MasterSwordController());
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