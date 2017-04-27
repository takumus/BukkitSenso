import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import splayer.SPlayerManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        SPlayerManager.init(this);
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
        System.out.println(args);
        return true;
    }
}