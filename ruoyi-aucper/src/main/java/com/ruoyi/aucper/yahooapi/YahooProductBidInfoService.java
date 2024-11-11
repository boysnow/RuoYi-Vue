package com.ruoyi.aucper.yahooapi;

import static com.codeborne.selenide.Selenide.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SelenideWait;
import com.codeborne.selenide.WebDriverRunner;
import com.ruoyi.aucper.config.YahooAuctionConifg;
import com.ruoyi.aucper.constant.BidStatus;
import com.ruoyi.aucper.constant.RealStatus;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.domain.TYahooAccount;
import com.ruoyi.aucper.mapper.TProductBidInfoMapper;
import com.ruoyi.aucper.mapper.TYahooAccountMapper;
import com.ruoyi.aucper.yahooapi.dto.ExhibitInfoDTO;
import com.ruoyi.selene.config.SeleneConifg;

@Service
public class YahooProductBidInfoService {

    private static final Logger logger = LoggerFactory.getLogger(YahooProductBidInfoService.class);

    @Autowired
    private YahooAuctionConifg config;

    @Autowired
    private TProductBidInfoMapper tProductBidInfoMapper;
    @Autowired
    private TYahooAccountMapper tYahooAccountMapper;
    @Autowired
    private StatusService statusService;
    @Autowired
    private YahooBidService yahooBidService;

    @Autowired
    private PlatformTransactionManager txManager;

	// 開始・終了時間
	private static final String FMT_DATE_JSON = "yyyy-MM-dd HH:mm:ss";
	private static final String FMT_DATE_HTML = "yyyy.MM.dd（E）HH:mm";

	private static NumberFormat format = DecimalFormat.getInstance(Locale.JAPAN);

    @Autowired
    @Qualifier("poolTargetSourceWebDriver")
    private CommonsPool2TargetSource poolTargetSourceWebDriver;

    @Async("webDriverTaskExecutor")
	public void getExhibitInfoById(String id) {

		DefaultTransactionDefinition def = null;
		TransactionStatus status = null;

    	WebDriver webDriver = null;
    	try {
    		// Seleneが無効の場合、何もしない
    		if (SeleneConifg.isInvalidMode()) {
    			return;
    		}

    		def = new DefaultTransactionDefinition();
    		status = txManager.getTransaction(def);

    		webDriver = (WebDriver) poolTargetSourceWebDriver.getTarget();
    		WebDriverRunner.setWebDriver(webDriver);

    		/**
    		 * 上記でプールからWebDriverオブジェクトを取得する為に時間が要する為
    		 * 開始ログや更新処理などをここ以降に実装する
    		 */
    		logger.info("@@@@@@@@ Get exhibit info by [" + id + "] - START");
    		logger.info("### use webdriver:{}", webDriver.toString());

        	statusService.updateRealStatus(id, RealStatus.Updating);

//    		String baseurl = "https://page.auctions.yahoo.co.jp/jp/auction/";

    		open(config.getBaseUrl() + id);
//    		open(baseurl + id);

    		ExhibitInfoDTO exhibitInfoDTO = new ExhibitInfoDTO();
    		// 商品（オークション）のID
    		exhibitInfoDTO.setAuctionID(id);


    		boolean existJson = true;

    		try {

    			new SelenideWait(webDriver, 1000, 200).until(ExpectedConditions.javaScriptThrowsNoExceptions("return pageData;"));
        		Map<String, Map<String, String>> pageData = executeJavaScript("return pageData;");

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
//    			String isClosed = items.get("isClosed");
//    			exhibitInfoDTO.setStatus(BidStatus.open.value);
//    			if (BidStatus.closed.value.equals(isClosed)) {
//    				exhibitInfoDTO.setStatus(BidStatus.closed.value);
//    			}

    		} catch (Exception e) {
    			logger.warn("Failed to get bidinfo by json pageData.", e);
    			existJson = false;
    		}


    		// JSONより取得できなかった場合、スクレイピングする
    		if (!existJson) {
    			// 商品（オークション）のタイトル
    			exhibitInfoDTO.setTitle($(".ProductTitle__text").text());
    			// 現在価格
    			BigDecimal price = BigDecimal.ZERO;
    			try {
    				price = org.springframework.util.NumberUtils.parseNumber($(".Price__value").text().trim(), BigDecimal.class, format);
    			} catch (Exception e) {
    				logger.debug(e.getMessage(), e);
    			}
    			exhibitInfoDTO.setPrice(price);

    			// 現在の入札数
    			System.out.println("get bids before");
    			String bids = $(".Count__count .Count__number").should(Condition.appear, Duration.ofMillis(1000)).text();
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
    					logger.debug(e.getMessage(), e);
    				}
    			}
    			// 出品終了日時
    			String endDate = elements.get(2).getText().replaceAll("：", "");
    			if (StringUtils.isNotEmpty(endDate)) {
    				try {
    					exhibitInfoDTO.setEndDate(DateUtils.parseDate(endDate, FMT_DATE_HTML));
    				} catch (ParseException e) {
    					logger.debug(e.getMessage(), e);
    				}
    			}
    		}
			// 状態
			exhibitInfoDTO.setStatus(BidStatus.open.value);
			try {
				String statusStr = $(".Count__count .Count__endDate").parent().getText();
				if (StringUtils.isNotEmpty(statusStr) && statusStr.startsWith(BidStatus.closed.text)) {
					exhibitInfoDTO.setStatus(BidStatus.closed.value);
				}
			} catch (Throwable th) {
				logger.debug(th.getMessage(), th);
			}

