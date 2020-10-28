package com.agree.jartest;

import com.agree.utils.HttpUtil;
import com.agree.utils.WebSocket;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 使用Jar来实现压力测试，加上websocket来实现消息推送，并将消息的进行解析，转换为Map
 */
public class JarTest {
    private static Logger logger = LoggerFactory.getLogger(JarTest.class);
    public static void main(String[] args) throws Exception {
        /**------------------------------------------------------------/
         |                 WebSocket 链接建立,注册newId13               |
         /============================================================*/
        WebSocket SC = new WebSocket("ws://192.9.180.42/abside/message/message/websocket");
        // 创建WebSocket
        SC.createWebSocket();
        // 对消息推送做一个初始化
        Map<String, Object> initMessage = new HashMap<String, Object>();
        // 数据
        Map<String, String> data = new HashMap<String, String>();
        // 将消息推进去
        initMessage.put("type", "init");
        initMessage.put("id", "newId13");
        initMessage.put("data", data);
        // 发送WebSocket数据
        SC.sendData(initMessage);

        logger.info("info content in the here:" + initMessage);

/**------------------------------------------------------------/
 |                     发起请求 1 --开启交易                    |
 /============================================================*/




    }
}
