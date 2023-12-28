package club.windofall.woftv4.utils;

import club.windofall.woftv4.Woftv4;
import club.windofall.woftv4.entity.ConfigE;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class ConfigUtil {
    public static final String path = "plugins/WofT/config.yaml";
    public static void wYaml() throws IOException {
        ConfigE configE = new ConfigE();
        wYaml(configE);
    }
    public static void wYaml(ConfigE configE) throws IOException {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(new Constructor(ConfigE.class,new LoaderOptions()),new Representer(dumperOptions));
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(yaml.dumpAs(configE,Tag.MAP, DumperOptions.FlowStyle.BLOCK));
        fileWriter.close();
    }
    public static void updateYaml() throws IOException {
        File file = new File(path);
        if (file.exists()){
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(new Constructor(ConfigE.class,new LoaderOptions()),new Representer(dumperOptions));
            InputStream inputStream = Files.newInputStream(Path.of(path));
            Woftv4.setConfigE(yaml.load(inputStream));
            inputStream.close();
        }else{
            wYaml();
            Woftv4.setConfigE(new ConfigE());
        }
    }
}
