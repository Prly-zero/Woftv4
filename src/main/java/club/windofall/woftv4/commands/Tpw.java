package club.windofall.woftv4.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class Tpw implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        World world = Bukkit.getWorld(strings[0]);
        double x = Double.parseDouble(strings[1]),y = Double.parseDouble(strings[2]),z = Double.parseDouble(strings[3]);
        Location location = new Location(world,x,y,z);
        player.teleport(location);
        player.sendMessage(ChatColor.YELLOW+"已前往标记点");
        return true;
    }
}
