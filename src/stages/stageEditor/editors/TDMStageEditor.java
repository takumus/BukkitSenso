package stages.stageEditor.editors;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import sPlayers.SPlayer;
import stages.Spawn;
import stages.Stage;
import stages.stageEditor.StageEditor;
import utils.ColorMap;
import utils.CreatorUtils;
import utils.MetadataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takumus on 2017/05/04.
 */
public class TDMStageEditor extends StageEditor {
    private void initWools(SPlayer sp) {
        sp.clearInventory();
        DyeColor[] colors = {
                DyeColor.RED,
                DyeColor.LIGHT_BLUE,
                DyeColor.PINK,
                DyeColor.YELLOW,
                DyeColor.LIME,
                DyeColor.BLUE,
                DyeColor.CYAN,
                DyeColor.ORANGE,
                DyeColor.GREEN
        };
        this.getEditor().getPlayer().getInventory().clear();
        for (int i = 0; i < colors.length; i ++) {
            this.setItemToSlot(this.createArmour(Material.LEATHER_CHESTPLATE, colors[i]), i);
        }
    }
    private ItemStack createArmour(Material material, DyeColor color) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("team_color_name", ColorMap.getName(color));
        return CreatorUtils.createArmour(material, color, tag);
    }
    private Map<String, List<Entity>> getSpawnsMap() {
        Map<String, List<Entity>> spawns = new HashMap<>();
        this.getSpawns().forEach((zombie) -> {
            String teamColor = MetadataManager.getMetadata(zombie, "team_color_name");
            List<Entity> teamSpawns = spawns.get(teamColor.toLowerCase());
            if (teamSpawns == null) {
                teamSpawns = new ArrayList<>();
                spawns.put(teamColor.toLowerCase(), teamSpawns);
            }
            teamSpawns.add(zombie);
        });
        return spawns;
    }
    private LivingEntity addSpawn(Location location, DyeColor color) {
        Map<String, String> meta = new HashMap<>();
        meta.put("team_color_name", ColorMap.getName(color));
        LivingEntity e = this.addSpawn(location, meta);
        e.getEquipment().setChestplate(this.createArmour(Material.LEATHER_CHESTPLATE, color));
        e.getEquipment().setLeggings(this.createArmour(Material.LEATHER_LEGGINGS, color));
        e.getEquipment().setBoots(this.createArmour(Material.LEATHER_BOOTS, color));
        MetadataManager.setMetadata(e, "team_color_name", ColorMap.getName(color));
        return e;
    }
    @Override
    protected boolean addingSpawn(Location location, NBTTagCompound tag) {
        this.addSpawn(location, ColorMap.getDyeColor(tag.getString("team_color_name")));
        return false;
    }

    @Override
    protected void removeSpawn(LivingEntity e) {
        String colorName = MetadataManager.getMetadata(e, "team_color_name");
        ChatColor color = ColorMap.getChatColor(colorName);
        this.getEditor().message(color + colorName + ChatColor.RESET + "'s spawn was removed");
    }

    @Override
    public void start(SPlayer editor, Stage stage) {
        this.initWools(editor);

        editor.message(ChatColor.YELLOW.toString() + stage.getSpawns().size() + ChatColor.RESET + " spawns loaded");
        stage.getSpawns().forEach((spawn) -> this.addSpawn(spawn.getLocation(), ColorMap.getDyeColor(spawn.getMeta("team"))));
    }

    @Override
    public void save() {
        Map<String, List<Entity>> spawns = this.getSpawnsMap();
        Stage stage = this.getStage();
        stage.clearSpawns();

        spawns.entrySet().forEach((es) -> {
            es.getValue().forEach((z) -> {
                Spawn s = new Spawn(z.getLocation());
                s.setMeta("team", es.getKey());
                stage.addSpawn(s);
            });
        });
    }

    @Override
    public void stop() {
        this.getSpawns().forEach((spawn) -> spawn.remove());
    }

    @Override
    protected void receiveValue(String value) {

    }
}