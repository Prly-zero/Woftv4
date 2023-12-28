package club.windofall.woftv4.events;

import club.windofall.woftv4.Woftv4;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class PlayerEmergency implements Listener {
    private static final NamespacedKey TYPE = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "Target_Type");
    private static final NamespacedKey TARGET = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "Target");
    @EventHandler
    public void onResurrect(EntityResurrectEvent event){
        if (event.getHand()!=null && event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            ItemStack totem = player.getInventory().getItem(event.getHand());
            if (totem != null && totem.hasItemMeta() && Objects.requireNonNull(totem.getItemMeta()).getPersistentDataContainer().has(TYPE, PersistentDataType.INTEGER) && Objects.requireNonNull(totem.getItemMeta()).getPersistentDataContainer().has(TARGET, PersistentDataType.STRING)) {
                int type = Objects.requireNonNull(totem.getItemMeta().getPersistentDataContainer().get(TYPE,PersistentDataType.INTEGER));
                if (type == 1){
                    String[] args = Objects.requireNonNull(totem.getItemMeta().getPersistentDataContainer().get(TARGET, PersistentDataType.STRING)).split(" ");
                    double x = Double.parseDouble(args[1]),y = Double.parseDouble(args[2]),z = Double.parseDouble(args[3]);
                    Location location = new Location(Bukkit.getWorld(args[0].trim()),x,y,z);
                    Bukkit.getScheduler().runTask(Woftv4.getPlugin(Woftv4.class),()-> player.teleport(location));
                } else if (type == 2) {
                    Bukkit.getScheduler().runTask(Woftv4.getPlugin(Woftv4.class),()-> player.performCommand("anchor "+Objects.requireNonNull(totem.getItemMeta().getPersistentDataContainer().get(TARGET, PersistentDataType.STRING))));
                }
                Bukkit.getScheduler().runTask(Woftv4.getPlugin(Woftv4.class),()-> player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 255)));
            }
        }
    }
}
