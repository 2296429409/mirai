package org.example.mirai.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpClientUtils {

    public static JSONObject get(String url) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            System.out.println("连接错误1");
        }
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject sendGetOld(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return JSONObject.parseObject(result);
    }


    public static JSONObject javtrailersSearch(String query) {
        try {
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .build();
            OkHttpClient client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                    .readTimeout(1000L, TimeUnit.MINUTES).build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://javtrailers.com/api/search?query=" + query + "&page=0")
                    .method("GET", null)
                    .addHeader("authorization", "HAHA_ADAM_HAVE_TO_RESORT_TO_THIS#@!@#")
                    .addHeader("Cookie", "auth.strategy=local; user-country=CN")
                    .build();
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            JSONObject jsonObject = JSON.parseObject(string);
            JSONArray results = jsonObject.getJSONArray("results");
            return results.getJSONObject(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject javtrailersVideo(String query) {
        try {
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .build();
            OkHttpClient client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                    .readTimeout(1000L, TimeUnit.MINUTES).build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://javtrailers.com/api/video/" + query)
                    .method("GET", null)
                    .addHeader("authorization", "HAHA_ADAM_HAVE_TO_RESORT_TO_THIS#@!@#")
                    .addHeader("Cookie", "auth.strategy=local; user-country=CN")
                    .build();
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            JSONObject jsonObject = JSON.parseObject(string);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getAvPerformer(String query, Group group) {
        try {
            OkHttpClient client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                    .readTimeout(1000L, TimeUnit.MINUTES).build();
            Request request = new Request.Builder()
                    .url("https://actress.dmm.co.jp/-/search/=/searchstr=" + query + "/")
                    .method("GET", null)
                    .addHeader("cookie", "age_check_done=1;")
                    .build();
            Response response = client.newCall(request).execute();
            String dataStr = response.body().string();
            if (dataStr.indexOf("該当する女優は見つかりませんでした。") != -1) {
                group.sendMessage("(˘•ω•˘)没找到：" + query);
            } else {
                dataStr = dataStr.substring(dataStr.indexOf("<div class=\"p-section-actressList\""), dataStr.lastIndexOf("<script>"));
                final String[] split = dataStr.split("<li class=\"p-list-actress\"");
                if (split.length == 2) {
                    String id = split[1].substring(
                            split[1].indexOf("href=\"") +
                                    "href=\"".length(),
                            split[1].indexOf("\" class=\"p-list-actress__link\""));
//                    System.out.println(" 找到= id = " + id);
                    getAvPerformerSon(id,group,query);
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 1; i < split.length; i++) {
                        int starti = split[i].indexOf("<p class=\"p-list-actress__name\" data-v-6e1d5150=\"\">");
                        String name = split[i].substring(
                                starti + "<p class=\"p-list-actress__name\" data-v-6e1d5150=\"\">".length(),
                                split[i].indexOf("</p>", starti + "<p class=\"p-list-actress__name\" data-v-6e1d5150=\"\">".length())
                        ).trim();
                        stringBuffer.append(name).append("、");
                    }
                    group.sendMessage("(˘•ω•˘)找到多个，请准确搜索：" + stringBuffer.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            group.sendMessage("(˘•ω•˘) 网络错误");
        }finally {
            JavaPluginMain.ispachi=0;
        }
        return null;
    }


    public static JSONObject getAvPerformerSon(String query, Group group,String avName) {
        try {
            OkHttpClient client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                    .readTimeout(1000L, TimeUnit.MINUTES).build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://actress.dmm.co.jp" + query)
                    .method("GET", null)
                    .addHeader("cookie", "age_check_done=1; age_check_done=1; ckcy=1")
                    .build();
            Response response = client.newCall(request).execute();
            String dataStr = response.body().string();
            dataStr = dataStr.substring(dataStr.indexOf("<div class=\"p-box-productList\" "), dataStr.lastIndexOf("<div class=\"p-box-pagenationArea\" "));
//            System.out.println("dataStr = " + dataStr);
            String[] split = dataStr.split("<li class=\"p-list-product__item\" ");
            List<ForwardMessage.Node> nodes = new ArrayList<>();
            ForwardMessage.Node nodehead = new ForwardMessage.Node(
                    group.getOwner().getId(),
                    (int) (new Date().getTime() / 1000),
                    "腾讯qq",
                    new MessageChainBuilder().append(new PlainText(avName)).build()
            );
            nodes.add(nodehead);
            for (int i = 1; i < split.length && i < 21; i++) {
                String name = split[i].substring(
                        split[i].indexOf("alt=\"") +
                                "alt=\"".length(),
                        split[i].indexOf("\" data-v-56711d8c=\"\"></span>"));
                String img = split[i].substring(
                        split[i].indexOf("<img src=\"") +
                                "<img src=\"".length(),
                        split[i].indexOf("\" alt=\""));
                String date = split[i].substring(
                        split[i].indexOf("<p class=\"p-list-product__item__releaseDate\" data-v-56711d8c=\"\">") +
                                "<p class=\"p-list-product__item__releaseDate\" data-v-56711d8c=\"\">".length(),
                        split[i].indexOf("発売")).trim();
                String id = img;
                if (id.indexOf("https://pics.dmm.co.jp/digital/video/")==-1){
                    id = img.replace("https://pics.dmm.co.jp/mono/movie/adult/9", "");
                }else {
                    id = img.replace("https://pics.dmm.co.jp/digital/video/", "");
                }
                id = id.substring(0, id.lastIndexOf("/"));
                BufferedImage image = ImageIO.read(new URL(img));
                BufferedImage mainfunction = TankImageUtils.mainfunction(image);
                InputStream inputStream = TankImageUtils.imageToStream(mainfunction);
                Image uploadImage = ExternalResource.uploadAsImage(inputStream, group);
                MessageChain chain = new MessageChainBuilder()
                        .append(new PlainText("\n番号：" + id + "\n"))
                        .append(new PlainText("标题：" + name + "\n"))
                        .append(new PlainText("图片预览：" + "\n"))
                        .append(uploadImage)
                        .append(new PlainText("发行日期：" + date + "\n")).build();
                ForwardMessage.Node node = new ForwardMessage.Node(
                        group.getOwner().getId(), (int) (new Date().getTime() / 1000), group.get(group.getOwner().getId()).queryProfile().getNickname(), chain
                );
                nodes.add(node);
            }
            ForwardMessage forwardMessage = new ForwardMessage(
                    Arrays.asList(avName, avName, avName),
                    "开冲", "真的假的", "真的！我在现场", "查看更多……",
                    nodes
            );
            MessageChain chain = new MessageChainBuilder()
                    .append(forwardMessage)
                    .build();
            group.sendMessage(chain).recallIn(120000L);
        } catch (Exception e) {
            e.printStackTrace();
            group.sendMessage("(˘•ω•˘) 网络错误");
        }
        return null;
    }


}
