package club.windofall.woftv4.utils;

import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.entity.ConfigE;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LiveChat {
    private static Timer timer;
    private static boolean flag;
    private static Date date = new Date();
    private static Date tmp_max_date = date;
    private static final Logger logger = Woftv4.getlogger();
    private static final HashMap<String,Object> hashMap = new HashMap<>();
    private static final SimpleDateFormat sdf =new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    private static final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                flag = true;
                hashMap.put("Host", "api.live.bilibili.com");
                hashMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
                hashMap.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
                hashMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                hashMap.put("TE", "Trailers");
                JSONObject jsonObject = (JSONObject) JSON.parse(HttpRequest.doGet("https://api.live.bilibili.com/xlive/web-room/v1/dM/gethistory?roomid=" + Woftv4.getConfigE().getBiliLive().getRoom_id(), hashMap));
                if(jsonObject == null){
                    logger.log(Level.WARNING,"[LiveChat] "+"网络异常,正在自动关闭LiveChat服务");
                    ConfigE configE = Woftv4.getConfigE();
                    configE.setLiveChat(false);
                    Woftv4.setConfigE(configE);
                } else if (jsonObject.getInteger("code") == -400){
                    logger.log(Level.WARNING,"[LiveChat] "+"直播房间号异常,正在自动关闭LiveChat服务");
                    ConfigE configE = Woftv4.getConfigE();
                    configE.setLiveChat(false);
                    Woftv4.setConfigE(configE);
                }else {
                    jsonObject = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("room");
                    for (Object jo : jsonArray) {
                        JSONObject js = (JSONObject) jo;
                        Date tmp_date = sdf.parse(js.getString("timeline"));
                        if (date.before(tmp_date)) {
                            tmp_max_date = tmp_max_date.before(tmp_date) ? tmp_date : tmp_max_date;
                            HashSet<Player> set = new HashSet<>();
                            for (Map.Entry<UUID, Integer> item : Woftv4.getLiveChannel().entrySet()) {
                                if (item.getValue() > 0) {
                                    set.add(Bukkit.getPlayer(item.getKey()));
                                }
                            }
                            set.retainAll(Bukkit.getOnlinePlayers());
                            for (Player p : set) {
                                p.sendMessage("[LiveChat] <" + js.getString("nickname") + "> " + js.getString("text"));
                            }
                        }
                    }
                    date = tmp_max_date;
                }
            }catch (ParseException | IOException e){
                logger.log(Level.WARNING,"[LiveChat] "+e);
            }
        }
    };
    public static void launch(){
        timer.schedule(timerTask, 1000,Woftv4.getConfigE().getBiliLive().getPeriod());
    }
    public static void reTimer(){
        spTimer();
        LiveChat.timer = new Timer(true);
    }
    public static void spTimer(){
        if (LiveChat.timer != null){
            if (flag){
                LiveChat.timer.cancel();
            }
            LiveChat.timer = null;
            flag = false;
        }
    }
}
