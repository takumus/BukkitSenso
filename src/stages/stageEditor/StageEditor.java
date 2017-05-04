package stages.stageEditor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sPlayer.SPlayer;
import stages.Stage;
import stages.StageManager;

/**
 * Created by takumus on 2017/05/04.
 */
abstract public class StageEditor implements Listener {
    private SPlayer editor;
    abstract public void begin(SPlayer editor, Stage stage);
    abstract public void end();
    abstract public void save();
    void _begin(SPlayer editor, Stage stage) {
        this.editor = editor;
        this.begin(editor, stage);
    }
    void _end() {
        this.end();
        this.editor = null;
    }

    void _save() {
        this.save();
        StageManager.saveConfig();
    }
    public SPlayer getEditor() {
        return this.editor;
    }
    abstract protected void receiveValue(String value);
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (!e.getPlayer().equals(this.getEditor().getPlayer())) return;
        this.receiveValue(e.getMessage());
        e.setCancelled(true);
    }
}
