package sItem;

import org.bukkit.event.Listener;

/**
 * Created by takumus on 2017/05/04.
 */
abstract public class SItemController implements Listener{
    abstract public void onTick();
    void _onTick() {
        this.onTick();
    }
}
