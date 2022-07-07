package org.example.mirai.plugin;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessage;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Schedule {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) {
//        new Schedule().test();
        final Long test = test();
        System.out.println("test = " + test);

        final long time = new Date().getTime();
        System.out.println("time = " + time);
        System.out.println("time = " + (time+test));


    }

    public void run(GroupMessageEvent event) {
        Timer timer = new Timer();
//        Calendar calendar = Calendar.getInstance();
//        int hour = 23 - calendar.get(Calendar.HOUR_OF_DAY);
//        int i = 1000 * 60 * 60 * hour;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<ForwardMessage.Node> nodes = new ArrayList<>();
                String filewDate = JavaPluginMain.filew + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\\";
//                String filewDate = "C:\\Users\\Administrator\\Desktop\\xz\\2022-07-06\\";
                File listFile = new File(filewDate);
                for (File file : listFile.listFiles()) {
                    try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                        Image uploadImage = ExternalResource.uploadAsImage(inputStream, event.getGroup());
                        nodes.add(new ForwardMessage.Node(
                                event.getSender().getId(), (int) (new Date().getTime() / 1000), event.getSenderName(), uploadImage
                        ));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                ForwardMessage forwardMessage = new ForwardMessage(
                        Arrays.asList("烧鸡嘿嘿", "烧鸡嘿嘿"), "开冲", "烧鸡", "烧鸡", "是烧鸡耶", nodes
                );
                MessageChain chain = new MessageChainBuilder()
                        .append(forwardMessage)
                        .build();
                event.getGroup().sendMessage(chain);
            }
        }, test(), 1000 * 60 * 60 * 24);
    }

    public static Long test() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),23,55,0);
        Date date = calendar.getTime();
        return date.getTime()-new Date().getTime();
    }

}
