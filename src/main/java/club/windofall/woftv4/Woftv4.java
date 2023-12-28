package club.windofall.woftv4;

import club.windofall.woftv4.commands.*;
import club.windofall.woftv4.entity.ConfigE;
import club.windofall.woftv4.events.*;
import club.windofall.woftv4.recipes.CraftingRecipes;
import club.windofall.woftv4.utils.ConfigUtil;
import club.windofall.woftv4.utils.EnvCK;
import club.windofall.woftv4.utils.LiveChat;
import club.windofall.woftv4.utils.SqliteUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Woftv4 extends JavaPlugin {
    private static HashMap<UUID,Integer> liveChannel = new HashMap<>();
    private static HashSet<String> anchorSet = new HashSet<>();
    private static final Logger logger = Bukkit.getServer().getLogger();
    private static final HashSet<Listener> eventSet = new HashSet<>();
    private static final HashMap<String, CommandExecutor> cmdMap = new HashMap<>();
    private static ConfigE configE = new ConfigE();
    @Override
    public void onEnable() {
        EnvCK.ckALL();
        try {
            SqliteUtil.Init();
            SqliteUtil.UpdateLC();
            SqliteUtil.UpdateAC();
            ConfigUtil.updateYaml();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        if (configE.isLiveChat()){getLogger().log(Level.INFO, "LiveChat功能已启用");}
        eventSet.add(new PlayerJoin());
        eventSet.add(new PlayerDeath());
        eventSet.add(new PlayerTrackerUpdate());
        eventSet.add(new PlayerEmergency());
        eventSet.add(new ApoBowUse());
        eventSet.add(new BooksUse());
        cmdMap.put("cml",new LiveMsg());
        cmdMap.put("anchor", new Anchor());
        cmdMap.put("setanchor", new SetAnchor());
        cmdMap.put("rmanchor", new RMAnchor());
        cmdMap.put("imhere", new Imhere());
        cmdMap.put("tpw", new Tpw());
        cmdMap.put("ping", new Ping());
        cmdMap.put("track", new CompassTracker());
        cmdMap.put("egy", new Emergency());
        cmdMap.put("apobow",new ApoBow());
        cmdMap.put("test",new TestCommand());
        for (Listener listener: eventSet){
            getServer().getPluginManager().registerEvents(listener,this);
        }
        for (Map.Entry<String,CommandExecutor> cmdItem:cmdMap.entrySet()){
            Objects.requireNonNull(this.getCommand(cmdItem.getKey())).setExecutor(cmdItem.getValue());
        }
        CraftingRecipes.RegisterAll(this);
    }

    @Override
    public void onDisable() {
        LiveChat.spTimer();
        CraftingRecipes.RemoveAll(this);
    }
    public static Logger getlogger(){
        return logger;
    }
    public static HashMap<UUID, Integer> getLiveChannel() {
        return liveChannel;
    }
    public static void setLiveChannel(HashMap<UUID, Integer> liveChannel) {
        Woftv4.liveChannel = liveChannel;
    }
    public static HashSet<String> getAnchorSet() {
        return anchorSet;
    }
    public static void setAnchorSet(HashSet<String> anchorSet) {
        Woftv4.anchorSet = anchorSet;
    }
    public static ConfigE getConfigE() {
        return configE;
    }
    public static void setConfigE(ConfigE configE) throws IOException {
        ConfigE ori_cfg = Woftv4.configE;
        if (configE.isLiveChat()){
            if (!configE.getBiliLive().equals(ori_cfg.getBiliLive())){
                LiveChat.reTimer();
                LiveChat.launch();
            }
        } else{
            LiveChat.spTimer();
        }
        Woftv4.configE = configE;
        ConfigUtil.wYaml(configE);
    }
}
