package club.windofall.woftv4.events;

import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.utils.EnvCK;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDeath implements Listener {
    private static final Logger logger = Woftv4.getlogger();
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player p = event.getEntity();
        Location location = p.getLocation();
        double x= location.getX(),y= location.getY(),z= location.getZ();
        String world = Objects.requireNonNull(location.getWorld()).getName();
        File json = new File("plugins/WofT/WorldN.json");
        HashMap<String,String> worldL =new HashMap<>();
        if (json.exists()){
            try {
                JsonReader jr = new JsonReader(new FileReader(json));
                worldL = new Gson().fromJson(jr, worldL.getClass());
                if (worldL==null){
                    worldL = new HashMap<>();
                    logger.log(Level.WARNING,  "配置文件 WofT/WorldN.json内容为空,请及时处理!");
                }
            } catch (FileNotFoundException e) {
                logger.log(Level.WARNING,"["+this.getClass().getSimpleName()+"] "+e);
            }
        }else{
            logger.log(Level.WARNING,"配置文件 WofT/WorldN.json不存在.(尝试自动修复，若多次出现该提示，请手动修复)");
            EnvCK.CKBaseDIR();
            EnvCK.CKWorldN();
        }
        world=worldL.getOrDefault(world,world);
        TextComponent text0 = new TextComponent(ChatColor.RED+"["+world+"]");
        TextComponent text1 = new TextComponent(ChatColor.YELLOW+" <"+"x:"+String.format("%.2f",x)+","+"y:"+String.format("%.2f",y)+","+"z:"+String.format("%.2f",z)+">");
        TextComponent text2 = new TextComponent("你已死亡,尸体在");
        text0.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpw "+location.getWorld().getName()+" "+String.format("%.2f",x)+" "+String.format("%.2f",y)+" "+String.format("%.2f",z)));
        text0.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(ChatColor.GREEN+"点击传送至死亡点")));
        text0.addExtra(text1);
        Player.Spigot spigot = p.spigot();
        spigot.sendMessage(text2,text0);

    }
}
