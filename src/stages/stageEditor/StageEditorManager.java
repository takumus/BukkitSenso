package stages.stageEditor;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayer.SPlayer;
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
    public static void beginEdit(String type, SPlayer editor) {
        if (type.equalsIgnoreCase("tdm")) {
            editor.message("editing 'tdm' stage");
            editorSPlayer = editor;
            currentEditor = new TDMStageEditor();
            currentEditor._begin(editorSPlayer);
            Bukkit.getServer().getPluginManager().registerEvents(currentEditor, plugin);
        }
    }
    public static void endEdit() {
        HandlerList.unregisterAll(currentEditor);
        currentEditor._end();
        currentEditor = null;
    }
}