    		// Shopの場合、消費税を取得
			BigDecimal tax = BigDecimal.ZERO;
			try {
				String taxStr = $(".Price__tax").text().trim();
				taxStr = taxStr.replaceAll("（|税込|税|）", "");
				tax = org.springframework.util.NumberUtils.parseNumber(taxStr.trim(), BigDecimal.class, format);
			} catch (Exception e) {
				logger.debug(e.getMessage(), e);
			}
			if (BigDecimal.ZERO.compareTo(tax) != 0) {
				exhibitInfoDTO.setPrice(tax);
			}


    		// 最高額入札者のID
    		boolean bidderResult = this.getBidder(exhibitInfoDTO);
    		// 取得できなかった場合、ログインしてから再取得
    		if (!bidderResult) {
    			logger.info("ログインして最高額入札者を再取得する[" + id + "]");
    			this.login(config.getAccountId(), config.getPassword());
        		open(config.getBaseUrl() + id);
        		this.getBidder(exhibitInfoDTO);
    		}
    		if (config.isDebug()) {
        		String sysTime = com.ruoyi.common.utils.DateUtils.dateTimeNow();
        		Selenide.screenshot(String.format("%s_%s", sysTime, id));
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

    		// 処理成功の場合、後続非同期の自動落札処理でデータ取得する為、一旦コミット
    		this.updateTProductBidInfo(exhibitInfoDTO);
    		txManager.commit(status);

    		// 非同期の自動落札
    		yahooBidService.runAutoBiding(id);

    		logger.info("@@@@@@@@ Get exhibit info by [" + id + "] - END");

    	} catch (Exception e1) {
    		txManager.rollback(status);
    		logger.error("Failed to get exhibit info.[" + id + "]", e1);
    	} finally {
            try {
            	if (webDriver != null) poolTargetSourceWebDriver.releaseTarget(webDriver);
			} catch (Exception e) {
				logger.error("Failed to release web driver.", e);
			}
		}
	}

    private boolean getBidder(ExhibitInfoDTO exhibitInfoDTO) {

		String keyText = "最高額入札者";
		if (BidStatus.closed.value.equals(exhibitInfoDTO.getStatus())) {
			keyText = "落札者";
		}

		SelenideElement element = $x("//ul[contains(@class, 'ProductDetail__items--secondary')]/li/dl/dt[text()='" + keyText + "']");
		if (element.exists()) {
			SelenideElement bidderElment = element.parent().find("dd.ProductDetail__description");
			if (StringUtils.isEmpty(bidderElment.getText())) {
				return true;
			}
			if (bidderElment.getText().contains("なし")) {
				return true;
			}
			if (bidderElment.getText().contains("ログインして確認")) {
				// 未ログインの為、再ログインするよう「false」を返す
				return false;
			}

			// 通常の場合、「：c*e*f*** / 評価 26459」
			// 新規の場合、「：c*e*f*** / 新規」
			String regex = "^：(.+) / (評価|新規) ?(\\d*)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(bidderElment.getText());
			if(matcher.find()) {
				exhibitInfoDTO.setHighestBiddersBidderId(matcher.group(1));
				String point = matcher.group(3);
				if (StringUtils.isNotEmpty(point)) {
					exhibitInfoDTO.setHighestBiddersBidderRatingPoint(NumberUtils.toInt(point));
				}
				return true;
	        }
		}
		return true;
    }

	private void updateTProductBidInfo(ExhibitInfoDTO exhibitInfoDTO) {

		// 存在チェック
		TProductBidInfo bidInfo = tProductBidInfoMapper.selectTProductBidInfoByProductCode(exhibitInfoDTO.getAuctionID());
		boolean existFlag = true;
		if (bidInfo == null) {
			existFlag = false;
		}
		// 必要な項目のみ更新するため、その対象のみ設定する
		bidInfo = new TProductBidInfo();

		bidInfo.setProductCode(exhibitInfoDTO.getAuctionID());
		bidInfo.setProductTitle(exhibitInfoDTO.getTitle());
		bidInfo.setNowPrice(exhibitInfoDTO.getPrice());
		bidInfo.setBidStartDate(exhibitInfoDTO.getStartDate());
		bidInfo.setBidEndDate(exhibitInfoDTO.getEndDate());
		bidInfo.setBidLastUser(exhibitInfoDTO.getHighestBiddersBidderId());
		bidInfo.setBidUserCount(exhibitInfoDTO.getBids());
		bidInfo.setBidStatus(exhibitInfoDTO.getStatus());
		bidInfo.setRealStatus("");
		if (BidStatus.open.value.equals(bidInfo.getBidStatus())) {
			bidInfo.setRealStatus(RealStatus.Watching.value);
		}

		Date nowTime = com.ruoyi.common.utils.DateUtils.getNowDate();
		if (existFlag) {
			bidInfo.setUpdateTime(nowTime);
			tProductBidInfoMapper.updateTProductBidInfo(bidInfo);
		} else {
			bidInfo.setCreateTime(nowTime);
			bidInfo.setUpdateTime(nowTime);
			tProductBidInfoMapper.insertTProductBidInfo(bidInfo);
		}
	}

	/**
	 *
	 * @param userid
	 * @param password
	 * @return 0:ログイン成功 1:パスワード認証 2:コード認証 9:ログイン失敗
	 */
	public String login(String userid, String password) {

		Selenide.open(config.getLoginUrl());
		String sysTime = com.ruoyi.common.utils.DateUtils.dateTimeNow();

		/*
		 * idWrap -> userid
		 * pwdWrap -> password
		 * codeWrap -> sms code
		 * submitWrap -> login button
		 *   btnSubmit -> do login
		 *   btnCodeSend -> send sms code
		 */

		if (config.isDebug()) {
			Selenide.screenshot(sysTime + "_01_ログイン-初期");
		}

		if (Selenide.$("#idWrap").isDisplayed()) {
			if (config.isDebug()) {
				Selenide.screenshot(sysTime + "_02_ログイン-ユーザID入力前");
			}
			Selenide.$(By.id("username")).val(userid);
			if (config.isDebug()) {
				Selenide.screenshot(sysTime + "_03_ログイン-ユーザID入力後");
			}
			Selenide.$(By.id("btnNext")).click();

			// ボタン押下処理の待機
			$("#submitWrap").shouldBe(Condition.visible);
		}

		if (config.isDebug()) {
			Selenide.screenshot(sysTime + "_04_ログイン-ユーザID入力後ボタン押下");
		}
		// PW認証
		if ($("#pwdWrap").isDisplayed()) {

			if (config.isDebug()) {
				Selenide.screenshot(sysTime + "_05_ログイン-PW入力前");
			}
			$("#passwd").val(password);
			if (config.isDebug()) {
				Selenide.screenshot(sysTime + "_06_ログイン-PW入力後");
			}
			Selenide.$(By.id("btnSubmit")).click();
			return "0";
		}
		// コード認証（コード入力）
		else if ($("#codeWrap").isDisplayed()) {

			return "2";

		}
		// コード認証（コード送信）
		else if ($("#btnCodeSend").isDisplayed()) {
			if (config.isDebug()) {
				Selenide.screenshot(sysTime + "_07_ログイン-SMS送信前");
			}
			$("#btnCodeSend").click();
			if (config.isDebug()) {
				Selenide.screenshot(sysTime + "_08_ログイン-SMS送信後");
			}
			return "2";
		} else {

			return "9";

		}

	}

	/**
	 *
	 * @param smscd
	 * @return 0:ログイン成功 1:パスワード認証 2:コード認証 9:ログイン失敗
	 */
	public String loginSMS(String smscd) {

		// 認証コード
		Selenide.$(By.id("code")).sendKeys(smscd);
		Selenide.$(By.id("btnSubmit")).click();

		return "0";
	}

	public String initLogin(WebDriver webDriver, int wdIndex) {

		logger.info("initialize yahoo login. [index=" + wdIndex + "] - START");

		WebDriverRunner.setWebDriver(webDriver);

		TYahooAccount cond = new TYahooAccount();
		cond.setDeleteFlag(false);
		List<TYahooAccount> list = tYahooAccountMapper.selectTYahooAccountList(cond);
		if (list.size() == 0) {
			return "9";
		}

		int index = wdIndex % list.size();
		TYahooAccount account = list.get(index);
//		String rel = this.login("aucper336", "makendev336");
		String rel = this.login(account.getYahooAccountId(), account.getPassword());

		logger.info("initialize yahoo login. [index=" + wdIndex + "] - END");

		return rel;
	}



}