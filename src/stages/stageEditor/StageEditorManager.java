package stages.stageEditor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayer.SPlayer;
import stages.Stage;
import stages.StageManager;
import stages.stageEditor.editors.TDMStageEditor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumus on 2017/05/04.
 */
public class StageEditorManager {
    private static JavaPlugin plugin;
    private static StageEditor currentEditor;
    private static SPlayer editorSPlayer;
    private static Map<String, StageEditor> editors;
    public static void init(JavaPlugin plugin) {
        StageEditorManager.plugin = plugin;
        editors = new HashMap<>();
        editors.put("tdm", new TDMStageEditor());
    }
    public static void begin(String stageName, String type, SPlayer editor) {
        if (isEditing()) {
            editor.message(ChatColor.RED + "Cannot begin editing while editing other stage");
            return;
        }
        StageEditor stageEditor = editors.get(type.toLowerCase());
        if (stageEditor == null) {
            editor.message(ChatColor.RED + "Type '" + type + "' is not found");
            return;
        }

        editor.getPlayer().setGameMode(GameMode.CREATIVE);
        Stage stage = StageManager.getStage(stageName, type);
        if (stage == null) {
            stage = StageManager.createStage(stageName, type);
            editor.message(ChatColor.YELLOW + "Stage '" + stage.getName() + "' was created");
        }else {
            editor.message(ChatColor.YELLOW + "Stage '" + stage.getName() + "' was selected");
        }
        editor.message(ChatColor.YELLOW + "Stage type is '" + type + "'");
        editorSPlayer = editor;
        currentEditor = stageEditor;
        currentEditor._begin(editorSPlayer, stage);
        Bukkit.getServer().getPluginManager().registerEvents(currentEditor, plugin);
    }
    public static void save() {
        if (!isEditing()) return;
        currentEditor._save();
        editorSPlayer.message(ChatColor.YELLOW + "Stage saved");
    }
    public static void end() {
        if (!isEditing()) return;
        save();
        HandlerList.unregisterAll(currentEditor);
        currentEditor._end();
        currentEditor = null;
    }
    public static boolean isEditing() {
        return currentEditor != null;
    }
}
