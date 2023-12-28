package club.windofall.woftv4.utils;

import club.windofall.woftv4.Woftv4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpRequest {
    private static final Logger logger = Woftv4.getlogger();
    public static String doGet(String httpUrl, HashMap<String,Object> map){
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result =new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            for (Map.Entry<String,Object> item : map.entrySet()){
                connection.setRequestProperty(item.getKey(),item.getValue().toString());
            }
            connection.setReadTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String temp;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,"["+HttpRequest.class.getSimpleName()+"] "+e);
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING,"["+HttpRequest.class.getSimpleName()+"] "+e);
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING,"["+HttpRequest.class.getSimpleName()+"] "+e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result.toString();
    }
}
