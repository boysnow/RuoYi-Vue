package com.ruoyi.aucper.yahooapi;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.ruoyi.aucper.config.YahooAuctionConifg;
import com.ruoyi.aucper.constant.BidStatus;
import com.ruoyi.aucper.constant.RealStatus;
import com.ruoyi.aucper.constant.TaskKind;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.domain.TYahooAccount;
import com.ruoyi.aucper.mapper.TProductBidInfoMapper;
import com.ruoyi.aucper.mapper.TYahooAccountMapper;
import com.ruoyi.selene.config.SeleneConifg;

@Service
public class YahooBidService {

    private static final Logger logger = LoggerFactory.getLogger(YahooBidService.class);

    @Autowired
    private TProductBidInfoMapper tProductBidInfoMapper;
    @Autowired
    private TYahooAccountMapper tYahooAccountMapper;
    @Autowired
    private StatusService statusService;
    @Autowired
    @Qualifier("poolTargetSourceWebDriver")
    private CommonsPool2TargetSource poolTargetSourceWebDriver;
    @Autowired
    private YahooAuctionConifg config;

	/**
	 * 自動入札
	 * @param productCode
	 * @param nowPrice
	 * @param onholdPrice
	 */
	@Async
	public void runAutoBiding(String productCode) {

		// Seleneが無効の場合、何もしない
		if (SeleneConifg.isInvalidMode()) {
			return;
		}

		logger.info("run auto bidding begin.");

		TProductBidInfo bidInfo = tProductBidInfoMapper.selectTProductBidInfoByProductCode(productCode);

		// 保留価格が未入力の場合、対象外とする
		if (bidInfo.getOnholdPrice() == null || bidInfo.getOnholdPrice().compareTo(BigDecimal.ZERO) <= 0) {
			logger.info("run auto bidding end.[{}]", "NotHoldPrice");
			return;
		}
		// 現在金額チェック
		if (bidInfo.getOnholdPrice().compareTo(bidInfo.getNowPrice()) <= 0) {
			logger.info("run auto bidding end.[{}]", "OnholdPriceOver");
			return;
		}
		// 状態≠出品中の場合、対象外とする
		if (!BidStatus.open.value.equals(bidInfo.getBidStatus())) {
			logger.info("run auto bidding end.[{}]", "NotOpen");
			return;
		}
		// 自動入札でない場合、対象外とする
		if (!TaskKind.AutoBid.value.equals(bidInfo.getTaskKind())) {
			logger.info("run auto bidding end.[{}]", "NotAutoBidding");
			return;
		}
//		// 残時間単位が「MINUTE」以外の場合、何もしない
//		if (!RemainingTimeUnit.V2.remainingTimeUnit.equals(productBidInfo.getRemainingTimeUnit())) {
//			logger.info("run auto bidding end.[{}]", "NotTime(minute unit");
//			return;
//		}
//		// 残時間 5 MINUTE 以上ものを対象外とする
//		if (productBidInfo.getRemainingTime() > 5) {
//			logger.info("run auto bidding end.[{}]", "NotTime(<5 minute)");
//			return;
//		}
		// 落札者が本人の場合、何もしない
		TYahooAccount cond = new TYahooAccount();
		cond.setAnonymousName(bidInfo.getBidLastUser());
		List<TYahooAccount> list = tYahooAccountMapper.selectTYahooAccountList(cond);
		if (list.size() > 0) {
			logger.info("run auto bidding end.[{}]", "AlreadyBid");
			return;
		}

		// real status -> biding
		statusService.updateRealStatus(productCode, RealStatus.Bidding);

    	WebDriver webDriver = null;
    	try {

    		webDriver = (WebDriver) poolTargetSourceWebDriver.getTarget();
    		WebDriverRunner.setWebDriver(webDriver);

    		Selenide.open(config.getBaseUrl() + productCode);

    		// bid the product
    		Selenide.$(".bds_btn").shouldBe(Condition.visible).click();
    		Selenide.$(".BidModal__buttonArea .js-validator-submit").shouldBe(Condition.visible).click();
    		Selenide.$(".SubmitBox__button--bid").shouldBe(Condition.visible).click();


    	} catch (Exception e1) {
    		logger.error("Failed to bid.[" + productCode + "]", e1);
    	} finally {
            try {
				poolTargetSourceWebDriver.releaseTarget(webDriver);
			} catch (Exception e) {
				logger.error("Failed to release web driver.", e);
			}
		}

    	// リアルステータス更新
		TProductBidInfo updateInfo = new TProductBidInfo();
		updateInfo.setProductCode(productCode);
		updateInfo.setRealStatus(RealStatus.Watching.value);
		tProductBidInfoMapper.updateTProductBidInfo(updateInfo);

		logger.info("run auto bidding end.");

	}

}
