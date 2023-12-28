package club.windofall.woftv4.commands;

import club.windofall.woftv4.Woftv4;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

public class ApoBow implements CommandExecutor, TabCompleter {
    private static final NamespacedKey TYPE = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "TYPE");
    private static final NamespacedKey ARGS = new NamespacedKey(Woftv4.getPlugin(Woftv4.class), "ARGS");
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (strings.length >= 1){
                if (player.getInventory().getItemInMainHand().getType().equals(Material.BOW)){
                    ItemStack bow = player.getInventory().getItemInMainHand();
                    ItemMeta meta = bow.getItemMeta();
                    if (meta != null) {
                        if (strings[0].equals("explode")){
                            meta.getPersistentDataContainer().set(TYPE, PersistentDataType.INTEGER,1);
                            meta.setDisplayName(ChatColor.GOLD+"爆炸弓");
                            int max = 100, step = 1;
                            float r = 4F;
                            boolean h = false, c = true;
                            List<String> list;
                            if (meta.getLore()!=null){
                                list = meta.getLore();
                                list.removeIf(inl -> inl.startsWith("最大爆炸距离:") || inl.startsWith("爆炸间隔:") || inl.startsWith("爆炸强度:") || inl.startsWith("生成热量:") || inl.startsWith("地质更改:"));
                            }else{
                                list = new ArrayList<>();
                            }
                            try {
                                if (strings.length >= 2) {
                                    max = Integer.parseInt(strings[1]);
                                    max = Math.min(max,200);
                                }
                                if (strings.length >= 3) {
                                    step = Integer.parseInt(strings[2]);
                                    if (step == 0){step = 1;}
                                }
                                if (strings.length >= 4) {
                                    r = Math.min(Float.parseFloat(strings[3]), 50F);
                                }
                                if (strings.length >= 5) {
                                    h = Boolean.parseBoolean(strings[4]);
                                }
                                if (strings.length >= 6) {
                                    c = Boolean.parseBoolean(strings[5]);
                                }
                            }catch (NumberFormatException e){
                                player.sendMessage(ChatColor.RED +"参数格式错误");
                                return false;
                            }
                            meta.getPersistentDataContainer().set(ARGS, PersistentDataType.STRING,String.format("%d %d %f %b %b",max,step,r,h,c));
                            list.add("最大爆炸距离:"+max);
                            list.add("爆炸间隔:"+step);
                            list.add("爆炸强度:"+r);
                            list.add("生成热量:"+h);
                            list.add("地质更改:"+c);
                            meta.setLore(list);
                        }
                    }
                    bow.setItemMeta(meta);
                    player.getInventory().setItemInMainHand(bow);
                    player.sendMessage(ChatColor.GOLD+"successfully.");
                    return true;
                }else {
                    player.sendMessage(ChatColor.RED+"需要手持一把弓");
                    return true;
                }
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1){
            list.add("explode");
        }else if(strings.length == 5 || strings.length == 6){
            list.add("true");
            list.add("false");
        }
        return list;
    }
}
