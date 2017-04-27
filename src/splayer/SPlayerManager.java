package splayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SPlayerManager implements Listener{
    private static SPlayerManager instance;
    private SPlayerManager() {
    }
    public static void init(JavaPlugin plugin) {
        instance = new SPlayerManager();
        Bukkit.getServer().getPluginManager().registerEvents(instance, plugin);
        Bukkit.getServer().getOnlinePlayers().forEach((player) -> instance.addSPlayer(player));
    }
    //public
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        this.addSPlayer(event.getPlayer());
    }
    public void addSPlayer(Player player) {
        Bukkit.getServer().broadcastMessage(player.getName() + "が入室した");
    }
}
