package club.windofall.woftv4.commands;

import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.utils.SqliteUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LiveMsg implements CommandExecutor, TabCompleter {
    private static final Logger logger = Woftv4.getlogger();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            try{
                UUID uuid = ((Player) commandSender).getUniqueId();
                if (strings.length == 0){
                    Object[][] resultSet = SqliteUtil.Query(String.format("SELECT LEVEL FROM CML WHERE UUID = '%s';", uuid));
                    int l = 0;
                    if (resultSet != null && resultSet.length > 0 && resultSet[0].length > 0) {
                        l = (int)resultSet[0][0];
                    }
                    String le;
                    switch (l){
                        case 1:
                            le = "Msg";
                            break;
                        case 0:
                        default:
                            le = "None";
                    }
                    commandSender.sendMessage(ChatColor.GREEN+"当前LiveMsg等级为"+ChatColor.YELLOW+le);
                }else{
                    int pn;
                    switch (strings[0]){
                        case "None":
                            pn = 0;
                            break;
                        case "Msg":
                            pn = 1;
                            break;
                        default:
                            return false;
                    }
                    SqliteUtil.Update(String.format("INSERT OR REPLACE INTO CML (UUID,LEVEL) VALUES ('%s',%d);", uuid, pn));
                    SqliteUtil.UpdateLC();
                    commandSender.sendMessage(ChatColor.GREEN+"已成功将LiveMsg等级设为"+ChatColor.YELLOW+strings[0]);
                }
                if (!Woftv4.getConfigE().isLiveChat()){
                    commandSender.sendMessage(ChatColor.RED+"注意:此项可能不起作用(服务器未启用LiveChat)");
                }
                return true;
            } catch (SQLException | ClassNotFoundException e) {
                logger.log(Level.WARNING,"["+this.getClass().getSimpleName()+"] "+e);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        list.add("None");
        list.add("Msg");
        return list;
    }
}
