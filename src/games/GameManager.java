package games;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;
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
    private static List<SPlayer> inGamePlayers = new ArrayList<>();
    public static void init(JavaPlugin plugin) {
        GameManager.plugin = plugin;
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (isPlaying()) currentGame.onTick();
        }, 0L, 1L);

        //ゲーム初期化
        addGame(new Deathmatch());
    }
    public static boolean start(String type, String stageName) {
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

        if (!game.start(stage)) {
            return false;
        }
        currentGame = game;
        Bukkit.getServer().getPluginManager().registerEvents(game, plugin);
        return true;
    }
    public static void stop() {
        if (!isPlaying()) {
            return;
        }
        currentGame.stop();
        inGamePlayers.forEach((sp) -> {
            sp.leaveSTeam();
            sp.getMeta().set("game_manager_is_playing", false);
        });
        inGamePlayers.clear();
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
        if (inGamePlayers.contains(sp)) return;
        sp.getMeta().set("game_manager_is_playing", true);
        inGamePlayers.add(sp);
    }
    public static void removePlayer(SPlayer sp) {
        if (!inGamePlayers.contains(sp)) return;
        sp.getMeta().set("game_manager_is_playing", false);
        inGamePlayers.remove(sp);
    }
    public static List<SPlayer> getInGamePlayers() {
        return inGamePlayers;
    }
    public static List<SPlayer> getNotInGamePlayers() {
        List<SPlayer> list = new ArrayList<>();
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            if (sp.getMeta().getBoolean("game_manager_is_playing")) return;
            list.add(sp);
        });
        return list;
    }
}
