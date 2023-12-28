package club.windofall.woftv4.events;

import club.windofall.woftv4.Woftv4;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class BooksUse implements Listener {
    private static final NamespacedKey TYPE = new NamespacedKey(Woftv4.getPlugin(Woftv4.class),"book_type");
    @EventHandler
    public void onUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction().toString().contains("RIGHT") && player.getInventory().getItemInMainHand().getType() == Material.BOOK){
            ItemStack book = player.getInventory().getItemInMainHand();
            if (book.hasItemMeta() && Objects.requireNonNull(book.getItemMeta()).getPersistentDataContainer().has(TYPE, PersistentDataType.STRING)){
                ItemMeta meta = book.getItemMeta();
                String type = meta.getPersistentDataContainer().getOrDefault(TYPE,PersistentDataType.STRING,"none");
                if (type.equals("craft")){
                    player.openWorkbench(null,true);
                }
            }
        }
    }
}
