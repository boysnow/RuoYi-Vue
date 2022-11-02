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

    private static String mode;
    private static String webdriverChrome;
    private static String webdriverGecko;
    private static int poolMaxSize;
    private static int poolMinIdle;
    private static String gridJsonHub;
    private static String gridJsonNode;

	@Value("${selene.mode}")
	public static void setMode(String mode) {
		SeleneConifg.mode = mode;
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

    public enum Mode {
    	invalid("0"), embed("1"), standalone("2");
        public String value;
    	Mode(String value) {
    		this.value = value;
    	}

    }
    public static String getMode() {
		return mode;
	}
    public static boolean isInvalidMode() {
    	if (Mode.invalid.value.equals(mode)) {
    		return true;
    	}
    	return false;
    }
    public static boolean isEmbedMode() {
    	if (Mode.embed.value.equals(mode)) {
    		return true;
    	}
    	return false;
    }
    public static boolean isStandaloneMode() {
    	if (Mode.standalone.value.equals(mode)) {
    		return true;
    	}
    	return false;
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

}
