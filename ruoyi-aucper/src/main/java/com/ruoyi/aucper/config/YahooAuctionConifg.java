package com.ruoyi.aucper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("yahoo")
@ConfigurationProperties("yahoo.auction")
public class YahooAuctionConifg {

    private String baseUrl;

    private String loginUrl;

	public String getBaseUrl() {
		return this.baseUrl;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
}
