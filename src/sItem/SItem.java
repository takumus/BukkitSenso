package sItem;

import org.bukkit.event.Listener;
import sPlayer.SPlayer;

/**
 * Created by takumus on 2017/04/28.
 */
public class SItem implements Listener{
    protected SPlayer holder;
    protected int index;
    public SItem() {

    }
    public void init(SPlayer holder, int index) {
        this.holder = holder;
        this.index = index;
        SItemManager.addSItem(this);
    }
    public void destroy() {
        SItemManager.removeItem(this);
    }
}
