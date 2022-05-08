package com.ruoyi.aucper.yahooapi;

import static com.codeborne.selenide.Selenide.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.ruoyi.aucper.config.YahooAuctionConifg;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.mapper.TProductBidInfoMapper;
import com.ruoyi.aucper.yahooapi.dto.ExhibitInfoDTO;
import com.ruoyi.aucper.yahooapi.dto.ExhibitInfoDTO.BID_STATUS;

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

	// 開始・終了時間
	private static final String FMT_DATE_JSON = "yyyy-MM-dd HH:mm:ss";
	private static final String FMT_DATE_HTML = "yyyy.MM.dd（E）HH:mm";

    @Autowired
//    @Qualifier("poolTargetSourceWebDriver")
    private CommonsPool2TargetSource poolTargetSourceWebDriver;

    @Async("webDriverTaskExecutor")
	public ExhibitInfoDTO getExhibitInfoById(String id) {

    	WebDriver webDriver = null;
    	try {
    		webDriver = (WebDriver) poolTargetSourceWebDriver.getTarget();
    		WebDriverRunner.setWebDriver(webDriver);


    		logger.info("@@@@@@@@ Get exhibit info by [" + id + "] - START");

//    		String baseurl = "https://page.auctions.yahoo.co.jp/jp/auction/";

    		open(config.getBaseUrl() + id);
//    		open(baseurl + id);

    		ExhibitInfoDTO exhibitInfoDTO = new ExhibitInfoDTO();
    		// 商品（オークション）のID
    		exhibitInfoDTO.setAuctionID(id);


    		boolean existJson = true;
    		Map<String, Map<String, String>> pageData = executeJavaScript("return pageData;");
//    		Object pageData = executeJavaScript("return pageData;");
    		System.out.println("pageData");
    		System.out.println(pageData.get("items"));

//    		try {
//    			Map<String, String> items = pageData.get("items");
    //
//    			// 商品（オークション）のタイトル
//    			exhibitInfoDTO.setTitle(items.get("productName"));
//    			// 現在価格
//    			BigDecimal price = BigDecimal.ZERO;
//    			try {
//    				price = NumberUtils.toScaledBigDecimal(items.get("price"));
//    			} catch (Exception e) {
//    				e.printStackTrace();
//    			}
//    			exhibitInfoDTO.setPrice(price);
//    			// 現在の入札数
//    			exhibitInfoDTO.setBids(NumberUtils.toInt(items.get("bids")));
//    			// 出品開始日時
//    			String starttime = items.get("starttime");
//    			if (StringUtils.isNotEmpty(starttime)) {
//    				exhibitInfoDTO.setEndDate(DateUtils.parseDate(starttime, FMT_DATE_JSON));
//    			}
//    			// 出品終了日時
//    			String endDate = items.get("endtime");
//    			if (StringUtils.isNotEmpty(endDate)) {
//    				exhibitInfoDTO.setEndDate(DateUtils.parseDate(endDate, FMT_DATE_JSON));
//    			}
//    			// 状態
//    			String isClosed = items.get("isClosed");
//    			exhibitInfoDTO.setStatus(BID_STATUS.open.value);
//    			if ("1".equals(isClosed)) {
//    				exhibitInfoDTO.setStatus(BID_STATUS.closed.value);
//    			}
    //
//    		} catch (Exception e) {
//    			System.out.println(e);
//    			e.printStackTrace();
//    			existJson = false;
//    		}



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
    			bids = bids.replaceAll("[^0-9].*$", "");
    			exhibitInfoDTO.setBids(NumberUtils.toInt(bids));

    			ElementsCollection elements = $(".ProductDetail__items--primary").$$(".ProductDetail__description");
    			// 出品開始日時
    			String starttime = elements.get(1).getText().replaceAll("：", "");
    			System.out.println("starttime=" + starttime);
    			if (StringUtils.isNotEmpty(starttime)) {
    				try {
    					exhibitInfoDTO.setStartDate(DateUtils.parseDate(starttime, FMT_DATE_HTML));
    				} catch (ParseException e) {
    					e.printStackTrace();
    				}
    			}
    			// 出品終了日時
    			String endDate = elements.get(2).getText().replaceAll("：", "");
    			System.out.println("endDate=" + endDate);
    			if (StringUtils.isNotEmpty(endDate)) {
    				try {
    					exhibitInfoDTO.setEndDate(DateUtils.parseDate(endDate, FMT_DATE_HTML));
    				} catch (ParseException e) {
    					e.printStackTrace();
    				}
    			}
    			// 状態
    			String statusStr = $(".Count__count--sideLine .Count__number").getText();
    			System.out.println("@@@@@@@@@@@@@@@@@@@@@");
    			System.out.println(statusStr);
    			exhibitInfoDTO.setStatus(BID_STATUS.open.value);
    			if (StringUtils.isNotEmpty(statusStr) && statusStr.startsWith(BID_STATUS.closed.text)) {
    				exhibitInfoDTO.setStatus(BID_STATUS.closed.value);
    			}


    		}

    		// 最高額入札者のID
    		String keyText = "最高額入札者";
    		if (BID_STATUS.closed.value.equals(exhibitInfoDTO.getStatus())) {
    			keyText = "落札者";
    		}

    		SelenideElement element = $x("//ul[contains(@class, 'ProductDetail__items--secondary')]/li/dl/dt[text()='" + keyText + "']");
    		if (element.exists()) {
    			SelenideElement bidderElment = element.parent().find("dd.ProductDetail__description");
    			// 「：c*e*f*** / 評価 26459」
    			String regex = "^：(.+) / (\\d+)$";
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

    		// 出品者のID
    		exhibitInfoDTO.setSellerId($(".Seller__name a").text());
    		// 出品者の評価ポイント
    		Integer ratingPoint = 0;
    		try {
//    			ratingPoint = NumberUtils.parseNumber($(".Seller__ratingSum a").text().trim(), Integer.class);
    		} catch (Exception e) {
    		}
    		exhibitInfoDTO.setSellerRatingPoint(ratingPoint);


//    		Selenide.closeWebDriver();




    		System.out.println("---------------------");
    		System.out.println(exhibitInfoDTO.toString());



    		this.updateTProductBidInfo(exhibitInfoDTO);

    		logger.info("@@@@@@@@ Get exhibit info by [" + id + "] - END");


    	} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
            try {
				poolTargetSourceWebDriver.releaseTarget(webDriver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void updateTProductBidInfo(ExhibitInfoDTO exhibitInfoDTO) {

		// 存在チェック
		TProductBidInfo conditions = new TProductBidInfo();
		conditions.setProductCode(exhibitInfoDTO.getAuctionID());
		List<TProductBidInfo> list = tProductBidInfoMapper.selectTProductBidInfoList(conditions);
		TProductBidInfo bidInfo = null;
		if (list.size() != 0) {
			bidInfo = list.get(0);
		} else {
			bidInfo = new TProductBidInfo();
			bidInfo.setProductCode(exhibitInfoDTO.getAuctionID());
		}

//		bidInfo.setProductCode(exhibitInfoDTO.getAuctionID());
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
		if (exhibitInfoDTO.getLeftTime() != null) {
			bidInfo.setRemainingTime(exhibitInfoDTO.getLeftTime().longValue());
		}
		bidInfo.setRemainingTimeUnit(exhibitInfoDTO.getLeftTimeUnit());

		if (bidInfo.getId() == null || bidInfo.getId() == 0) {
			bidInfo.setUpdateDatetime(com.ruoyi.common.utils.DateUtils.getNowDate());
			tProductBidInfoMapper.insertTProductBidInfo(bidInfo);
		} else {
			bidInfo.setUpdateDatetime(com.ruoyi.common.utils.DateUtils.getNowDate());
			tProductBidInfoMapper.updateTProductBidInfo(bidInfo);
		}
	}

}
