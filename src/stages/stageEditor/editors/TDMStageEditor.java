package stages.stageEditor.editors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sPlayer.SPlayer;
import stages.Stage;
import stages.stageEditor.StageEditor;

/**
 * Created by takumus on 2017/05/04.
 */
public class TDMStageEditor extends StageEditor {
    @Override
    public void begin(SPlayer editor, Stage stage) {

    }

    @Override
    public void end() {

    }

    @Override
    protected void receiveValue(String value) {

    }
}

enum Phase {
    SELECT_STAGE,
}