package com.ruoyi.aucper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:yahoo_auction.conf")
public class YahooAuctionConifg {

	@Value("${base.url}")
    private String baseUrl;



	public String getBaseUrl() {
		return this.baseUrl;
	}
}
