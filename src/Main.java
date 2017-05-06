import games.Deathmatch;
import games.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
import teams.TeamSelector;
import utils.CommandArgsWrapper;
import utils.MetadataManager;

import java.util.Arrays;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        StageManager.init(this);
        TeamSelector.init(this);
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
            if (commandLabel.equalsIgnoreCase("stage")) {
                if (a.at(0).equalsIgnoreCase("edit")) {
                    if (a.at(1).equalsIgnoreCase("begin")) {
                        StageEditorManager.begin(a.at(2), a.at(3), sp);
                    }else if(a.at(1).equalsIgnoreCase("end")) {
                        StageEditorManager.end();
                    }else if(a.at(1).equalsIgnoreCase("save")) {
                        StageEditorManager.save();
                    }
                }else if (a.at(0).equalsIgnoreCase("save")) {
                    StageManager.saveConfig();
                    sp.message("stage saved");
                }else if (a.at(0).equalsIgnoreCase("list")) {
                    if (a.at(1).length() > 0) {
                        sp.message(ChatColor.YELLOW + "--stages of " + a.at(1) + "--");
                    }else {
                        sp.message(ChatColor.YELLOW + "--stages--");
                    }
                    StageManager.getStages(a.at(1)).forEach((stage) -> {
                        sp.message(stage.getName());
                    });
                    sp.message(ChatColor.YELLOW + "----------");
                }
            }else if (commandLabel.equalsIgnoreCase("game")) {
                if (a.at(0).equalsIgnoreCase("begin")){
                    GameManager.begin(a.at(1), a.at(2));
                }else if (a.at(0).equalsIgnoreCase("end")){
                    GameManager.end();
                }
            }else if (commandLabel.equalsIgnoreCase("team")) {
                TeamSelector.showTeamSelector(sp);
            }
        }

        return true;
    }
}