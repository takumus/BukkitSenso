package sPlayer;
import games.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SPlayerManager implements Listener{
    private static SPlayerManager instance;
    private HashMap<Player, SPlayer> sPlayers;
    private SPlayerManager() {
        this.sPlayers = new HashMap<>();
    }
    public static void init(JavaPlugin plugin) {
        instance = new SPlayerManager();
        Bukkit.getServer().getPluginManager().registerEvents(instance, plugin);
        Bukkit.getServer().getOnlinePlayers().forEach((player) -> instance.addSPlayer(player));
    }
    public static Collection<SPlayer> getAllSPlayer() {
        return instance.sPlayers.values();
    }
    public static SPlayer getSPlayer(UUID uuid) {
        return SPlayerManager.getSPlayer(Bukkit.getServer().getPlayer(uuid));
    }
    public static SPlayer getSPlayer(Entity player) {
        return instance.sPlayers.get(player);
    }

    public static void message(String message) {
        getAllSPlayer().forEach((sp) -> sp.message(message));
    }
    public static void message(String message, SPlayer withoutMe) {
        getAllSPlayer().forEach((sp) -> {
            if (!sp.equals(withoutMe)) sp.message(message);
        });
    }
    //----------
    //
    //----------
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        this.addSPlayer(event.getPlayer());
    }
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        SPlayer sp = getSPlayer(event.getPlayer());
        GameManager.removePlayer(sp);
        sp.leaveSTeam();
        this.removeSPlayer(event.getPlayer());
    }
    public void addSPlayer(Player player) {
        Bukkit.getServer().broadcastMessage(player.getName() + "が入室した");
        SPlayer splayer = new SPlayer(player);
        this.sPlayers.put(player, splayer);
        splayer.sendTitle("ようこそ", "ようこそ", 60);
    }
    public void removeSPlayer(Player player) {
        this.sPlayers.remove(player);
    }
}
