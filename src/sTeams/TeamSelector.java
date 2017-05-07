package sTeams;

import games.GameManager;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import sPlayers.SPlayer;
import sPlayers.SPlayerManager;
import utils.ColorMap;
import utils.CreatorUtils;

import java.util.*;

/**
 * Created by takumus on 2017/05/06.
 */
public class TeamSelector implements Listener{
    private static TeamSelector instance;
    private static Map<DyeColor, STeam> teamsMap;
    private static Inventory teamsInventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, "Select your team");
    public static void init(JavaPlugin plugin) {
        instance = new TeamSelector();
        Bukkit.getServer().getPluginManager().registerEvents(instance, plugin);
    }
    public static void setTeamsMap(List<STeam> teams) {
        teamsInventory.clear();
        teamsMap = new HashMap<>();
        int id = 0;
        for (STeam team : teams) {
            DyeColor dc = team.getDyeColor();
            team._selectorId = id++;
            NBTTagCompound tag = new NBTTagCompound();
            String teamName = ColorMap.getName(dc);
            tag.setString("team_color_name", teamName);
            ItemStack armour = CreatorUtils.createArmour(Material.LEATHER_CHESTPLATE, dc, tag);
            ItemMeta meta = armour.getItemMeta();
            meta.setDisplayName(team.getChatColor() + teamName);
            armour.setItemMeta(meta);
            teamsInventory.addItem(armour);
            teamsMap.put(dc, team);
        }
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

        STeam sTeam = teamsMap.get(teamColor);
        if (sTeam == null) return;

        // Bukkit.getServer().getPluginManager().callEvent(new STeamEvent(sTeam));
        GameManager.selectTeam(sp, sTeam);
    }
    public static void updateTeam() {
        teamsMap.entrySet().forEach((es) -> {
            STeam st = es.getValue();
            ItemStack item = teamsInventory.getItem(st._selectorId);

            ItemMeta meta = item.getItemMeta();
            List<String> memberNames = new ArrayList<>();
            st.getMembers().forEach((msp) -> memberNames.add(ChatColor.RESET + msp.getName()));
            meta.setLore(memberNames);
            item.setItemMeta(meta);
        });
    }
}
