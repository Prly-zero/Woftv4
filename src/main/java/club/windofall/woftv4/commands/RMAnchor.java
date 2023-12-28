package club.windofall.woftv4.commands;

import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.utils.SqliteUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RMAnchor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if (strings.length!=0){
                String sql = String.format("SELECT COUNT(*) FROM ANCHOR WHERE NAME = '%s';",strings[0]);
                try {
                    Object[][] res = SqliteUtil.Query(sql);
                    if (res != null && (int) res[0][0] > 0) {
                        sql = String.format("DELETE FROM ANCHOR WHERE NAME = '%s';", strings[0]);
                        SqliteUtil.Update(sql);
                        SqliteUtil.UpdateAC();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(ChatColor.YELLOW + "已移除锚点" + ChatColor.RED + "[" + strings[0] + "]");
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>(Woftv4.getAnchorSet());
    }
}
