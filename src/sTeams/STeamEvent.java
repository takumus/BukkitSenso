package sTeams;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by takumus on 2017/05/06.
 */
public class STeamEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private STeam sTeam;
    public STeamEvent(STeam sTeam) {
        this.sTeam = sTeam;
    }
    public STeam getSTeam() {
        return this.sTeam;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
