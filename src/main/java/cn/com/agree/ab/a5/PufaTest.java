package cn.com.agree.ab.a5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PufaTest {

	public static void main(String[] args) throws Exception {

		List<PufaWebsocket> list = new ArrayList<>();
		for(int i = 0; i < 1; i++ ) {
			PufaWebsocket wct = new PufaWebsocket("ws://127.0.0.1:8080/websocket");
//			PufaWebsocket wct = new PufaWebsocket("ws://[fe80::bc54:b452:bb41:90a2]:8080/websocket");
			wct.createWebSocket();
			
			Map<String, Object> initMessage = new HashMap<>();
			Map<String, String> data = new HashMap<>();
			initMessage.put("type", "init");
			initMessage.put("id","oid" + i);
			initMessage.put("data", data);
			
			data.put("app", "qq");
			if( i < 5)
				data.put("corporation", "c1");
			else
				data.put("corporation", "c2");
			
			if( i < 10)
				data.put("branch", "b1");
			else
				data.put("branch", "b2");
			wct.register(initMessage);
			list.add(wct);
			
//			wct.setConnectionLostTimeout(10);
		}
		
//		Map<String, Object> initMessage = new HashMap<>();
//		Map<String, String> data = new HashMap<>();
//		initMessage.put("type", "init");
//		initMessage.put("id","oid" + 0);
//		initMessage.put("data", data);
//		data.put("app", "qq");
//		data.put("corporation", "c1");
//		data.put("branch", "b2");
//		list.get(0).send(initMessage);
		

		Thread.sleep(1000000);
	}
}
