package com.agree.cn.jar;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpURLConnectionDemos {

	OutputStream os = null;
	BufferedReader in = null;
	HttpURLConnection conn = null;
	URL url = null;
	public void doPost(Map<String, Object> map, int coolNum) {
		String jsonString = null;
		try {
			if((Boolean)map.get("judge") == true) {
				url = new URL((String) map.get("path1"));
			} else {
				url = new URL((String) map.get("path"));
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("X-ABX-ModuleName", ((String)map.get("moduleName")));
			conn.setRequestProperty("X-ABX-SessionID", (String) map.get("sessionID"));
			conn.setRequestProperty("X-ABX-TradeName","trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
			conn.setRequestProperty("X-ABX-TradeTitle", "%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
			conn.setRequestProperty("X-ABX-StepKey", URLEncoder.encode(((String) map.get("StepKey")), "UTF-8"));
			conn.setRequestProperty("X-ABX-VARIABLEJSON", "%7B%0A%09%22CHANNELCODE%22%3A%22C001%22%2C%0A%09%22AGENTFLAG%22%3A%220%22%0A%7D");
			conn.setRequestProperty("X-ABX-OID", (String) map.get("OID"));// 新增
			conn.setRequestProperty("X-ABX-MemberOIDS", "%7B%22"+map.get("OID")+"%22%3A%22A%22%7D");
			conn.setRequestProperty("Charset", "UTF-8");
			// 打印请求头-用于问题的排查，检查请求头的数据是否完整
			System.out.println("getRequestProperties:"+map.get("url") + ":" +conn.getRequestProperties());
			conn.connect();
			os = conn.getOutputStream();


			/*StringBuffer strbuffer = new StringBuffer();
			try(  // 使用语法糖
				  FileInputStream fis = new FileInputStream("F:/data/" + map.get("url"));
				  InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
				  BufferedReader inBR = new BufferedReader(inputStreamReader)
			) {
				String str = null;
				while ((str = inBR.readLine()) != null) {
					strbuffer.append(str);
				}
			}
			String jsonString = strbuffer.toString();*/


			if (coolNum == 1){
				jsonString = (map.get("map1")).toString();
			}
			if (coolNum == 2){
				jsonString = (map.get("map2")).toString();
			}
			if (coolNum == 3){
				jsonString = (map.get("map3")).toString();
			}
			if (coolNum == 4){
				jsonString = (map.get("map4")).toString();
			}
			os.write(jsonString.getBytes(StandardCharsets.UTF_8));
			os.flush();
//			conn.getResponseMessage();
			int responseCode = conn.getResponseCode();
			System.out.println(map.get("url") + "=:responseCode:=" + responseCode);
			System.out.println("flush");
			System.out.println(map.get("url") + "ResponseMessage is == " + conn.getResponseMessage());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
					conn.disconnect();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}


}