package com.agree.cn.jar;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WebSockets extends WebSocketClient {
	Map<String, Object> map1 = new HashMap<String, Object>();
	private AtomicInteger coolNum = new AtomicInteger(2);
	CountDownLatch countDownLatch;
	public WebSockets(String uri) throws Exception {
		this(new URI(uri));
	}
	public WebSockets(URI serverUri) {
		super(serverUri);
	}
	public WebSockets(String string, CountDownLatch countDownLatch, Map<String, Object> map1)
			throws URISyntaxException {

		this(new URI(string));
		this.map1 = map1;
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		System.out.println("onpen");
	}
	@Override
	public void onMessage(String message) {
		try {
			Map<String, Object> map = parses(message);
			HttpURLConnectionDemos httpUDs = new HttpURLConnectionDemos();
			if (map.containsKey("end")) {
				//在确定推送的消息中包含end时进行场景判断,再判断end为false继续执行,为true则结束
				Map<String, Object> ack = new HashMap<>();
				ack.put("msgId", map.get("msgId"));
				ack.put("type", "ack");
				this.send(ack);
				if (((Boolean) map.get("end") == false)  && ((Boolean) map.get("msgType") == false)) {
					System.out.println(coolNum + "==" + message);
					map.put("map2", map1.get("map2"));
					map.put("map3", map1.get("map3"));
					map.put("map4", map1.get("map4"));
					map.put("path", map1.get("path"));
					map.put("OID", map1.get("OID"));
					// 在服务器报错的原因就是此处少put了moduleName
					map.put("moduleName", map1.get("moduleName"));
					map.put("judge", false);

					new Thread(() -> {
						httpUDs.doPost(map, coolNum.getAndIncrement());
					}).start();

				} else {
					System.out.println(coolNum + "==" + message);
					countDownLatch.countDown();
					close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		System.out.println("close");
	}

	@Override
	public void onError(Exception e) {
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

	@SuppressWarnings("unchecked")
	private Map<String, Object> parses(String string) throws UnsupportedEncodingException {
		Map<String, Object> map = (Map<String, Object>) JSONObject.parseObject(string);
		Map<String, Object> reqMap = new HashMap<>();
		// 线程刚开启时第一次执行
		if (map.containsKey("code")) {
//			Map<String, Object> FMap = new HashMap<>();
			reqMap.put("StepKey", "");
			reqMap.put("sessionID", "");
			reqMap.put("msgId", "");
			return reqMap;
			// 线程执行之后的四次请求
		} else {
//			Map<String, Object> reqMap = new HashMap<>();
			String StepKey = (String) ((Map<String, Object>) ((Map<String, Object>) map.get("data")).get("meta"))
					.get("stepKey");
			String sessionID = (String) ((Map<String, Object>) ((Map<String, Object>) map.get("data")).get("meta"))
					.get("sessionID");
			Boolean end = (Boolean) ((Map<String, Object>) ((Map<String, Object>) map.get("data")).get("meta"))
					.get("end");
			String msgId = (String) map.get("msgId");
			Boolean msgType = (Boolean) map.containsKey("msgType");
			reqMap.put("StepKey", StepKey);
			reqMap.put("sessionID", sessionID);
			reqMap.put("end", end);
			reqMap.put("msgId", msgId);
			reqMap.put("msgType", msgType);
			return reqMap;
		}

	}


}