package com.agree.utils;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.Map;

/**
 * 获取请求头信息的工具类
 */
public class HttpUtil {
    public static void postRequestTest(Map map){
        String url = "http://192.9.180.42/abside/modulea-aase/aase/index";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        // 设置请求头的信息内容
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.addHeader("X-ABX-SessionID", (String) map.get("sessionID"));
        httpPost.addHeader("X-ABX-TradeName", "trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
        httpPost.addHeader("X-ABX-ModuleName", "ModuleA");
        httpPost.addHeader("X-ABX-TradeTitle", "%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
        httpPost.addHeader("X-ABX-StepKey", (String) map.get("StepKey"));
        httpPost.addHeader("X-ABX-OID", "%7B%0A%09%22A%22%3A%22newId13%22%0A%7D");
        // 添加BODY的参数， 使用StringBuffer
        StringBuffer strbuffer = new StringBuffer();
        String s = "";
        try {

            // 此处是获取请求体的内容，即是txt文件，通过流的形式来读取
            FileInputStream fis = new FileInputStream("src/main/resources/"+map.get("url"));
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader inBR = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = inBR.readLine()) != null) {// 使用readLine方法，一次读一行
                strbuffer.append(str);
            }
            s = strbuffer.toString();
            inBR.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringEntity entity2 = null;
        try {
            entity2 = new StringEntity(s, "application/json", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(entity2);
        try {
            long t1 = System.currentTimeMillis();
            httpclient.execute(httpPost);
            long t2 = System.currentTimeMillis();
            System.out.println("耗时=============:" + (t2 - t1));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
