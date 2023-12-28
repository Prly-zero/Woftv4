package club.windofall.woftv4.commands;

import club.windofall.woftv4.Woftv4;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CompassTracker implements CommandExecutor, TabCompleter {
    private static final NamespacedKey TARGET_PLAYER = new NamespacedKey(Woftv4.getPlugin(Woftv4.class),"trackerTarget");
    private static final NamespacedKey SHOW_DETAILS = new NamespacedKey(Woftv4.getPlugin(Woftv4.class),"details");
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            if (strings.length >= 1){
                Player target = Bukkit.getPlayer(strings[0]);
                if (target != null){
                    Player player = (Player) commandSender;
                    ItemStack compass = player.getInventory().getItemInMainHand();
                    if (compass.getType().equals(Material.COMPASS)){
                        CompassMeta meta = (CompassMeta) compass.getItemMeta();
                        if (meta != null) {
                            meta.setLodestoneTracked(false);
                            meta.setLodestone(target.getLocation());
                            meta.setDisplayName(strings[0]);
                            meta.getPersistentDataContainer().set(TARGET_PLAYER, PersistentDataType.STRING, strings[0]);
                            if (strings.length >=2) {
                                if (strings[1].equals("true")) {
                                    meta.getPersistentDataContainer().set(SHOW_DETAILS, PersistentDataType.BOOLEAN, true);
                                }else{
                                    meta.getPersistentDataContainer().set(SHOW_DETAILS, PersistentDataType.BOOLEAN, false);
                                }
                            }else{
                                meta.getPersistentDataContainer().set(SHOW_DETAILS, PersistentDataType.BOOLEAN, false);
                            }
                            compass.setItemMeta(meta);
                            player.getInventory().setItemInMainHand(compass);
                            commandSender.sendMessage(ChatColor.GREEN+"已绑定追踪器并指向 >"+strings[0]);
                            return true;
                        }
                    }else{
                        commandSender.sendMessage(ChatColor.RED + "需要手持一个指南针作为追踪器");
                        return true;
                    }
                }else{
                    commandSender.sendMessage(ChatColor.RED + "玩家"+strings[0]+"不存在或已离线");
                    return true;
                }
            }else {
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1){
            for(Player p: Bukkit.getOnlinePlayers()){
                list.add(p.getName());
            }
        } else if (strings.length == 2) {
            list.add("true");
            list.add("false");
        }
        return list;
    }
}
