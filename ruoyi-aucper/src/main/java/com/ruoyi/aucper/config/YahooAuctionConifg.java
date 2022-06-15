package com.ruoyi.aucper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("yahoo")
@ConfigurationProperties("yahoo")
public class YahooAuctionConifg {

	@Value("${yahoo.auction.baseUrl}")
    private String baseUrl;

	@Value("${yahoo.auction.loginUrl}")
    private String loginUrl;

	@Value("${yahoo.login.accountId}")
	private String accountId;

	@Value("${yahoo.login.password}")
	private String password;

	@Value("${yahoo.debug}")
	private boolean debug;

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
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
