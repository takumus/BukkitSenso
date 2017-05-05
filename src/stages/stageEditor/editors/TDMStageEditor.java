package stages.stageEditor.editors;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
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
    private List<Zombie> markerZombies;
    private void initWools(SPlayer sp) {
        sp.clearInventory();
        DyeColor[] colors = {DyeColor.RED, DyeColor.LIGHT_BLUE, DyeColor.PINK, DyeColor.YELLOW, DyeColor.LIME, DyeColor.BLUE, DyeColor.CYAN, DyeColor.ORANGE, DyeColor.GREEN};

        for (int i = 0; i < colors.length; i ++) {
            sp.getPlayer().getInventory().setItem(i, this.createArmour(Material.LEATHER_CHESTPLATE, colors[i]));
        }
    }
    private ItemStack createArmour(Material material, DyeColor color) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("team_color_name", ColorMap.getName(color));
        return CreatorUtils.createArmour(material, color, tag);
    }
    private Zombie createZombie(Location location, DyeColor color) {
        location.setPitch(0);
        Zombie z = CreatorUtils.createNoAIZombie(location);

        z.getEquipment().setChestplate(this.createArmour(Material.LEATHER_CHESTPLATE, color));
        z.getEquipment().setLeggings(this.createArmour(Material.LEATHER_LEGGINGS, color));
        z.getEquipment().setBoots(this.createArmour(Material.LEATHER_BOOTS, color));
        MetadataManager.setMetadata(z, "team_color_name", ColorMap.getName(color));
        return z;
    }
    private Zombie addSpawn(Location location, DyeColor color) {
        Zombie z = this.createZombie(location, color);
        this.markerZombies.add(z);
        return z;
    }
    private Map<String, List<Entity>> getSpawns() {
        Map<String, List<Entity>> spawns = new HashMap<>();
        this.markerZombies.forEach((zombie) -> {
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
    @EventHandler (priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getViewers().size() != 1) return;

        if (!SPlayerManager.getSPlayer(e.getViewers().get(0)).equals(this.getEditor())) return;

        NBTTagCompound tag = CraftItemStack.asNMSCopy(e.getCurrentItem()).getTag();
        if (tag == null) return;

        String colorString = tag.getString("team_color_name");
        if (colorString == null) return;

        e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onDropItem(PlayerDropItemEvent e) {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(e.getItemDrop().getItemStack()).getTag();
        if (tag == null) return;
        String colorString = tag.getString("team_color_name");
        if (colorString == null) return;

        e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        if(!this.markerZombies.contains(e.getEntity())) return;

        Zombie z = (Zombie) e.getEntity();
        e.setCancelled(true);

        if(!(e instanceof EntityDamageByEntityEvent)) return;
        if(!((EntityDamageByEntityEvent)e).getDamager().equals(this.getEditor().getPlayer())) return;

        this.markerZombies.remove(z);
        z.remove();

        String colorName = MetadataManager.getMetadata(z, "team_color_name");
        ChatColor color = ColorMap.getChatColor(colorName);
        this.getEditor().message(color + colorName + ChatColor.RESET + "'s spawn was removed");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onRightClick(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!e.getPlayer().equals(this.getEditor().getPlayer())) return;

        NBTTagCompound tag = CraftItemStack.asNMSCopy(this.getEditor().getPlayer().getInventory().getItemInMainHand()).getTag();
        if (tag == null) return;

        String colorString = tag.getString("team_color_name");
        if (colorString == null) return;

        for (Zombie z : this.markerZombies) {
            double d = z.getLocation().distance(this.getEditor().getPlayer().getLocation());
            if (d < 1) return;
        }

        Zombie z = this.addSpawn(this.getEditor().getPlayer().getLocation(), ColorMap.getDyeColor(colorString));

        this.getEditor().message(this.markerZombies.size() + " zombies");
    }

    @Override
    public void begin(SPlayer editor, Stage stage) {
        this.initWools(editor);
        this.markerZombies = new ArrayList<>();

        stage.getSpawns().forEach((spawn) -> {
            System.out.println(spawn);
            this.addSpawn(spawn.getLocation(), ColorMap.getDyeColor(spawn.getMeta("team")));
        });
    }

    @Override
    public void save() {
        Map<String, List<Entity>> spawns = this.getSpawns();
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
    public void end() {
        this.markerZombies.forEach((zombie) -> {
            zombie.remove();
        });
    }

    @Override
    protected void receiveValue(String value) {

    }
}

enum Phase {
    SELECT_STAGE,
}