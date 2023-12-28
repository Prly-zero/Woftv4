package club.windofall.woftv4.utils;

import club.windofall.woftv4.Woftv4;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvCK {
    private static final Logger logger = Woftv4.getlogger();
    public static void CKBaseDIR(){
        File file = new File("plugins/WofT");
        if(!file.exists()){file.mkdirs();}
        file = new File("plugins/WofT/DB");
        if(!file.exists()){file.mkdirs();}
    }
    public static void CKWorldN(){
        try{
            File file=new File("plugins/WofT/WorldN.json");
            if(!file.exists()){
                file.createNewFile();
                HashMap<String,String> map =new HashMap<>();
                map.put("world","主世界");
                map.put("world_nether","下界");
                map.put("world_the_end","末地");
                String data = new Gson().toJson(map);
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), false);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                bw.write(data);
                bw.close();
            }
        }catch(Exception e){
            logger.log(Level.WARNING,"["+EnvCK.class.getSimpleName()+"] "+e);
        }
    }
    public static void ckALL(){
        CKBaseDIR();
        CKWorldN();
    }
}
