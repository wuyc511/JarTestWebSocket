package com.agree.cn.jar;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

public class TestJarClass {
	
	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			Thread thread = new Thread(() -> {  // lamda表达式
				JmeterClient test = new JmeterClient();
				Arguments arg0 = test.getDefaultParameters();
				JavaSamplerContext argResult = new JavaSamplerContext(arg0);
				test.setupTest(argResult);
				test.runTest(argResult);
			});
			thread.start();
		}

	}
}
