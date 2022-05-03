package com.ruoyi.selene.pool;

import java.io.File;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.shared.GridNodeServer;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.util.ResourceUtils;

import com.ruoyi.selene.config.SeleneConifg;

public class WebDriverObjectPool extends CommonsPool2TargetSource {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverObjectPool.class);

    public void initializeObjects() throws Exception {

        initializeGridAndNode();

        Thread.sleep(3000);
        for (int i = 0; i < this.getMinIdle(); i++) {
            this.releaseTarget(this.getTarget());
        }
    }

    private void initializeGridAndNode() throws Exception {
        System.setProperty("webdriver.chrome.driver", SeleneConifg.getChromeDriverPath());
        System.setProperty("webdriver.gecko.driver", SeleneConifg.getGeckoDriverPath());

        //  HUB Configuration - org.openqa.grid.internal.utils.configuration.GridHubConfiguration
        File hubJson = ResourceUtils.getFile("classpath:gridHub.json");
        GridHubConfiguration gridHubConfig = GridHubConfiguration.loadFromJSON( hubJson.toString() );

        Hub hub = new Hub(gridHubConfig);
        hub.start();

        // initialize node
        // NODE Configuration - org.openqa.selenium.remote.server.SeleniumServer
        File nodeJson = ResourceUtils.getFile("classpath:registerNode.json");
        GridNodeConfiguration gridNodeConfiguration = GridNodeConfiguration.loadFromJSON( nodeJson.toString() );
        RegistrationRequest request = new RegistrationRequest( gridNodeConfiguration );
        GridNodeServer node = new SeleniumServer( request.getConfiguration() );

        SelfRegisteringRemote remote = new SelfRegisteringRemote( request );
        remote.setRemoteServer( node );
        remote.startRemoteServer();
        remote.startRegistrationProcess();

        logger.info("initialization grid completed");

    }

}
