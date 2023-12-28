package club.windofall.woftv4.commands;

import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.utils.EnvCK;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Imhere implements CommandExecutor {
    private static final Logger logger = Woftv4.getlogger();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            Location location = player.getLocation();
            double x= location.getX(),y= location.getY(),z= location.getZ();
            String world = Objects.requireNonNull(location.getWorld()).getName();
            File json = new File("plugins/WofT/WorldN.json");
            if (json.exists()){
                try {
                    JsonReader jr = new JsonReader(new FileReader(json));
                    HashMap<String,String> worldL =new HashMap<>();
                    worldL = new Gson().fromJson(jr, worldL.getClass());
                    if (worldL==null){
                        worldL = new HashMap<>();
                        logger.log(Level.WARNING,  "配置文件 WofT/WorldN.json内容为空,请及时处理!");
                    }
                    world=worldL.getOrDefault(world,world);
                    TextComponent text0 = new TextComponent("["+world+"]");
                    TextComponent text1 = new TextComponent("我在");
                    TextComponent text2 = new TextComponent(ChatColor.WHITE+"<x:"+ChatColor.YELLOW+String.format("%.2f",x)+ChatColor.WHITE+",y:"+ChatColor.YELLOW+String.format("%.2f",y)+ChatColor.WHITE+",z:"+ChatColor.YELLOW+String.format("%.2f",z)+ChatColor.WHITE+">");
                    TextComponent text3 = new TextComponent("<"+player.getName()+"> ");
                    text0.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                    text0.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpw "+location.getWorld().getName()+" "+x+" "+y+" "+z));
                    text0.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("点击传送至这个点")));
                    text2.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, location.getWorld().getName()+" "+String.format("%.2f",x)+" "+String.format("%.2f",y)+" "+String.format("%.2f",z)));
                    text2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("点击复制坐标\n格式:world x y z")));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20*60,1));
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.spigot().sendMessage(text3,text1,text0,text2);
                    }
                    return true;
                } catch (FileNotFoundException e) {
                    logger.log(Level.WARNING,"["+this.getClass().getSimpleName()+"] "+e);
                }
            }else {
                logger.log(Level.WARNING,"配置文件 WofT/WorldN.json不存在.(尝试自动修复，若多次出现该提示，请手动修复)");
                EnvCK.CKBaseDIR();
                EnvCK.CKWorldN();
            }
        }
        return false;
    }
}
