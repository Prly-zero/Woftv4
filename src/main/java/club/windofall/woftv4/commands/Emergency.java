package club.windofall.woftv4.commands;

import club.windofall.woftv4.Woftv4;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Emergency implements CommandExecutor, TabCompleter {
    private static final NamespacedKey TYPE = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "Target_Type");
    private static final NamespacedKey TARGET = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "Target");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 1) {
                Player player = (Player) commandSender;
                ItemStack totem = player.getInventory().getItemInMainHand();
                if (totem.getType().equals(Material.TOTEM_OF_UNDYING)) {
                    ItemMeta meta = totem.getItemMeta();
                    if (meta != null) {
                        String lore_s = strings[0];
                        if (strings[0].equals("!here")) {
                            meta.getPersistentDataContainer().set(TYPE, PersistentDataType.INTEGER, 1);
                            Location location = player.getLocation();
                            double x = location.getX(), y = location.getY(), z = location.getZ();
                            lore_s = String.format("%s %.2f %.2f %.2f", Objects.requireNonNull(location.getWorld()).getName(), x, y, z);
                            meta.getPersistentDataContainer().set(TARGET, PersistentDataType.STRING, lore_s);
                        } else if (Woftv4.getAnchorSet().contains(strings[0])) {
                            meta.getPersistentDataContainer().set(TYPE, PersistentDataType.INTEGER, 2);
                            meta.getPersistentDataContainer().set(TARGET, PersistentDataType.STRING, strings[0]);
                        } else {
                            commandSender.sendMessage(ChatColor.RED + "不支持的目的.");
                            return true;
                        }
                        meta.setDisplayName("不死图腾:紧急避险");
                        List<String> lore = new ArrayList<>();
                        lore.add(lore_s);
                        meta.setLore(lore);
                        totem.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(totem);
                        commandSender.sendMessage(ChatColor.GREEN + "紧急避险已启用.");
                        return true;
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "需要手持一个不死图腾以启用功能");
                    return true;
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "缺少目的.");
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>(Woftv4.getAnchorSet());
        list.add("!here");
        return list;
    }
}
