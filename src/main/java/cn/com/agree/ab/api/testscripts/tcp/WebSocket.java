package cn.com.agree.ab.api.testscripts.tcp;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WebSocket extends WebSocketClient {
	private boolean end;
	CountDownLatch countDownLatch;
	Map<String, Object> map2;

	public WebSocket(String uri, CountDownLatch countDownLatch, Map<String, Object> map) throws Exception {
		this(new URI(uri));
		this.countDownLatch = countDownLatch;
		this.map2 = map;
	}

	public WebSocket(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		System.out.println("On open the websocket!!");
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void onMessage(String s) {
		System.out.println(s);
		try {


			/**------------------------------------------------------------/
	                           |         交易执行请求 -   启动线程来执行, 避免阻塞导致执行交易时超时（超过15秒）                            |
	         /============================================================*/
			new Thread(()->{

				/**------------------------------------------------------------/
		                        |                                               对消息作进一步的确认，避免阻塞发生                                                         |
				/============================================================*/
				Map<String, String> m = (Map<String, String>) JSONObject.parse(s);
				Map<String, Object> ack = new HashMap<>();
				ack.put("msgId", m.get("msgId"));
				ack.put("type", "ack");
				this.send(ack);
				Map<String, Object> map = new HashMap<>();
				// 使用原子来进行自增将自增的i放在线程内，是为了避免在压测中线程结束时i还在自增。
				AtomicInteger i = new AtomicInteger(1);
				try {
					map = parseT(s, map);
					if ((map != null) && end == false) {
						map.put("url", i.getAndIncrement() + ".txt");
						map.put("oid", map2.get("oid"));
						map.put("path", map2.get("path"));
						map.put("sessionId", map2.get("sessionId"));
						map.put("tradeName", map2.get("tradeName"));
						map.put("moduleName", map2.get("moduleName"));
						map.put("tradeTitle", map2.get("tradeTitle"));
						map.put("stepKey", map2.get("stepKey"));
						new HttpUtil().doPost(map);
						countDownLatch.countDown();
					} else if(end == true){
						close();
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
	// 解析messages
	@SuppressWarnings("unchecked")
	private Map<String, Object> parseT(String string, Map<String, Object> map0) throws UnsupportedEncodingException {
		map0 = (Map<String, Object>) JSONObject.parseObject(string);
		if (map0.get("data") != null) {
			Map<String, Object> datas = (Map<String, Object>) (map0.get("data"));
			Map<String, Object> meta = (Map<String, Object>) (datas.get("meta"));
			end = (Boolean) meta.get("end");
			if (!end) {
				String StepKey = (String) ((Map<String, Object>) ((Map<String, Object>) map0.get("data")).get("router")).get("stepKey");
				String sessionID = (String) ((Map<String, Object>) ((Map<String, Object>) map0.get("data")).get("meta")).get("sessionID");
				String msgId = (String) map0.get("msgId");
				map0.put("StepKey", StepKey);
				map0.put("sessionID", sessionID);
				map0.put("end", end);
				map0.put("msgId", msgId);
				return map0;
			}
		} else if (map0.get("msg").equals("success")) {
			map0.put("StepKey", "");
			map0.put("sessionID", "");
			return map0;
		}
		return null;
	}

}
