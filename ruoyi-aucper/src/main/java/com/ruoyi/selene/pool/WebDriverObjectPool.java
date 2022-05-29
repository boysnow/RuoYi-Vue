package com.ruoyi.selene.pool;

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

        logger.info("initialization grid begin.");

        if (SeleneConifg.isUseExternal()) {
            System.setProperty(SeleneConifg.Keys.CHROME, SeleneConifg.getWebdriverChrome());
        }
        if (SeleneConifg.isUseExternal()) {
            System.setProperty(SeleneConifg.Keys.GECKO, SeleneConifg.getWebdriverGecko());
        }

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

        logger.info("initialization grid completed.");

    }

}
