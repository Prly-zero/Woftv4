package club.windofall.woftv4.commands;


import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.utils.SqliteUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Anchor implements CommandExecutor, TabCompleter {
    private static final Logger logger = Woftv4.getlogger();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(strings.length!=0){
                String sql = String.format("SELECT COUNT(*) FROM ANCHOR WHERE NAME = '%s';",strings[0]);
                try {
                    Object[][] res = SqliteUtil.Query(sql);
                    if (res != null && (int)res[0][0] > 0) {
                        sql = String.format("SELECT INFO FROM ANCHOR WHERE NAME = '%s';", strings[0]);
                        res = SqliteUtil.Query(sql);
                        if (res != null) {
                            String[] location = ((String) res[0][0]).split(" ");
                            player.teleport(new Location(Bukkit.getWorld(location[0]),Double.parseDouble(location[1]),Double.parseDouble(location[2]),Double.parseDouble(location[3])));
                            player.sendMessage(ChatColor.YELLOW+"已传送至锚点.");
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    logger.log(Level.WARNING,"["+this.getClass().getSimpleName()+"] "+e);
                }
            }else{
                player.sendMessage(ChatColor.RED+"请输入锚点名称");
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>(Woftv4.getAnchorSet());
    }
}