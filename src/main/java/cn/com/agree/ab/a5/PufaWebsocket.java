package cn.com.agree.ab.a5;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PufaWebsocket extends WebSocketClient {

	private static Logger logger = LoggerFactory.getLogger(PufaWebsocket.class);

	public static Map<String, String> message = new ConcurrentHashMap<>();

	public PufaWebsocket(String uri) throws Exception {
		this(new URI(uri));
	}

	public PufaWebsocket(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		logger.info("握手成功");
//		new Thread(() -> {
//			this.send(new byte[] { 50 });
//		}).start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(String s) {
		System.out.println("收到消息==========" + s);
		logger.info("收到消息==========" + s);
		try {
			Map<String, String> m = (Map<String, String>) JSONObject.parse(s);
			Map<String, Object> ack = new HashMap<>();
			ack.put("msgId", m.get("msgId"));
			ack.put("type", "ack");
			this.send(ack);
		} catch (Exception e) {
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		logger.info("链接已关闭");
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
