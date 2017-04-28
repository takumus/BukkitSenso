package sItem.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import sItem.SItem;
import sPlayer.SPlayer;

/**
 * Created by takumus on 2017/04/28.
 */
public class Sword extends SItem{
    public Sword() {
        super();
    }
    @EventHandler
    private void onClick(PlayerInteractEvent e) {
        this.holder.getPlayer().setVelocity(new Vector(0, 10, 0));
    }
}