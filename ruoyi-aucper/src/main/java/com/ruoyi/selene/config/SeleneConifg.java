package com.ruoyi.selene.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:selene.conf")
public class SeleneConifg {

    private static String chromeDriverPath;
    private static String geckoDriverPath;
    private static int maxSize;
    private static int minIdle;

    @Value("${webdriver.chrome.driver}")
	public void setChromeDriverPath(String chromeDriverPath) {
    	SeleneConifg.chromeDriverPath = chromeDriverPath;
	}

    @Value("${webdriver.gecko.driver}")
	public void setGeckoDriverPath(String geckoDriverPath) {
    	SeleneConifg.geckoDriverPath = geckoDriverPath;
	}

    @Value("${pool.max.size}")
	public void setMaxSize(int maxSize) {
		SeleneConifg.maxSize = maxSize;
	}

    @Value("${pool.min.idle}")
	public void setMinIdle(int minIdle) {
		SeleneConifg.minIdle = minIdle;
	}

	public static String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public static String getGeckoDriverPath() {
		return geckoDriverPath;
	}

	public static int getMaxSize() {
		return maxSize;
	}

	public static int getMinIdle() {
		return minIdle;
	}


}
