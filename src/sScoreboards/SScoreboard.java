package sScoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by takumus on 2017/05/11.
 */
public class SScoreboard {
    private static Scoreboard scoreboard;
    public static void init() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }
    public static Scoreboard getScoreboard() {
        return scoreboard;
    }
}
