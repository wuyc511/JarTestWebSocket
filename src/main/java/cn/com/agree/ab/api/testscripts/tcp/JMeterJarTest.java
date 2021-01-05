package cn.com.agree.ab.api.testscripts.tcp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class JMeterJarTest extends AbstractJavaSamplerClient  {
	public static void main(String[] args) {
		JMeterJarTest jms = new JMeterJarTest();
		Arguments arguments = jms.getDefaultParameters();
		JavaSamplerContext context = new JavaSamplerContext(arguments);
		jms.setupTest(context);
		jms.runTest(context);


	}


	/**
	 * 执行请求头的部分
	 * @return a
	 */
	public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("path","http://192.9.180.42/abside/modulea-aase/aase/index");
//        arguments.addArgument("path","http://49.233.250.34:80/wangyang/modulea-aase/aase/index");

        arguments.addArgument("X-ABX-TradeName", "trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
        arguments.addArgument("X-ABX-ModuleName","ModuleA");
        arguments.addArgument("X-ABX-TradeTitle","%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
        arguments.addArgument("X-ABX-StepKey","");  //(String) map.get("StepKey")
        arguments.addArgument("X-ABX-SessionID",""); //(String) map.get("sessionID")

        return arguments;
    }
	private String oid;
	private String path ;
	private String sessionId ;
	private String tradeName ;
	private String moduleName;
	private String tradeTitle ;
	private String stepKey;

	/**
	 * 进行初始化
	 */
	public void setupTest(JavaSamplerContext arg) {
		oid = UUID.randomUUID().toString().replace("-", "");
		path = arg.getParameter("path");
		sessionId = arg.getParameter("X-ABX-SessionID");
		tradeName = arg.getParameter("X-ABX-TradeName");
		moduleName = arg.getParameter("X-ABX-ModuleName");
		tradeTitle = arg.getParameter("X-ABX-TradeTitle");
		stepKey = arg.getParameter("X-ABX-StepKey");
	}

	/**
	 * webSocket的部分
	 */
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Map<String, Object> map = new HashMap<>();
		map.put("oid",oid);
		map.put("path",path);
		map.put("sessionId",sessionId);
		map.put("tradeName",tradeName);
		map.put("moduleName",moduleName);
		map.put("tradeTitle",tradeTitle);
		map.put("stepKey",stepKey);
		sampleResult.setSampleLabel("Java请求"); // 事务名
		sampleResult.sampleStart(); // jmeter 开始统计响应时间标记
		try {
			WebSocket webSocket = new WebSocket("ws://192.9.180.42/abside/message/message/websocket", countDownLatch, map);
			webSocket.createWebSocket();
			Map<String, Object> initMessage = new HashMap<>();
			Map<String, String> data = new HashMap<>();
			initMessage.put("type", "init");
			initMessage.put("id", oid);
			initMessage.put("data", data);
			webSocket.sendData(initMessage);

			sampleResult.setResponseCode("200");
	        sampleResult.setResponseData("data return by server", "utf-8");
	        sampleResult.setResponseCodeOK();
	        sampleResult.setSuccessful(true);
		} catch (Exception e) {
			e.printStackTrace();
			sampleResult.setSuccessful(false);
		} finally {
			try {
				countDownLatch.await();
				sampleResult.sampleEnd();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		return sampleResult;
	}


	public void teardownTest(JavaSamplerContext context) {
		// 5、end() --断开
	}



}
