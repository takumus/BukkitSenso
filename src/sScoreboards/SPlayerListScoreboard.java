package sScoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by takumus on 2017/05/07.
 */
public class SPlayerListScoreboard {
    private Scoreboard board;
    private Objective objective;
    public SPlayerListScoreboard(String name) {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();

        this.objective = this.board.registerNewObjective("hello", "dummy");
        this.objective.setDisplayName(name);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    public void setScore(String name, int score) {
        this.objective.getScore(name).setScore(score);
    }
    public void clear() {
        this.board.getEntries().forEach((score) -> this.board.resetScores(score));
    }
    public Scoreboard getScoreboard() {
        return this.board;
    }
}