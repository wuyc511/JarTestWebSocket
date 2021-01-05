package com.agree.cn.jar;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReadTxt {
    /**
     * 读取请求体的txt文件内容的方法
     * @return 返回请求获取到的所有请求头的数据--Map<String, Object>格式
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Map<String, Object> readingTxt() throws FileNotFoundException, IOException {
        StringBuffer stringbuffer = new StringBuffer();
        Map<String, Object> mapData1 = new HashMap<>();
        Map<String, Object> mapData2 = new HashMap<>();
        Map<String, Object> mapData3 = new HashMap<>();
        Map<String, Object> mapData4 = new HashMap<>();
        Map<String, Object> mapData = new HashMap<String, Object>();
        for(int i = 1; i < 5; i++) {
            try(  // 使用语法糖
//				FileInputStream fis = new FileInputStream("/home/user/data/" + map.get("url"));
                  FileInputStream fis = new FileInputStream("F:/data/" + i + ".txt");
                  InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
                  BufferedReader inBR = new BufferedReader(inputStreamReader)
            ) {
                String str = null;
                while ((str = inBR.readLine()) != null) {
                    stringbuffer.append(str);
                }
            }
            String jsonString = stringbuffer.toString();
            Map<String, Object> mapJson = (Map<String, Object>) JSONObject.parseObject(jsonString);
            // 清除strbuffer中的所有内容，才能作下一次的循环读取数据。
            int sbLength = stringbuffer.length();
            stringbuffer.delete(0, sbLength);
            if(i == 1) {
                mapData1 = mapJson;
                mapData.put("map1", mapData1);
            }
            if(i == 2) {
                mapData2 = mapJson;
                mapData.put("map2", mapData2);
            }
            if(i == 3) {
                mapData3 = mapJson;
                mapData.put("map3", mapData3);
            }
            if(i == 4) {
                mapData4 = mapJson;
                mapData.put("map4", mapData4);
            }
        }
        return mapData;
    }

}
