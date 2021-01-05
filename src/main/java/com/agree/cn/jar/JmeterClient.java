package com.agree.cn.jar;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class JmeterClient extends AbstractJavaSamplerClient {

	private String OID;
	private String tradeName;
	private String tradeTitle;
	private String sessionID;
	private String stepKey;
	private String path1;
	private String path;
	private String moduleName;
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	private Boolean judge = false;

	Map<String, Object> map = new HashMap<String, Object>();
	Map<String, Object> mapData = new HashMap<String, Object>();

	public Arguments getDefaultParameters() {

		Arguments args = new Arguments();
//		args.addArgument("path", "http://49.233.250.34/wangyang/moduleb-aase/aase/index");
//		args.addArgument("path1", "http://49.233.250.34:80/wangyang/moduleb-aase/aase/openTrade");
//		args.addArgument("path", "http://49.233.250.34:80/wangyang/moduleb-aase/aase/execTrade");

		// 重新做压测的ip地址 服务如下两行：
		args.addArgument("path1", "http://192.9.200.106:30002/openTrade");
		args.addArgument("path", "http://192.9.200.106:30002/execTrade");

		args.addArgument("Content-Type", "application/json");
		args.addArgument("X-ABX-ModuleName", "ModuleB");
		args.addArgument("X-ABX-TradeName", "trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
		args.addArgument("X-ABX-TradeTitle", "%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
		args.addArgument("X-ABX-StepKey", "");
		args.addArgument("X-ABX-SessionID", "");
		return args;
	}


	@Override
	public void setupTest(JavaSamplerContext context) {
		String str = UUID.randomUUID().toString();
		try {
			map = mapData = (new ReadTxt()).readingTxt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String tempString = str.substring(0, 8) + str.substring(9, 13) +
				str.substring(14, 18) + str.substring(19, 23) +
				str.substring(24);
		OID = tempString;
		path1 = context.getParameter("path1");
		path = context.getParameter("path");
		tradeName = context.getParameter("X-ABX-TradeName");
		tradeTitle = context.getParameter("X-ABX-TradeTitle");
		sessionID = context.getParameter("X-ABX-StepKey");
		stepKey = context.getParameter("X-ABX-StepKey");
//		modulName = context.getParameter("X-ABX-ModuleName");// X-ABX-ModuleName
		moduleName = context.getParameter("X-ABX-ModuleName");// X-ABX-ModuleName
		map.put("OID", OID);
		map.put("tradeName", tradeName);
		map.put("tradeTitle", tradeTitle);
		map.put("StepKey", stepKey);
		map.put("path", path);
		map.put("sessionID", sessionID);
		map.put("moduleName", moduleName);
		WebSockets webSocket;   //
		try {
//			webSocket = new WebSockets("ws://192.9.200.106/wangyang/message/message/websocket", countDownLatch,map);
			webSocket = new WebSockets("ws://192.9.200.106:8080/websocket", countDownLatch,map);
			webSocket.createWebSocket();
			Map<String, Object> initMessage = new HashMap<>();
			Map<String, String> data = new HashMap<>();
			initMessage.put("type", "init");
			initMessage.put("id", OID);
			initMessage.put("data", data);
			webSocket.sendData(initMessage);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 每个线程执行多次,相当于loadrunner的Action()方法 (non-Javadoc)
	 */
	public SampleResult runTest(JavaSamplerContext arg0) {
		boolean if_success = true;// 测试结果标记位
		SampleResult sr = new SampleResult(); // 为避免多线程问题，设置sr为局部变量
		HttpURLConnectionDemos httpUDs = new HttpURLConnectionDemos();
		sr.setSampleLabel("java请求");
		try {
			Map<String, Object> fMap = new HashMap<>();
			//fMap = map;
			fMap.put("StepKey", "");
			fMap.put("sessionID", "");
			fMap.put("OID", OID);
			fMap.put("path1", path1);
			fMap.put("moduleName", moduleName);
			// put第一个交易的请求体
			fMap.put("map1",map.get("map1"));
			judge = true;
			fMap.put("judge", judge);
			int coolNum = 1;
			sr.sampleStart();
			/**------------------------------------------------------------/
			 |                    开始发起第一次请求                        |
			 /===========================================================*/
			httpUDs.doPost(fMap,coolNum);

			sr.setResponseData("响应结果", "utf-8"); // 第二个参数 为编码， 设置JMeter GUI "取样器结果" DataEncoding: utf-8
			sr.setDataType(SampleResult.TEXT); // 设置JMeter GUI "取样器结果" Data type ("text"|"bin"|""):text
			sr.setResponseMessageOK(); // 设置JMeter GUI "取样器结果" Response message: OK
			sr.setResponseCodeOK(); // 设置JMeter GUI "取样器结果" Response code: 200
			if_success = true;
		} catch (Exception e) {
			if_success = false; // 请求失败
			sr.setResponseCode("500"); // 设置JMeter GUI "取样器结果" Response code: 500
			e.printStackTrace();
		} finally {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sr.sampleEnd();
			sr.setSuccessful(if_success);
		}
		return sr;
	}

	public void teardownTest() {

	}
}
