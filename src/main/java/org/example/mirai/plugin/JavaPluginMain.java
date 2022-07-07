package org.example.mirai.plugin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class JavaPluginMain extends JavaPlugin {


    public static final JavaPluginMain INSTANCE = new JavaPluginMain();

    private JavaPluginMain() {
        super(new JvmPluginDescriptionBuilder("org.example.mirai-example", "0.1.0")
                .info("EG")
                .build());
    }

    private static String filepath = "C:\\Users\\Administrator\\Desktop\\mirai\\imgsc\\";

    private static String imgxzlog = "C:\\Users\\Administrator\\Desktop\\xzlog\\%s.txt";

    private static String imgxzlogRepeat = "";

    private static String setupattern = "^来\\d*张\\S*色图$";

    private static long yourQQNumber = 2296429409l;

    private String speakRepeat = "";

    //真心求图
    private long setu99Qid = 0l;

    ConcurrentHashMap<Long, Long> limit = new ConcurrentHashMap<>();

    static volatile String schedule = "0";

    public static final String filew = "C:\\Users\\Administrator\\Desktop\\xz\\";

    @Override
    public void onEnable() {

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, g -> {
            if (schedule.endsWith("0")) {
                schedule = "1";
                new Schedule().run(g);
            }
            if (g.getMessage().contentToString().equals("测试消息")){

            }
            //监听群消息
            String msgstr = g.getMessage().contentToString();
            Group group = g.getGroup();
            String groupId = String.valueOf(group.getId());
            Long senderId = g.getSender().getId();
            if (msgstr.equals("来点色图")) {
                if (limit.containsKey(senderId) && new Date().getTime() < limit.get(senderId) + 1000 * 60 * 3) {
                    At atSender = new At(senderId);
                    MessageChain chain = new MessageChainBuilder()
                            .append(atSender)
                            .append(new PlainText("年轻人要节制点啊( ･´ω`･ )"))
                            .build();
                    group.sendMessage(chain);
                    return;
                }
                limit.put(senderId, new Date().getTime());
                File[] files = new File(filepath + "gkd").listFiles();
                File file = files[new Random().nextInt(files.length)];
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
                    BufferedImage image = ImageIO.read(bufferedInputStream);
                    BufferedImage mainfunction = TankImageUtils.mainfunction(image);
                    InputStream inputStream = TankImageUtils.imageToStream(mainfunction);
                    Image uploadImage = ExternalResource.uploadAsImage(inputStream, group);
                    group.sendMessage(uploadImage).recallIn(120000L);
                } catch (Exception e) {
                }
                List<String> list = new ArrayList<>();
                String api = String.format("https://api.lolicon.app/setu/v2?r18=1&proxy=i.pixiv.re&num=%s&size=regular", 2);
                JSONObject jsonObject = HttpClientUtils.sendGetOld(api);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int item = 0; item < data.size(); item++) {
                    JSONObject urls = data.getJSONObject(item).getJSONObject("urls");
                    String original = (String) urls.get("regular");
                    list.add(original);
                }
                list.forEach(url -> {
                    try {
                        BufferedImage image = ImageIO.read(new URL(url));
                        BufferedImage mainfunction = TankImageUtils.mainfunction(image);
                        InputStream inputStream = TankImageUtils.imageToStream(mainfunction);
                        Image uploadImage = ExternalResource.uploadAsImage(inputStream, group);
                        group.sendMessage(uploadImage).recallIn(120000L);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }


        });
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, f -> {
            //监听好友消息
            if (f.getSender().getId() == yourQQNumber) {
                String msgstr = f.getMessage().contentToString();
                //图片
                if (MiraiConfig.imgConfig.containsKey(msgstr)) {
                    File file = new File(filepath + MiraiConfig.imgConfig.get(msgstr));
                    File[] files = file.listFiles();
                    File fileimg = files[new Random().nextInt(files.length)];
                    try (FileInputStream is = new FileInputStream(fileimg);) {
                        Image uploadImage = ExternalResource.uploadAsImage(is, f.getFriend());
                        String sendMessage = "[mirai:image:" + uploadImage.getImageId() + "]";
                        f.getFriend().sendMessage(MiraiCode.deserializeMiraiCode(sendMessage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //配置文件
                if (msgstr.indexOf("配置") == 0) {
                    String[] split = msgstr.split("-");
                    if (split.length == 4) {
                        if (split[1].equals("qun")) {
                            MiraiConfig.qunConfig.put(split[2], split[3].equals("1") ? true : false);
                        }
                        if (split[1].equals("img")) {
                            MiraiConfig.imgConfig.put(split[2], split[3]);
                        }
                        if (split[1].equals("speak")) {
                            MiraiConfig.speakConfig.put(split[2], split[3]);
                        }
                    }
                    MiraiConfig.setmapConfig();
                }
            }
        });
    }
}