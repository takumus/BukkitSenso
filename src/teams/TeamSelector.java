package teams;

import games.GameManager;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import utils.ColorMap;
import utils.CreatorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takumus on 2017/05/06.
 */
public class TeamSelector implements Listener{
    private static TeamSelector instance;
    private static Map<DyeColor, STeam> teams;
    private static Inventory teamsInventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, "Select your team");
    public static void init(JavaPlugin plugin) {
        instance = new TeamSelector();
        Bukkit.getServer().getPluginManager().registerEvents(instance, plugin);
    }
    public static void setTeams(List<DyeColor> teamDyeColors) {
        teamsInventory.clear();
        teams = new HashMap<>();
        teamDyeColors.forEach((dc) -> {
            STeam team = new STeam(dc);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("team_color_name", ColorMap.getName(dc));
            teamsInventory.addItem(CreatorUtils.createArmour(Material.LEATHER_CHESTPLATE, dc, tag));
            teams.put(dc, team);
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

        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(e.getCurrentItem());
        if (s.getTag() == null) return;

        String teamColorName = s.getTag().getString("team_color_name");
        if (teamColorName == null) return;

        DyeColor teamColor = ColorMap.getDyeColor(teamColorName);
        if (teamColor == null) return;

        STeam sTeam = teams.get(teamColor);
        if (sTeam == null) return;

        Bukkit.getServer().getPluginManager().callEvent(new STeamEvent(sTeam));
        GameManager.selectTeam(sp, sTeam);
    }
}
