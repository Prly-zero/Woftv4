package club.windofall.woftv4.events;

import club.windofall.woftv4.Woftv4;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ApoBowUse implements Listener {
    private static final NamespacedKey TYPE = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "TYPE");
    private static final NamespacedKey ARGS = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "ARGS");
    @EventHandler
    public void onBowUse(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player){
            ItemStack bow = event.getBow();
            Entity arrow = event.getProjectile();
            if (bow != null) {
                ItemMeta meta = bow.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer().has(TYPE, PersistentDataType.INTEGER)){
                    Player player = (Player)event.getEntity();
                    int type = meta.getPersistentDataContainer().getOrDefault(TYPE,PersistentDataType.INTEGER,1);
                    if (type == 1){
                        Location startLocation = arrow.getLocation();
                        Vector direction = arrow.getVelocity();
                        Location currentLocation = startLocation.clone();
                        String[] args = meta.getPersistentDataContainer().getOrDefault(ARGS,PersistentDataType.STRING,"100 5 4F false true").split(" ");
                        int maxDistance = Math.min(Integer.parseInt(args[0]),200);
                        int step = Integer.parseInt(args[1]);
                        if (step == 0){step = 1;}
                        arrow.remove();
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 255));
                        while (maxDistance > 0){
                            currentLocation.add(direction.normalize().multiply(step));
                            Objects.requireNonNull(currentLocation.getWorld()).createExplosion(currentLocation,Math.min(Float.parseFloat(args[2]),50f),Boolean.parseBoolean(args[3]),Boolean.parseBoolean(args[4]));
                            maxDistance -= step;
                        }
                    }
                }
            }
        }
    }
}
