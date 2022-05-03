package com.ruoyi.selene.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:selene.conf")
public class SeleneConifg {

    private static String chromeDriverPath;

    private static String geckoDriverPath;

    @Value("${webdriver.chrome.driver}")
	public void setChromeDriverPath(String chromeDriverPath) {
    	SeleneConifg.chromeDriverPath = chromeDriverPath;
	}

    @Value("${webdriver.gecko.driver}")
	public void setGeckoDriverPath(String geckoDriverPath) {
    	SeleneConifg.geckoDriverPath = geckoDriverPath;
	}

	public static String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public static String getGeckoDriverPath() {
		return geckoDriverPath;
	}


}
