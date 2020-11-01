package com.agree.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Websocket客户端，通过它来建立链接获取，消息推送进行解析，并推送
 */
public class WebSocket extends WebSocketClient {

    long tt1;
    long tt2;
    private static int i = 1;
    /** 
     * 有参构造方法
     * @param uri
     * @throws Exception
     */
    public WebSocket(String uri) throws Exception {
        this(new URI(uri));
    }

    public WebSocket(URI serverUri) {
        super(serverUri);
    }


    /**
     * logger 和 messageMap 用来存储message推送来的消息,转化为map,交给前端调用
     */
    private static Logger logger = LoggerFactory.getLogger(WebSocket.class);

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        tt1 = System.currentTimeMillis();
        System.out.println(tt1 + "连接成功.........................");
        logger.info("握手成功............................");

    }

    @Override
    public void onMessage(String message) {
        System.out.println("A message has been received from websocket :=====:" + "\n" + message);
        logger.info("A message has been received from websocket:===:" + "\n" + message);

        /**------------------------------------------------------------/
         |                   对消息作进一步的确认，避免阻塞发生          |
         /============================================================*/
        Map m = (Map) JSONObject.parse(message);
        Map ok = new HashMap<>();
        ok.put("msgId", m.get("msgId"));
        ok.put("type", "ack");
        this.send(ok);

        /**------------------------------------------------------------/
         |     启动线程来执行, 避免阻塞导致执行交易时超时（超过15秒）      |
         /============================================================*/
        new Thread(()->{
            try {
                Map map;
                map = parse(message);
                if (map != null){
                    map.put("url", (i++) + ".txt");
                    HttpUtil.postRequestTest(map);
                } else {
                    // When the program has finished executing, close websocket.
                    close();
                }
            } catch (Exception e) {
                logger.error("AN ERROR WAS FOUND :  " + e);
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        tt2 = System.currentTimeMillis();
        logger.info("Link closed.");

        System.out.println(tt2 + "链接已关闭");
        System.out.println("The total times is : " + (tt2 - tt1));
    }

    @Override
    public void onError(Exception e) {
        System.err.println("发生错误.....已关闭" + e);
    }

    public void createWebSocket() throws Exception {
        if (!this.isClosed() || !this.isClosing()) {
            connectBlocking();
        }
    }

    public void sendData(Map<String, Object> data) {
        if (this.isOpen()) {
            send(JSON.toJSONString(data));
        }
    }

    public void send(Map<String, Object> sendData) {
        if (this.isOpen()) {
            send(JSON.toJSONString(sendData));
        }
    }

    /**
     * 此方法是解析websocket 中推送的消息message
     * 将String的类型转换为Map方便获取
     * @param message 获取需要进行解析的消息
     * @return 返回值
     * @throws UnsupportedEncodingException 抛异常处理
     */
    private static Map parse(String message) throws Exception {
        Map map = JSONObject.parseObject(message);
        if (map.get("data") != null) {
            Map data = (Map) (map.get("data"));
            Map meta = (Map) (data.get("meta"));
            boolean end = (Boolean) meta.get("end");
            // 对是否结束做判断，避免出现空指针异常
            if (!end) {
                Map maps = new HashMap();
                String stepKey = (String) ((Map) ((Map) map.get("data")).get("router")).get("stepKey");
                String StepKey = URLEncoder.encode(stepKey,"UTF-8");
                String sessionID = (String) ((Map) ((Map) map.get("data")).get("meta")).get("sessionID");
                String msgId = (String) ( map.get("msgId"));
                maps.put("StepKey", StepKey);
                maps.put("sessionID", sessionID);
                maps.put("msgId",msgId);
                return maps;
            }
        } else if (map.get("msg").equals("success")){
            Map<String, Object> map2 = new HashMap<>();
            map2.put("StepKey", "");
            map2.put("sessionID", "");
            return map2;
        }
        return null;
    }
}
