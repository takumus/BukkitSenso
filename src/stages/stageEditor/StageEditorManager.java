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

/**
 * Created by takumus on 2017/05/04.
 */
public class StageEditorManager {
    private static JavaPlugin plugin;
    private static StageEditor currentEditor;
    private static SPlayer editorSPlayer;
    public static void init(JavaPlugin plugin) {
        StageEditorManager.plugin = plugin;
    }
    public static void begin(String stageName, String type, SPlayer editor) {
        if (currentEditor != null) {
            editor.message(ChatColor.RED + "Cannot begin editing while editing other stage");
            return;
        }
        editor.getPlayer().setGameMode(GameMode.CREATIVE);
        Stage stage = StageManager.getStage(stageName, type);
        if (stage == null) stage = StageManager.createStage(stageName, type);

        if (type.equalsIgnoreCase("tdm")) {
            editor.message("editing stage '" + stage.getName() + "' tdm mode");
            editorSPlayer = editor;
            currentEditor = new TDMStageEditor();
            currentEditor._begin(editorSPlayer, stage);
            Bukkit.getServer().getPluginManager().registerEvents(currentEditor, plugin);
        }
    }
    public static void save() {
        if (currentEditor == null) return;
        currentEditor._save();
    }
    public static void end() {
        if (currentEditor == null) return;
        save();
        HandlerList.unregisterAll(currentEditor);
        currentEditor._end();
        currentEditor = null;
    }
}
