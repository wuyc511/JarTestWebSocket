package testlength;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class Test extends AbstractJavaSamplerClient {
	private String OID;
	private String tradeName;
	private String tradeTitle;
	private String sessionID;
	private String stepKey;
	private String path;
	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			Thread thread = new Thread(() -> {
				Test test = new Test();
				Arguments arg0 = test.getDefaultParameters();
				JavaSamplerContext argResult = new JavaSamplerContext(arg0);
				test.setupTest(argResult);
				test.runTest(argResult);
			});
			thread.start();
		}
	}
	public Arguments getDefaultParameters() {
		Arguments args = new Arguments();
		args.addArgument("path", "http://192.9.180.42/abside/modulea-aase/aase/index");
		args.addArgument("Content-Type", "application/json");
		args.addArgument("X-ABX-ModuleName", "ModuleA");
		args.addArgument("X-ABX-TradeName", "trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
		args.addArgument("X-ABX-TradeTitle", "%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
		args.addArgument("X-ABX-StepKey", "");
		args.addArgument("X-ABX-SessionID", "");
		return args;
	}
	@Override
	public void setupTest(JavaSamplerContext context) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		String str = UUID.randomUUID().toString();
		String tempString = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		OID = tempString;
		path = context.getParameter("path");
		tradeName = context.getParameter("X-ABX-TradeName");
		tradeTitle = context.getParameter("X-ABX-TradeTitle");
		sessionID = context.getParameter("X-ABX-StepKey");
		stepKey = context.getParameter("X-ABX-StepKey");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OID", OID);
		map.put("tradeName", tradeName);
		map.put("tradeTitle", tradeTitle);
		map.put("StepKey", stepKey);
		map.put("path", path);
		map.put("sessionID", sessionID);
		WebSockets webSocket;
		try {
			webSocket = new WebSockets("ws://192.9.180.42/abside/message/message/websocket", countDownLatch,map);
			webSocket.createWebSocket();
			Map<String, Object> initMessage = new HashMap<>();
			Map<String, String> data = new HashMap<>();
			initMessage.put("type", "init");
			initMessage.put("id", OID);
			initMessage.put("data", data);
			webSocket.register(initMessage);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception ei) {
			ei.printStackTrace();
		}
	}
	/**
	 * 每个线程执行多次,相当于loadrunner的Action()方法 (non-Javadoc)
	 */
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult sampleResult = new SampleResult(); // 为避免多线程问题，设置sampleResult为局部变量
		sampleResult.setSampleLabel("JAVA请求");
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			Map<String, Object> firstMap = new HashMap<>();
			firstMap.put("StepKey", "");
			firstMap.put("sessionID", "");
			firstMap.put("OID", OID);
			firstMap.put("path", path);
			firstMap.put("url", "1.txt");
			sampleResult.sampleStart();
			//开始发起第一次请求
			new HttpURLConnectionDemos().doPost(firstMap);
			while (WebSockets.isFlags()) {
				break;
			}
			countDownLatch.countDown();
			sampleResult.setResponseData("响应结果", "utf-8"); // 第二个参数 为编码， 设置JMeter GUI "取样器结果" DataEncoding: utf-8
			sampleResult.setDataType(SampleResult.TEXT); // 设置JMeter GUI "取样器结果" Data type ("text"|"bin"|""):text
			sampleResult.setResponseMessageOK(); // 设置JMeter GUI "取样器结果" Response message: OK
			sampleResult.setResponseCodeOK(); // 设置JMeter GUI "取样器结果" Response code: 200
			sampleResult.setSuccessful(true);
			countDownLatch.await();
		} catch (Exception e) {
			// 请求失败
			sampleResult.setSuccessful(false);
			sampleResult.setResponseCode("500"); // 设置JMeter GUI "取样器结果" Response code: 500
			e.printStackTrace();
		} finally {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sampleResult.sampleEnd();
		}
		return sampleResult;
	}

	public void teardownTest() {

	}
}
