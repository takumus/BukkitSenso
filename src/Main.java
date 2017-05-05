import games.Deathmatch;
import games.GameManager;
import org.bukkit.ChatColor;
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
        GameManager.init(this);

        SItemManager.addSItem(new GrenadeController());
        SItemManager.addSItem(new SuperBowController());
        SItemManager.addSItem(new MasterSwordController());

        GameManager.addGame(new Deathmatch());
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
        CommandArgsWrapper a = new CommandArgsWrapper(args);

        if (sender instanceof Player) {
            SPlayer sp = SPlayerManager.getSPlayer((Player) sender);
            if (a.at(0).equalsIgnoreCase("stage")) {
                if (a.at(1).equalsIgnoreCase("edit")) {
                    if (a.at(2).equalsIgnoreCase("begin")) {
                        StageEditorManager.begin(a.at(3), a.at(4), sp);
                    }else if(a.at(2).equalsIgnoreCase("end")) {
                        StageEditorManager.end();
                    }else if(a.at(2).equalsIgnoreCase("save")) {
                        StageEditorManager.save();
                    }
                }else if (a.at(1).equalsIgnoreCase("save")) {
                    StageManager.saveConfig();
                    sp.message("stage saved");
                }else if (a.at(1).equalsIgnoreCase("list")) {
                    if (a.at(2).length() > 0) {
                        sp.message(ChatColor.YELLOW + "--stages of " + a.at(2) + "--");
                    }else {
                        sp.message(ChatColor.YELLOW + "--stages--");
                    }
                    StageManager.getStages(a.at(2)).forEach((stage) -> {
                        sp.message(stage.getName());
                    });
                    sp.message(ChatColor.YELLOW + "----------");
                }
            }else {
                if (a.at(0).equalsIgnoreCase("game")) {
                    if (a.at(1).equalsIgnoreCase("begin")){
                        GameManager.begin(a.at(2), a.at(3));
                    }else if (a.at(1).equalsIgnoreCase("end")){
                        GameManager.end();
                    }
                }
            }
        }
        return true;
    }
}