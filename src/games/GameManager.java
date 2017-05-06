package games;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayers.SPlayer;
import stages.Stage;
import stages.StageManager;
import sTeams.STeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takumus on 2017/05/05.
 */
public class GameManager {
    private static JavaPlugin plugin;
    private static Map<String, GameBase> games = new HashMap<>();
    private static GameBase currentGame;
    private static List<SPlayer> currentPlayers = new ArrayList<>();
    public static void init(JavaPlugin plugin) {
        GameManager.plugin = plugin;
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (isPlaying()) currentGame.onTick();
        }, 0L, 1L);
    }
    public static boolean begin(String type, String stageName) {
        if (isPlaying()) {
            return false;
        }

        GameBase game = games.get(type.toLowerCase());
        if (game == null) {
            return false;
        }

        Stage stage = StageManager.getStage(stageName, game.getType());
        if (stage == null) {
            return false;
        }

        if (!game.begin(stage)) {
            return false;
        }
        currentGame = game;
        Bukkit.getServer().getPluginManager().registerEvents(game, plugin);
        return true;
    }
    public static void end() {
        if (!isPlaying()) {
            return;
        }
        currentGame.end();
        currentPlayers.clear();
        HandlerList.unregisterAll(currentGame);
        currentGame = null;
    }
    public static void selectTeam(SPlayer sp, STeam sTeam) {
        if (!isPlaying()) return;
        currentGame.selectTeam(sp, sTeam);
    }
    public static boolean isPlaying() {
        return currentGame != null;
    }
    public static void addGame(GameBase game) {
        games.put(game.getType(), game);
    }
    public static void addPlayer(SPlayer sp) {
        if (currentPlayers.contains(sp)) return;
        currentPlayers.add(sp);
    }
    public static void removePlayer(SPlayer sp) {
        if (!currentPlayers.contains(sp)) return;
        currentPlayers.remove(sp);
    }
    public static void clearCurrentPlayers() {
        currentPlayers.clear();
    }
    public static List<SPlayer> getCurrentPlayers() {
        return currentPlayers;
    }
}
