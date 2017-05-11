package sScoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by takumus on 2017/05/07.
 */
public class SSidebarScoreboard extends SScoreboard{
    private Scoreboard board;
    private Objective objective;
    public SSidebarScoreboard(String name) {
        this.board = this.getScoreboard();

        this.objective = this.board.registerNewObjective("sidebar", "dummy");
        this.objective.setDisplayName(name);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    public void setScore(String name, int score) {
        this.objective.getScore(name).setScore(score);
    }
    public void clear() {
        this.board.getEntries().forEach((score) -> this.board.resetScores(score));
    }
}