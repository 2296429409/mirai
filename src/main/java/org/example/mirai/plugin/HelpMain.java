package org.example.mirai.plugin;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.FileReader;

public class HelpMain {

    private static final String file = "C:\\Users\\Administrator\\Desktop\\mirai\\config\\help.txt";


    private static String readfile(){
        String data = "";
        try (FileReader fr = new FileReader(file)) {
            char[] chs = new char[1 << 20];
            int len = fr.read(chs);
            data = String.valueOf(chs, 0, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void help(Group group) {
        MessageChain chain = new MessageChainBuilder()
                .append(new PlainText(readfile()))
                .build();
        group.sendMessage(chain);
    }

    public static void help(Friend friend) {
        MessageChain chain = new MessageChainBuilder()
                .append(new PlainText(readfile()))
                .build();
        friend.sendMessage(chain);
    }


}
