package stages.stageEditor;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import sPlayer.SPlayer;
import sPlayer.SPlayerManager;
import stages.Stage;
import stages.StageManager;
import utils.ColorMap;
import utils.CreatorUtils;
import utils.MetadataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by takumus on 2017/05/04.
 */
abstract public class StageEditor implements Listener {
    private SPlayer editor;
    private Stage stage;
    private List<LivingEntity> spawns;

    abstract public void begin(SPlayer editor, Stage stage);
    abstract public void end();
    abstract public void save();
    abstract protected void receiveValue(String value);
    abstract protected boolean addingSpawn(Location location, NBTTagCompound tag);
    public SPlayer getEditor() {
        return this.editor;
    }
    public Stage getStage() {
        return this.stage;
    }
    public List<LivingEntity> getSpawns() {
        return this.spawns;
    }

    void _begin(SPlayer editor, Stage stage) {
        this.spawns = new ArrayList<>();
        this.editor = editor;
        this.stage = stage;
        this.begin(editor, stage);
    }
    void _end() {
        this.end();
        this.editor.getPlayer().getInventory().clear();
        this.editor = null;
        this.stage = null;
    }

    void _save() {
        this.save();
        StageManager.saveConfig();
    }
    abstract protected void removeSpawn(LivingEntity e);
    protected LivingEntity addSpawn(Location location, Map<String, String> meta) {
        LivingEntity e = CreatorUtils.createNoAIZombie(location);
        meta.entrySet().forEach((es) -> {
            MetadataManager.setMetadata(e, es.getKey(), es.getValue());
        });
        this.spawns.add(e);
        return e;
    }
    protected void setItemToSlot(ItemStack item, int id) {
        Inventory inv = this.getEditor().getPlayer().getInventory();
        net.minecraft.server.v1_11_R1.ItemStack s = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = s.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setBoolean("use_for_stage_editor", true);
        s.setTag(tag);
        inv.setItem(id, CraftItemStack.asBukkitCopy(s));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (!e.getPlayer().equals(this.getEditor().getPlayer())) return;

        this.receiveValue(e.getMessage());
        e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        if(!this.spawns.contains(e.getEntity())) return;

        Zombie z = (Zombie) e.getEntity();
        e.setCancelled(true);

        if(!(e instanceof EntityDamageByEntityEvent)) return;
        if(!((EntityDamageByEntityEvent)e).getDamager().equals(this.getEditor().getPlayer())) return;

        this.spawns.remove(z);
        this.removeSpawn(z);
        z.remove();
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getViewers().size() != 1) return;

        if (!SPlayerManager.getSPlayer(e.getViewers().get(0)).equals(this.getEditor())) return;

        e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onDropItem(PlayerDropItemEvent e) {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(e.getItemDrop().getItemStack()).getTag();
        if (tag == null) return;

        if (!tag.getBoolean("use_for_stage_editor")) return;

        e.setCancelled(true);

        if (this.getSpawns().size() > 0 && !this.getSpawns().get(0).getWorld().equals(this.getEditor().getPlayer().getWorld())) {
            this.getEditor().message(ChatColor.YELLOW + "Cannot add spawn to another world");
            this.getEditor().message(ChatColor.YELLOW + "This stages world is " + this.getSpawns().get(0).getWorld().getName());
            return;
        }
        Location loc = this.getEditor().getPlayer().getLocation();
        loc.setX(Math.floor(loc.getX()) + 0.5);
        loc.setY(Math.floor(loc.getY()));
        loc.setZ(Math.floor(loc.getZ()) + 0.5);

        for (LivingEntity z : this.getSpawns()) {
            double d = z.getLocation().distance(loc);
            if (d < 0.5) return;
        }
        this.addingSpawn(loc, tag);
    }
}
