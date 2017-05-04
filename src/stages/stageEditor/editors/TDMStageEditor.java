package stages.stageEditor.editors;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import stages.Stage;
import stages.stageEditor.StageEditor;
import utils.ColorMap;

/**
 * Created by takumus on 2017/05/04.
 */
public class TDMStageEditor extends StageEditor {
    private void initWools(SPlayer sp) {
        sp.clearInventory();
        DyeColor[] colors = {DyeColor.RED, DyeColor.LIGHT_BLUE, DyeColor.PINK, DyeColor.YELLOW, DyeColor.LIME, DyeColor.BLUE, DyeColor.CYAN, DyeColor.ORANGE, DyeColor.GREEN};

        for (int i = 0; i < colors.length; i ++) {
            sp.getPlayer().getInventory().setItem(i + 9, this.createWool(colors[i]));
        }
    }
    private ItemStack createWool(DyeColor color) {
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(chest);
        s.setTag(new NBTTagCompound());
        s.getTag().setString("team_color_name", ColorMap.getName(color));
        chest = CraftItemStack.asBukkitCopy(s);
        LeatherArmorMeta meta = (LeatherArmorMeta)chest.getItemMeta();
        meta.setColor(color.getColor());
        chest.setItemMeta(meta);
        return chest;
    }
    @EventHandler
    public void selectTeam(InventoryClickEvent e) {
        if (e.getViewers().size() != 1) return;

        if (!SPlayerManager.getSPlayer(e.getViewers().get(0)).equals(this.getEditor())) return;

        NBTTagCompound tag = CraftItemStack.asNMSCopy(e.getCurrentItem()).getTag();

        if (tag == null) return;

        String colorString = tag.getString("team_color_name");

        if (colorString == null) return;

        DyeColor color = ColorMap.getDyeColor(colorString);
        ChatColor chatColor = ColorMap.getChatColor(color);

        this.getEditor().message(chatColor + colorString + ChatColor.RESET + " selected");

        this.getEditor().getPlayer().getInventory().setItemInOffHand(this.createWool(color));

        this.getEditor().getPlayer().closeInventory();

        e.setCancelled(true);
    }
    @Override
    public void begin(SPlayer editor, Stage stage) {
        this.initWools(editor);
    }

    @Override
    public void end() {

    }

    @Override
    protected void receiveValue(String value) {

    }
}

enum Phase {
    SELECT_STAGE,
}