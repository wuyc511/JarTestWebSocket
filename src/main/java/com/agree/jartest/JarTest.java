package com.agree.jartest;

import com.agree.utils.HttpUtil;
import com.agree.utils.WebSocket;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 使用Jar来实现压力测试，加上websocket来实现消息推送，并将消息的进行解析，转换为Map
 */
public class JarTest {
    public static void main(String[] args) throws Exception {
        // WebSocket 链接建立,注册newId13
        WebSocket SC = new WebSocket("ws://192.9.180.42/abside/message/message/websocket");
        // 创建WebSocket
        SC.createWebSocket();

        // 对消息推送做一个初始化
        Map<String, Object> initMessage = new HashMap<>();
        // 数据
        Map<String, String> data = new HashMap<>();
        // 将消息推进去
        initMessage.put("type", "init");
        initMessage.put("id", "newId" + 13);
        initMessage.put("data", data);

        // 注册WebSocket
        SC.register(initMessage);

/**------------------------------------------------------------/
 |                     发起请求 1 --开启交易                    |
 /============================================================*/
        Map map = new HashMap();
        map.put("StepKey", "");
        map.put("sessionID", "");
        // 将txt文件添加到Map里面
        map.put("url", "1.txt");

        // 将获取Map中的信息
        HttpUtil.postRequestTest(map);
        String string = SC.getMessages();

        // 解析websocket 中推送的消息message, 在下一步的交易中使用到
        Map map1 = parse(string);
        System.out.println(string);


/**------------------------------------------------------------/
 |                     发起请求 2 -- 证件选择                   |
 /============================================================*/

        map1.put("url", "2.txt");
        HttpUtil.postRequestTest(map1);
        String string2 = SC.getMessages();
        System.out.println(string2);
        Map map2 = parse(string2);

/**------------------------------------------------------------/
 |                     发起请求 3 --读取客户证件信息            |
 /============================================================*/
        map2.put("url", "3.txt");
        HttpUtil.postRequestTest(map2);
        String string3 = SC.getMessages();
        Map map3 = parse(string3);
        System.out.println(string3);

/**------------------------------------------------------------/
 |                     发起请求 4 --新客户确认                  |
 /============================================================*/
        map3.put("url", "4.txt");
        HttpUtil.postRequestTest(map3);
        String string4 = SC.getMessages();
        Map map4 = parse(string4);
        System.out.println(string4);
//        System.out.println(map4);

    }


    /**
     * 此方法是解析websocket 中推送的消息message
     * 将String的类型转换为Map方便获取
     * @param message 获取消息
     * @return 返回值
     * @throws UnsupportedEncodingException 抛异常处理
     */
    public  static Map parse(String message) throws UnsupportedEncodingException {
        Map map = (Map) JSONObject.parseObject(message);
        if (map.get("data") != null) {
            Map datas = (Map) (map.get("data"));
            Map meta = (Map) (datas.get("meta"));
            boolean end = (boolean) meta.get("end");

            // 对是否结束做判断，避免出现空指针异常
            if (!end) {
                Map maps = new HashMap();
                String stepKey = (String) ((Map) ((Map) map.get("data")).get("router")).get("stepKey");
                String StepKey = URLEncoder.encode(stepKey,"UTF-8");
                String sessionID = (String) ((Map) ((Map) map.get("data")).get("meta")).get("sessionID");
                maps.put("StepKey", StepKey);
                maps.put("sessionID", sessionID);
                return maps;
            }
        }
        return null;

    }

}
