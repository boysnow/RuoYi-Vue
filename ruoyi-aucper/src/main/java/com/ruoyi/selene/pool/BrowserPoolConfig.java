package com.ruoyi.selene.pool;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BrowserPoolConfig {

    @Bean(name = "webDriver")
    @Scope("prototype")
    public WebDriver webDriver() throws MalformedURLException {
        URL remoteURL = new URL(String.format("http://%s:%d/wd/hub", "127.0.0.1", 4444) );
        System.out.println("Remote URL : "+remoteURL);
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        WebDriver driver = new RemoteWebDriver(remoteURL, capabilities);

        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
//        chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);

        WebDriver driver = new RemoteWebDriver(remoteURL, chromeOptions);

        return driver;
    }

    @Bean(name = "poolTargetSourceWebDriver", initMethod = "initializeObjects")
    WebDriverObjectPool makeWebDriverPool() {
        WebDriverObjectPool pool = new WebDriverObjectPool();
        pool.setMaxSize(5);
        pool.setTargetClass(WebDriver.class);
        pool.setMinIdle(5);
        pool.setTargetBeanName("webDriver");
        return pool;
    }

    @Bean
    ProxyFactoryBean proxyFactoryBean() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTargetSource(makeWebDriverPool());
        return factoryBean;
    }
}