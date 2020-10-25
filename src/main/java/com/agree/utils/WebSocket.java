package com.agree.utils;

import com.alibaba.fastjson.JSON;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

/**
 * Websocket客户端，通过它来建立链接获取，消息推送进行解析，并推送
 */
public class WebSocket extends WebSocketClient {

    public static String messages;

    public static String getMessages() {
        return messages;
    }

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
     * logger 和messageMap 用来存储message推送来的消息,转化为map,交给前端调用
     */
    private static Logger logger = LoggerFactory.getLogger(WebSocket.class);

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("连接成功。。。");
        logger.info("握手成功");

    }

    //	@SuppressWarnings("unchecked")
    @Override
    public void onMessage(String message) {
        messages = message;
    }

    @Override
    public void onClose(int i, String s, boolean b) {
//        logger.info("链接已关闭");
    }

    @Override
    public void onError(Exception e) {
        logger.error("发生错误已关闭", e);
    }

    public void createWebSocket() throws Exception {
        if (!this.isClosed() || !this.isClosing()) {
            connectBlocking();
        }
    }

    public void register(Map<String, Object> data) {
        if (this.isOpen()) {
            send(JSON.toJSONString(data));
        }
    }

    public void send(Map<String, Object> sendData) {
        if (this.isOpen()) {
            send(JSON.toJSONString(sendData));
        }
    }



}
