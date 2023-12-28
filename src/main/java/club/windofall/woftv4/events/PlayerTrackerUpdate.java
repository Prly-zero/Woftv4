package club.windofall.woftv4.events;

import club.windofall.woftv4.Woftv4;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PlayerTrackerUpdate implements Listener {
    private static final NamespacedKey TARGET_PLAYER = new NamespacedKey(Woftv4.getPlugin(Woftv4.class),"trackerTarget");
    private static final NamespacedKey SHOW_DETAILS = new NamespacedKey(Woftv4.getPlugin(Woftv4.class),"details");
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction().toString().contains("RIGHT") && player.getInventory().getItemInMainHand().getType() == Material.COMPASS){
            ItemStack compass = player.getInventory().getItemInMainHand();
            if (compass.hasItemMeta() && Objects.requireNonNull(compass.getItemMeta()).getPersistentDataContainer().has(TARGET_PLAYER, PersistentDataType.STRING)){
                String targetPlayerName = compass.getItemMeta().getPersistentDataContainer().get(TARGET_PLAYER,PersistentDataType.STRING);
                if (targetPlayerName != null) {
                    Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (targetPlayer!=null){
                        Location location = targetPlayer.getLocation();
                        Location plocation = player.getLocation();
                        CompassMeta meta = (CompassMeta) compass.getItemMeta();
                        if (meta != null) {
                            meta.setLodestone(location);
                            compass.setItemMeta(meta);
                        }
                        if (Objects.requireNonNull(compass.getItemMeta()).getPersistentDataContainer().has(SHOW_DETAILS, PersistentDataType.BOOLEAN) && Boolean.TRUE.equals(compass.getItemMeta().getPersistentDataContainer().get(SHOW_DETAILS, PersistentDataType.BOOLEAN)))
                        {
                            double x = location.getX(), y = location.getY(), z = location.getZ(), d = Math.sqrt(Math.pow((plocation.getX() - x),2)+Math.pow((plocation.getY() - y),2)+Math.pow((plocation.getZ() - z),2));
                            if (Objects.equals(plocation.getWorld(), location.getWorld())){
                                player.sendMessage(ChatColor.YELLOW+"#"+targetPlayerName + ChatColor.GREEN + String.format("> x %.2f y %.2f z %.2f d %.2f",x,y,z,d));
                            }else{
                                player.sendMessage(ChatColor.RED +"目标:" + targetPlayerName + "不在当前维度");
                            }
                        }
                    }else{
                        player.sendMessage(ChatColor.RED +"目标:" + targetPlayerName + "不存在或已离线");
                    }
                }
            }
        }
    }
}
