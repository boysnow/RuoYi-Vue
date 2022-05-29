package com.ruoyi.selene.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("selene")
public class SeleneConifg {

	public static class Keys {
		public static final String CHROME = "webdriver.chrome.driver";
		public static final String GECKO = "webdriver.gecko.driver";
	}

	private static boolean useExternal;
    private static String webdriverChrome;
    private static String webdriverGecko;
    private static int poolMaxSize;
    private static int poolMinIdle;
    private static String gridJsonHub;
    private static String gridJsonNode;

    @Value("${selene.webdriver.use-external}")
	public void setUseExternal(boolean useExternal) {
		SeleneConifg.useExternal = useExternal;
	}
    @Value("${selene.webdriver.chrome}")
	public void setWebdriverChrome(String webdriverChrome) {
    	SeleneConifg.webdriverChrome = webdriverChrome;
	}
    @Value("${selene.webdriver.gecko}")
	public void setWebdriverGecko(String webdriverGecko) {
    	SeleneConifg.webdriverGecko = webdriverGecko;
	}
    @Value("${selene.pool.max-size}")
	public void setPoolMaxSize(int poolMaxSize) {
		SeleneConifg.poolMaxSize = poolMaxSize;
	}
    @Value("${selene.pool.min-idle}")
	public void setPoolMinIdle(int poolMinIdle) {
		SeleneConifg.poolMinIdle = poolMinIdle;
	}
    @Value("${selene.grid-json.hub}")
	public void setGridJsonHub(String gridJsonHub) {
		SeleneConifg.gridJsonHub = gridJsonHub;
	}
    @Value("${selene.grid-json.node}")
	public void setGridJsonNode(String gridJsonNode) {
		SeleneConifg.gridJsonNode = gridJsonNode;
	}

	public static String getWebdriverChrome() {
		return webdriverChrome;
	}
	public static String getWebdriverGecko() {
		return webdriverGecko;
	}
	public static int getPoolMaxSize() {
		return poolMaxSize;
	}
	public static int getPoolMinIdle() {
		return poolMinIdle;
	}
	public static String getGridJsonHub() {
		return gridJsonHub;
	}
	public static String getGridJsonNode() {
		return gridJsonNode;
	}
	public static boolean isUseExternal() {
		return useExternal;
	}

}
