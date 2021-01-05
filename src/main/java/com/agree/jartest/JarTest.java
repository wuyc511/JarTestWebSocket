//package com.agree.jartest;
//
//import com.agree.utils.WebSocket;
//import org.apache.jmeter.config.Arguments;
//import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
//import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
//import org.apache.jmeter.samplers.SampleResult;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.CountDownLatch;
//
//
///**
// * 使用Jar来实现压力测试，加上websocket来实现消息推送，并将消息的进行解析，转换为Map
// */
//public class JarTest extends AbstractJavaSamplerClient {
//    private WebSocket webSocket;
//    public static void main(String[] args) {
//        System.out.println("==========main test==============");
//        JarTest jms = new JarTest();
//        Arguments arguments = jms.getDefaultParameters();
//        JavaSamplerContext context = new JavaSamplerContext(arguments);
//        jms.setupTest(context);
//        jms.runTest(context);
//
//
//    }
//
//
//    /**
//     * 执行请求头的部分
//     * @return Java Request returning to Jmeter interface.
//     */
//    public Arguments getDefaultParameters() {
//        Arguments arguments = new Arguments();
//        arguments.addArgument("path","http://192.9.180.42/abside/modulea-aase/aase/index");
////        arguments.addArgument("Content-Type", "application/json");
////        arguments.addArgument("Accept", "application/json");
//
//        arguments.addArgument("X-ABX-TradeName", "trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
//        arguments.addArgument("X-ABX-ModuleName","ModuleA");
//        arguments.addArgument("X-ABX-TradeTitle","%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
//        arguments.addArgument("X-ABX-StepKey","");  //(String) map.get("StepKey")
//        arguments.addArgument("X-ABX-SessionID",""); //(String) map.get("sessionID")
////        arguments.addArgument("X-ABX-OID","%7B%0A%09%22A%22%3A%22newId13%22%0A%7D");
//
//        return arguments;
//    }
//    private String oid;
//    private String path ;
//    private String sessionId ;
//    private String tradeName ;
//    private String moduleName;
//    private String tradeTitle ;
//    private String stepKey;
//
//    /**
//     * 进行初始化
//     */
//    public void setupTest(JavaSamplerContext arg) {
////		String uuid = UUID.randomUUID().toString();
////		System.out.println("uuid: " + uuid);
////		oid = uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
//        oid = UUID.randomUUID().toString().replace("-", "");
////		oid = UUID.randomUUID().toString();
//        System.out.println("oid: " + oid);
//
//        path = arg.getParameter("path");
//        sessionId = arg.getParameter("X-ABX-SessionID");
//        tradeName = arg.getParameter("X-ABX-TradeName");
//        moduleName = arg.getParameter("X-ABX-ModuleName");
//        tradeTitle = arg.getParameter("X-ABX-TradeTitle");
//        stepKey = arg.getParameter("X-ABX-StepKey");
////		String oid = arg.getParameter("X-ABX-OID");
//
//
//    }
//
//    /**
//     * webSocket的部分
//     */
//    @Override
//    public SampleResult runTest(JavaSamplerContext context) {
//        SampleResult sr = new SampleResult();
//        sr.setSampleLabel("Java请求"); // 事务名
//        // WebSocket 建立连接,注册newId13
//        sr.sampleStart(); // jmeter 开始统计响应时间标记
//        CountDownLatch countDownLatch = new CountDownLatch(4);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("oid",oid);
//        map.put("path",path);
//        map.put("sessionId",sessionId);
//        map.put("tradeName",tradeName);
//        map.put("moduleName",moduleName);
//        map.put("tradeTitle",tradeTitle);
//        map.put("stepKey",stepKey);
//
//        try {
//            webSocket = new WebSocket("ws://192.9.180.42/abside/message/message/websocket", countDownLatch, map);
//            webSocket.createWebSocket();
//            Map<String, Object> initMessage = new HashMap<>();
//            Map<String, String> data = new HashMap<>();
//            initMessage.put("type", "init");
//            initMessage.put("id", oid);
//            initMessage.put("data", data);
//            webSocket.sendData(initMessage);
//
//            sr.setResponseCode("200");
//            sr.setResponseData("data return by server", "utf-8");
//            sr.setResponseCodeOK();
//            sr.setSuccessful(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                countDownLatch.await();
//                sr.setSuccessful(false);
//                sr.sampleEnd();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        return sr;
//    }
//
//
//    public void teardownTest(JavaSamplerContext context) {
//        // 5、end() --断开
//    }
//
//}
