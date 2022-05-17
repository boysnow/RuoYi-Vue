package com.ruoyi.aucper.yahooapi;

import static com.codeborne.selenide.Selenide.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.ruoyi.aucper.config.YahooAuctionConifg;
import com.ruoyi.aucper.constant.BidStatus;
import com.ruoyi.aucper.constant.RealStatus;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.mapper.TProductBidInfoMapper;
import com.ruoyi.aucper.yahooapi.dto.ExhibitInfoDTO;

@Service
public class YahooAPIService {

    private static final Logger logger = LoggerFactory.getLogger(YahooAPIService.class);

	static {
		System.setProperty("selenide.headless", "true");
	}

    @Autowired
    private YahooAuctionConifg config;

    @Autowired
    private TProductBidInfoMapper tProductBidInfoMapper;
    @Autowired
    private StatusService statusService;

	// 開始・終了時間
	private static final String FMT_DATE_JSON = "yyyy-MM-dd HH:mm:ss";
	private static final String FMT_DATE_HTML = "yyyy.MM.dd（E）HH:mm";

    @Autowired
    @Qualifier("poolTargetSourceWebDriver")
    private CommonsPool2TargetSource poolTargetSourceWebDriver;

    @Async("webDriverTaskExecutor")
	public void getExhibitInfoById(String id) {

    	WebDriver webDriver = null;
    	try {
    		webDriver = (WebDriver) poolTargetSourceWebDriver.getTarget();
    		WebDriverRunner.setWebDriver(webDriver);

    		/**
    		 * 上記でプールからWebDriverオブジェクトを取得する為に時間が要する為
    		 * 開始ログや更新処理などをここ以降に実装する
    		 */
    		logger.info("@@@@@@@@ Get exhibit info by [" + id + "] - START");

        	statusService.updateRealStatus(id, RealStatus.Updating);

//    		String baseurl = "https://page.auctions.yahoo.co.jp/jp/auction/";

    		open(config.getBaseUrl() + id);
//    		open(baseurl + id);

    		ExhibitInfoDTO exhibitInfoDTO = new ExhibitInfoDTO();
    		// 商品（オークション）のID
    		exhibitInfoDTO.setAuctionID(id);


    		boolean existJson = true;
    		Map<String, Map<String, String>> pageData = executeJavaScript("return pageData;");

    		try {
    			Map<String, String> items = pageData.get("items");

    			// 商品（オークション）のタイトル
    			exhibitInfoDTO.setTitle(items.get("productName"));
    			// 現在価格
    			BigDecimal price = BigDecimal.ZERO;
    			try {
    				price = NumberUtils.toScaledBigDecimal(items.get("price"));
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			exhibitInfoDTO.setPrice(price);
    			// 現在の入札数
    			exhibitInfoDTO.setBids(NumberUtils.toInt(items.get("bids")));
    			// 出品開始日時
    			String starttime = items.get("starttime");
    			if (StringUtils.isNotEmpty(starttime)) {
    				exhibitInfoDTO.setEndDate(DateUtils.parseDate(starttime, FMT_DATE_JSON));
    			}
    			// 出品終了日時
    			String endDate = items.get("endtime");
    			if (StringUtils.isNotEmpty(endDate)) {
    				exhibitInfoDTO.setEndDate(DateUtils.parseDate(endDate, FMT_DATE_JSON));
    			}
    			// 状態
    			String isClosed = items.get("isClosed");
    			exhibitInfoDTO.setStatus(BidStatus.open.value);
    			if ("1".equals(isClosed)) {
    				exhibitInfoDTO.setStatus(BidStatus.closed.value);
    			}

    		} catch (Exception e) {
    			logger.warn("Failed to get bidinfo by json pageData.", e);
    			existJson = false;
    		}


    		// JSONより取得できなかった場合、スクレイピングする
    		existJson = false;
    		if (!existJson) {
    			// 商品（オークション）のタイトル
    			exhibitInfoDTO.setTitle($(".ProductTitle__text").text());
    			// 現在価格
    			BigDecimal price = BigDecimal.ZERO;
    			try {
    				NumberFormat format = DecimalFormat.getInstance(Locale.JAPAN);
    				price = org.springframework.util.NumberUtils.parseNumber($(".Price__value").text().trim(), BigDecimal.class, format);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			exhibitInfoDTO.setPrice(price);

    			// 現在の入札数
    			String bids = $(".Count__count .Count__number").text();
    			System.out.println("before=" + bids);
    			bids = bids.replaceAll("[^0-9](.|\n)*$", "");
    			System.out.println("after=" + bids);
    			exhibitInfoDTO.setBids(NumberUtils.toInt(bids));

    			ElementsCollection elements = $(".ProductDetail__items--primary").$$(".ProductDetail__description");
    			// 出品開始日時
    			String starttime = elements.get(1).getText().replaceAll("：", "");
    			if (StringUtils.isNotEmpty(starttime)) {
    				try {
    					exhibitInfoDTO.setStartDate(DateUtils.parseDate(starttime, FMT_DATE_HTML));
    				} catch (ParseException e) {
    					e.printStackTrace();
    				}
    			}
    			// 出品終了日時
    			String endDate = elements.get(2).getText().replaceAll("：", "");
    			if (StringUtils.isNotEmpty(endDate)) {
    				try {
    					exhibitInfoDTO.setEndDate(DateUtils.parseDate(endDate, FMT_DATE_HTML));
    				} catch (ParseException e) {
    					e.printStackTrace();
    				}
    			}
    			// 状態
    			String statusStr = $(".Count__count--sideLine .Count__number").getText();
    			exhibitInfoDTO.setStatus(BidStatus.open.value);
    			if (StringUtils.isNotEmpty(statusStr) && statusStr.startsWith(BidStatus.closed.text)) {
    				exhibitInfoDTO.setStatus(BidStatus.closed.value);
    			}


    		}

    		// 最高額入札者のID
    		this.getBidder(exhibitInfoDTO);
    		// 取得できなかった場合、ログインしてから再取得
    		if (StringUtils.isEmpty(exhibitInfoDTO.getHighestBiddersBidderId())) {
    			logger.info("ログインして最高額入札者を再取得する[" + id + "]");
    			this.login();
        		open(config.getBaseUrl() + id);
        		this.getBidder(exhibitInfoDTO);
    		}

//    		// 出品者のID
//    		exhibitInfoDTO.setSellerId($(".Seller__name a").text());
//    		// 出品者の評価ポイント
//    		Integer ratingPoint = 0;
//    		try {
//    			ratingPoint = NumberUtils.toInt($(".Seller__ratingSum a").text().trim());
//    		} catch (Exception e) {
//    		}
//    		exhibitInfoDTO.setSellerRatingPoint(ratingPoint);


    		// WebDriverの使用終わった後クローズ
    		//Selenide.closeWebDriver();



    		System.out.println("---------------------");
    		System.out.println(exhibitInfoDTO.toString());


    		this.updateTProductBidInfo(exhibitInfoDTO);

    		logger.info("@@@@@@@@ Get exhibit info by [" + id + "] - END");

    	} catch (Exception e1) {
    		logger.error("Failed to get get exhibit info.[" + id + "]", e1);
    	} finally {
            try {
				poolTargetSourceWebDriver.releaseTarget(webDriver);
			} catch (Exception e) {
				logger.error("Failed to release web driver.", e);
			}
		}
	}

    private void getBidder(ExhibitInfoDTO exhibitInfoDTO) {

		String keyText = "最高額入札者";
		if (BidStatus.closed.value.equals(exhibitInfoDTO.getStatus())) {
			keyText = "落札者";
		}

		SelenideElement element = $x("//ul[contains(@class, 'ProductDetail__items--secondary')]/li/dl/dt[text()='" + keyText + "']");
		if (element.exists()) {
			SelenideElement bidderElment = element.parent().find("dd.ProductDetail__description");
			if (bidderElment.getText() != null && bidderElment.getText().contains("なし")) {
				return;
			}
			// 「：c*e*f*** / 評価 26459」
			String regex = "^：(.+) / 評価 (\\d+)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(bidderElment.getText());
			if(matcher.find()) {
				exhibitInfoDTO.setHighestBiddersBidderId(matcher.group(1));
				String point = matcher.group(2);
				if (StringUtils.isNotEmpty(point)) {
					exhibitInfoDTO.setHighestBiddersBidderRatingPoint(NumberUtils.toInt(point));
				}
	        }
		}
    }

	private void updateTProductBidInfo(ExhibitInfoDTO exhibitInfoDTO) {

		// 存在チェック
		TProductBidInfo bidInfo = tProductBidInfoMapper.selectTProductBidInfoByProductCode(exhibitInfoDTO.getAuctionID());
		boolean existFlag = true;
		if (bidInfo == null) {
			bidInfo = new TProductBidInfo();
			existFlag = false;
		}

		bidInfo.setProductCode(exhibitInfoDTO.getAuctionID());
		bidInfo.setProductTitle(exhibitInfoDTO.getTitle());
		bidInfo.setNowPrice(exhibitInfoDTO.getPrice());
		bidInfo.setOnholdPrice(exhibitInfoDTO.getInsideBidorbuy());
		bidInfo.setBidStartDate(exhibitInfoDTO.getStartDate());
		bidInfo.setBidEndDate(exhibitInfoDTO.getEndDate());
		bidInfo.setBidLastUser(exhibitInfoDTO.getHighestBiddersBidderId());
		bidInfo.setTrusteeshipUser1(null);
		bidInfo.setTrusteeshipUser2(null);
		bidInfo.setBidUserCount(exhibitInfoDTO.getBids());
		bidInfo.setBidStatus(exhibitInfoDTO.getStatus());
		bidInfo.setRealStatus(RealStatus.Watching.value);

		if (existFlag) {
			bidInfo.setUpdateTime(com.ruoyi.common.utils.DateUtils.getNowDate());
			tProductBidInfoMapper.updateTProductBidInfo(bidInfo);
		} else {
			Date nowTime = com.ruoyi.common.utils.DateUtils.getNowDate();
			bidInfo.setCreateTime(nowTime);
			bidInfo.setUpdateTime(nowTime);
			tProductBidInfoMapper.insertTProductBidInfo(bidInfo);
		}
	}

	public void login() {

		Selenide.open(config.getLoginUrl());

		SelenideElement username = Selenide.$(By.id("username"));
		System.out.println("@@@@@@@@@@@@@@@@@@@@ 22");
		System.out.println(username.isDisplayed());
		if (username.isDisplayed()) {
			username.val("buyee05");
			Selenide.$(By.id("btnNext")).click();
		}

		Selenide.$(By.id("passwd")).val("a8621jp");
		Selenide.$(By.id("btnSubmit")).click();

	}

}
