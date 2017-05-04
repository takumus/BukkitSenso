package stages.stageEditor;

import org.bukkit.event.Listener;
import sPlayer.SPlayer;

/**
 * Created by takumus on 2017/05/04.
 */
abstract public class StageEditor implements Listener {
    private SPlayer editor;
    abstract public void begin(SPlayer editor);
    abstract public void end();
    void _begin(SPlayer editor) {
        this.editor = editor;
        this.begin(editor);
    }
    void _end() {
        this.editor = null;
        this.end();
    }
    public SPlayer getEditor() {
        return this.editor;
    }
}
