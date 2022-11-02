package com.ruoyi.selene.pool;

import java.util.ArrayList;
import java.util.List;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.shared.GridNodeServer;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.CommonsPool2TargetSource;

import com.ruoyi.aucper.yahooapi.YahooAPIService;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.selene.config.SeleneConifg;

public class WebDriverObjectPool extends CommonsPool2TargetSource {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverObjectPool.class);

    public void initializeObjects() throws Exception {

    	// Selene使用可否チェック
    	if (SeleneConifg.isInvalidMode()) {
    		return;
    	}
        initializeGridAndNode();

        // 初期化に時間が要する為、少し待機
        Thread.sleep(5000);

        YahooAPIService yahooAPIService = SpringUtils.getBean(YahooAPIService.class);

        // min idle 作成
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < this.getMinIdle(); i++) {
        	WebDriver webDriver = (WebDriver) this.getTarget();
        	yahooAPIService.initLogin(webDriver, i);
        	list.add(webDriver);
    		logger.info("### Initialized webdriver:{}", webDriver.toString());
        }
        for (Object o : list) {
        	this.releaseTarget(o);
        }
        list.clear();
    }

    private void initializeGridAndNode() throws Exception {

        logger.info("initialization grid begin.");

        try {

            System.setProperty(SeleneConifg.Keys.CHROME, SeleneConifg.getWebdriverChrome());
            System.setProperty(SeleneConifg.Keys.GECKO, SeleneConifg.getWebdriverGecko());

            //  HUB Configuration - org.openqa.grid.internal.utils.configuration.GridHubConfiguration
            GridHubConfiguration gridHubConfig = GridHubConfiguration.loadFromJSON(SeleneConifg.getGridJsonHub());

            Hub hub = new Hub(gridHubConfig);
            hub.start();

            // initialize node
            // NODE Configuration - org.openqa.selenium.remote.server.SeleniumServer
            GridNodeConfiguration gridNodeConfiguration = GridNodeConfiguration.loadFromJSON(SeleneConifg.getGridJsonNode());
            RegistrationRequest request = new RegistrationRequest( gridNodeConfiguration );
            GridNodeServer node = new SeleniumServer( request.getConfiguration() );

            SelfRegisteringRemote remote = new SelfRegisteringRemote( request );
            remote.setRemoteServer( node );
            remote.startRemoteServer();
            remote.startRegistrationProcess();

		} catch (Exception e) {
			logger.error("Failed to initializeGridAndNode.", e);
			throw e;
		}
        logger.info("initialization grid completed.");

    }

}
