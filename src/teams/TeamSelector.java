package teams;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import utils.CreatorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takumus on 2017/05/06.
 */
public class TeamSelector implements Listener{
    private static TeamSelector instance;
    private static List<STeam> teams;
    private static Inventory teamsInventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, "Select your team");
    public static void init(JavaPlugin plugin) {
        instance = new TeamSelector();
        Bukkit.getServer().getPluginManager().registerEvents(instance, plugin);
    }
    public static void setTeams(List<DyeColor> teamDyeColors) {
        teamsInventory.clear();
        teams = new ArrayList<>();
        teamDyeColors.forEach((dc) -> {
            STeam team = new STeam(dc);
            teamsInventory.addItem(CreatorUtils.createArmour(Material.LEATHER_CHESTPLATE, dc));
            teams.add(team);
        });
    }
    public static void showTeamSelectorToAllPlayers() {
        SPlayerManager.getAllSPlayer().forEach((sp) -> {
            showTeamSelector(sp);
        });
    }
    public static void showTeamSelector(SPlayer sp) {
        sp.clearInventory();
        sp.getPlayer().openInventory(teamsInventory);

    }

    @EventHandler
    public void onSelectItem(InventoryClickEvent e) {
        if (!e.getInventory().equals(teamsInventory)) return;
        e.setCancelled(true);

        SPlayer sp = SPlayerManager.getSPlayer(e.getWhoClicked());
        if (sp == null) return;

        sp.getPlayer().closeInventory();
    }
}
