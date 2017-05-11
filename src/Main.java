import games.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sItem.SItemManager;
import sPlayers.SPlayer;
import sPlayers.SPlayerDataManager;
import sPlayers.SPlayerManager;
import sScoreboards.SScoreboard;
import stages.StageManager;
import stages.stageEditor.StageEditorManager;
import sTeams.TeamSelector;
import sTimers.STimer;
import utils.CommandArgsWrapper;
import utils.DamageArrow;
import utils.DelayedTask;
import utils.MetadataManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        SScoreboard.init();
        DamageArrow.init(this);
        DelayedTask.init(this);
        StageManager.init(this);
        SPlayerDataManager.init(this);
        STimer.init(this);
        TeamSelector.init(this);
        MetadataManager.init(this);
        StageEditorManager.init(this);
        SItemManager.init(this);
        SPlayerManager.init(this);
        GameManager.init(this);
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
        CommandArgsWrapper a = new CommandArgsWrapper(args);
        if (sender instanceof Player) {
            SPlayer sp = SPlayerManager.getSPlayer((Player) sender);
            if (commandLabel.equalsIgnoreCase("stage")) {
                if (a.at(0).equalsIgnoreCase("edit")) {
                    if (a.at(1).equalsIgnoreCase("start")) {
                        StageEditorManager.start(a.at(2), a.at(3), sp);
                    }else if(a.at(1).equalsIgnoreCase("stop")) {
                        StageEditorManager.stop();
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
                if (a.at(0).equalsIgnoreCase("start")){
                    GameManager.start(a.at(1), a.at(2));
                }else if (a.at(0).equalsIgnoreCase("stop")){
                    GameManager.stop();
                }
            }else if (commandLabel.equalsIgnoreCase("team")) {
                TeamSelector.showTeamSelector(sp);
            }else if (commandLabel.equalsIgnoreCase("killmessage")) {
                if (a.at(0).equalsIgnoreCase("set")) {
                    sp.setKillMessage(a.at(1));
                    SPlayerDataManager.saveMeta(sp);
                }else if (a.at(0).equalsIgnoreCase("reset")) {
                    sp.setKillMessage("");
                    SPlayerDataManager.saveMeta(sp);
                }
            }
        }

        return true;
    }
}