package games;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import stages.Stage;
import stages.StageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumus on 2017/05/05.
 */
public class GameManager {
    private static JavaPlugin plugin;
    private static Map<String, GameBase> games = new HashMap<>();
    private static GameBase currentGame;
    public static void init(JavaPlugin plugin) {
        GameManager.plugin = plugin;
    }
    public static boolean begin(String name, String stageName) {
        if (isPlaying()) {
            return false;
        }

        GameBase game = games.get(name.toLowerCase());
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
        HandlerList.unregisterAll(currentGame);
        currentGame = null;
    }
    public static boolean isPlaying() {
        return currentGame != null;
    }
    public static void addGame(GameBase game) {
        games.put(game.getType(), game);
    }
}
