package cn.com.agree.ab.api.testscripts.tcp;

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


public class HttpUtil {
	public String doPost(Map<String, Object> map){
        OutputStream outputStream = null;
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
//            URL url = new URL("http://192.9.180.42/abside/modulea-aase/aase/index");
        	URL url = new URL((String) map.get("path"));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("X-ABX-SessionID", (String) ((Map<String, Object>) map).get("sessionID"));
            conn.setRequestProperty("X-ABX-TradeName", "trade%2F%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
    		conn.setRequestProperty("X-ABX-ModuleName", "ModuleA");
            conn.setRequestProperty("X-ABX-TradeTitle", "%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E7%BB%B4%E6%8A%A4");
            conn.setRequestProperty("X-ABX-StepKey",URLEncoder.encode((String) ((Map<String, Object>) map).get("StepKey"),"UTF-8") );
            conn.setRequestProperty("X-ABX-OID", "%7B%0A%09%22A%22%3A%22" + map.get("oid") + "%22%0A%7D");
            conn.connect();
            outputStream = conn.getOutputStream();
        	//获取请求体的数据
            StringBuffer strbuffer = new StringBuffer();
            String toStr = "";
    		try {
    			FileInputStream fis = new FileInputStream("F:/data/"+((Map<String, Object>) map).get("url"));
    			InputStreamReader inputStreamReader = new InputStreamReader(fis,"UTF-8");
    			BufferedReader inBR = new BufferedReader(inputStreamReader);
    			String str = null;
    			while ((str = inBR.readLine()) != null) {// 使用readLine方法，一次读一行
    				strbuffer.append(str);
    			}
    			inBR.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		toStr = strbuffer.toString();
    		outputStream.write(toStr.getBytes(StandardCharsets.UTF_8));
    		outputStream.flush();
            conn.getResponseMessage();
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                if(outputStream != null){
                	outputStream.close();
                }
                if(in != null){
                    in.close();
                    conn.disconnect();
                }
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
		return "";
    }

}
