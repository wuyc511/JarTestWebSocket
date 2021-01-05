package testlength;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpURLConnectionDemos {
	public String doPost(Map<String, Object> map) {
		OutputStream outputStream = null;
		HttpURLConnection conn;
		try {
			// 创建http请求体 outputStream
			URL url = new URL((String) map.get("path"));
//			URL url = new URL("http://192.9.180.42/abside/modulea-aase/aase/index");
//			URL url = new URL("http://49.233.250.34:80/wangyang/modulea-aase/aase/index");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("X-ABX-ModuleName", "ModuleA");
			conn.setRequestProperty("X-ABX-SessionID", (String) map.get("sessionID"));
			conn.setRequestProperty("X-ABX-TradeName","trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
			conn.setRequestProperty("X-ABX-TradeTitle", "%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
			conn.setRequestProperty("X-ABX-StepKey", URLEncoder.encode((String) map.get("StepKey"), "UTF-8"));
			conn.setRequestProperty("X-ABX-OID", "%7B%0A%09%22A%22%3A%22"+map.get("OID")+"%22%0A%7D");
			conn.connect();
			outputStream = conn.getOutputStream();
			StringBuffer stringBuffer = new StringBuffer();
			// 使用语法糖：Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件
			try(FileInputStream fis = new FileInputStream("F:/data/" + map.get("url"));
				InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufferReader = new BufferedReader(inputStreamReader);
				) {
				String str;
				while ((str = bufferReader.readLine()) != null) {
					stringBuffer.append(str);
				}
			}
			String jsonString = stringBuffer.toString();
			outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
			outputStream.flush();
			conn.getResponseMessage();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return "true";
	}
}