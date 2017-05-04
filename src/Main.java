import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.SItemManager;
import sItem.items.grenade.GrenadeController;
import sItem.items.masterSword.MasterSwordController;
import sItem.items.superBow.SuperBowController;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import scenes.Deathmatch;
import stages.StageManager;
import stages.stageEditor.StageEditorManager;
import utils.CommandArgsWrapper;
import utils.MetadataManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        StageManager.init(this);
        MetadataManager.init(this);
        StageEditorManager.init(this);
        SItemManager.init(this);
        SPlayerManager.init(this);
        // Bukkit.getServer().getPluginManager().registerEvents(new Deathmatch(this), this);

        SItemManager.addSItem(new GrenadeController());
        SItemManager.addSItem(new SuperBowController());
        SItemManager.addSItem(new MasterSwordController());
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
        CommandArgsWrapper a = new CommandArgsWrapper(args);

        if (sender instanceof Player) {
            SPlayer sp = SPlayerManager.getSPlayer((Player) sender);
            if(a.at(0).equalsIgnoreCase("stage")) {
                if (a.at(1).equalsIgnoreCase("edit")) {
                    if (a.at(2).equalsIgnoreCase("begin")) {
                        StageEditorManager.beginEdit(a.at(3), a.at(4), sp);
                    }else if(a.at(2).equalsIgnoreCase("end")) {
                        StageEditorManager.endEdit();
                    }
                }else if (a.at(1).equalsIgnoreCase("save")) {
                    StageManager.saveConfig();
                    sp.message("stage saved");
                }
            }
        }
        return true;
    }
}