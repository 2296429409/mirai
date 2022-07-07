package org.example.mirai.plugin;

import com.alibaba.fastjson.JSON;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class MiraiConfig {

    private static final String url = "C:\\Users\\Administrator\\Desktop\\mirai\\config\\";

    public static Map<String, String> imgConfig = new HashMap();

    public static Map<String, String> speakConfig = new HashMap();

    public static Map<String, Boolean> qunConfig = new HashMap<>();

    static {
        imgConfig();
        speakConfig();
        qunConfig();
    }

    public static void imgConfig() {
        String json = "";
        try (FileReader fr = new FileReader(url + "img.txt")) {
            char[] chs = new char[1 << 20];
            int len = fr.read(chs);
            json = String.valueOf(chs, 0, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgConfig = JSON.parseObject(json, Map.class);
    }

    public static void speakConfig() {
        String json = "";
        try (FileReader fr = new FileReader(url + "speak.txt")) {
            char[] chs = new char[1 << 20];
            int len = fr.read(chs);
            json = String.valueOf(chs, 0, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        speakConfig = JSON.parseObject(json, Map.class);
    }

    public static void qunConfig() {
        String json = "";
        try (FileReader fr = new FileReader(url + "qun.txt")) {
            char[] chs = new char[1 << 20];
            int len = fr.read(chs);
            json = String.valueOf(chs, 0, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        qunConfig = JSON.parseObject(json, Map.class);
    }


    public synchronized static void setmapConfig() {
        String json ="";
        json = JSON.toJSONString(imgConfig);
        try (FileWriter fw = new FileWriter(url + "img.txt")) {
            char[] chars = json.toCharArray();
            fw.write(chars);
        } catch (Exception e) {
            e.printStackTrace();
        }
        json = JSON.toJSONString(speakConfig);
        try (FileWriter fw = new FileWriter(url + "speak.txt")) {
            char[] chars = json.toCharArray();
            fw.write(chars);
        } catch (Exception e) {
            e.printStackTrace();
        }
        json = JSON.toJSONString(qunConfig);
        try (FileWriter fw = new FileWriter(url + "qun.txt")) {
            char[] chars = json.toCharArray();
            fw.write(chars);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
