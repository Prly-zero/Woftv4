package club.windofall.woftv4.commands;

import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.utils.SqliteUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetAnchor implements CommandExecutor {
    private static final Logger logger = Woftv4.getlogger();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (strings.length!=0) {
                if (!strings[0].startsWith("!")) {
                    Location location = player.getLocation();
                    String world = Objects.requireNonNull(location.getWorld()).getName();
                    double x = location.getX(), y = location.getY(), z = location.getZ();
                    String info = world + " " + x + " " + y + " " + z;
                    try {
                        String sql = String.format("INSERT OR REPLACE INTO ANCHOR (NAME,INFO) VALUES ('%s','%s');", strings[0].trim(), info);
                        SqliteUtil.Update(sql);
                        SqliteUtil.UpdateAC();
                        TextComponent text0 = new TextComponent(ChatColor.YELLOW + "[" + strings[0].trim() + "]");
                        TextComponent text1 = new TextComponent("锚点");
                        TextComponent text2 = new TextComponent(ChatColor.WHITE + "已成功创建");
                        text0.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "点击传送至该锚点")));
                        text0.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/anchor " + strings[0].trim()));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            Player.Spigot spigot = p.spigot();
                            spigot.sendMessage(text1, text0, text2);
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        logger.log(Level.WARNING, "[" + this.getClass().getSimpleName() + "] " + e);
                    }
                }else{
                    player.sendMessage(ChatColor.RED+"请重新输入锚点名称，前缀'!'是保留的.");
                }
            }else {
                player.sendMessage(ChatColor.RED+"请输入锚点名称");
            }
        }
        return true;
    }
}