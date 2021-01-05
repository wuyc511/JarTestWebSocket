package testlength;

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

	private static boolean flags = false;

	public static boolean isFlags() {
		return flags;
	}

	public static void setFlags(boolean flags) {
		WebSockets.flags = flags;
	}

	CountDownLatch countDown;
	Map<String, Object> map1 = new HashMap<>();
	public WebSockets(URI serverUri) {
		super(serverUri);
	}

	public WebSockets(String string, CountDownLatch countDownLatch, Map<String, Object> map1)
			throws URISyntaxException {
		this(new URI(string));
		this.countDown = countDownLatch;
		this.map1 = map1;
	}



	@Override
	public void onMessage(String message) {
		// 自增的 i 需要放在 onMessage()方法里，否则会在多线程高并发下会使线程结束时i还在自增。
		AtomicInteger i = new AtomicInteger(2);
		System.out.println("MESSAGE : " + message);
		try {
			Map<String, Object> map = parses(message);
			if (map.containsKey("end")) {
				Map<String, Object> ack = new HashMap<>();
				ack.put("msgId", map.get("msgId"));
				ack.put("type", "ack");
				this.send(ack);
				if ((boolean) map.get("end") == true) {
					countDown.countDown();
					flags = true;
					close();
				} else {
					new Thread(() -> {
						map.put("url", i.getAndIncrement() + ".txt");
						map.put("path", map1.get("path"));
						map.put("OID", map1.get("OID"));
						countDown.countDown();
						new HttpURLConnectionDemos().doPost(map);
					}).start();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onOpen(ServerHandshake serverHandshake) {
	}

	@Override
	public void onClose(int i, String s, boolean b) {
	}

	@Override
	public void onError(Exception e) {
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

	@SuppressWarnings("unchecked")
	private Map<String, Object> parses(String string) throws UnsupportedEncodingException {
		Map<String, Object> map = JSONObject.parseObject(string);
		// 线程刚开启时执行第一次交易
		if (map.containsKey("code")) {
			Map<String, Object> firstMap = new HashMap<>();
			firstMap.put("StepKey", "");
			firstMap.put("sessionID", "");
			firstMap.put("msgId", "");
			return firstMap;
			// 线程执行之后的四次请求
		} else {
			Map<String, Object> afterMap = new HashMap<>();
			String StepKey = (String) ((Map<String, Object>) ((Map<String, Object>) map.get("data")).get("meta"))
					.get("stepKey");
			String sessionID = (String) ((Map<String, Object>) ((Map<String, Object>) map.get("data")).get("meta"))
					.get("sessionID");
			boolean end = (boolean) ((Map<String, Object>) ((Map<String, Object>) map.get("data")).get("meta"))
					.get("end");
			String msgId = (String) map.get("msgId");
			afterMap.put("StepKey", StepKey);
			afterMap.put("sessionID", sessionID);
			afterMap.put("end", end);
			afterMap.put("msgId", msgId);
			return afterMap;
		}

	}

}
