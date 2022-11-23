package com.ruoyi.aucper.yahooapi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ruoyi.aucper.constant.BidStatus;
import com.ruoyi.aucper.constant.RemainingTimeUnit;
import com.ruoyi.aucper.constant.TaskKind;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.domain.TYahooAccount;
import com.ruoyi.aucper.mapper.TYahooAccountMapper;
import com.ruoyi.aucper.service.ITProductBidInfoService;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.selene.config.SeleneConifg;

@Service
public class YahooAPI {

    private static final Logger logger = LoggerFactory.getLogger(YahooAPI.class);

    @Autowired
    private YahooProductBidInfoService yahooAPIService;
    @Autowired
    private ITProductBidInfoService tProductBidInfoService;
    @Autowired
    private TYahooAccountMapper tYahooAccountMapper;

    /**
     * 入札情報取得（定期）
     * @param params 1:20秒単位-2分以内
     *                2:1分単位-10分以内
     *                3:5分単位-1時間以内
     *                4:30分単位-24時間以内
     *                5:1時間単位-1日以上
     * @throws TaskException
     */
	public void updateBidInfo(String params) throws TaskException {

		// Seleneが無効の場合、何もしない
		if (SeleneConifg.isInvalidMode()) {
			return;
		}

		logger.info("update bid info begin.[{}]", params);

		// 実行Noにより実行単位を取得
		RemainingTimeUnit unit = RemainingTimeUnit.getByVal(params);

		TProductBidInfo cond = new TProductBidInfo();
		cond.setDeleteFlag(false);
		cond.setBidStatus(BidStatus.closed.value);
		if (unit != null) {
			cond.setRemainingTime(unit.remainingTime);
			cond.setRemainingTimeUnit(unit.remainingTimeUnit);
		}
		List<TProductBidInfo> list = tProductBidInfoService.selectOpeningBidList(cond);
		if (list.size() > 0) {
			for (TProductBidInfo info : list) {
				yahooAPIService.getExhibitInfoById(info.getProductCode());
				logger.info("add queue [{}]", info.getProductCode());
			}
		}

		logger.info("update bid info end.");
	}

	@Async
	public void runAutoBiding(TProductBidInfo productBidInfo) {

		logger.info("run auto bidding begin.");

		// 状態チェック
		if (!BidStatus.open.value.equals(productBidInfo.getBidStatus())) {
			logger.info("run auto bidding end.[{}]", "NotOpen");
			return;
		}
		if (!TaskKind.AutoBid.value.equals(productBidInfo.getTaskKind())) {
			logger.info("run auto bidding end.[{}]", "NotAutoBidding");
			return;
		}
		// 残時間単位が「MINUTE」以外の場合、何もしない
		if (!RemainingTimeUnit.V2.remainingTimeUnit.equals(productBidInfo.getRemainingTimeUnit())) {
			logger.info("run auto bidding end.[{}]", "NotTime(minute unit");
			return;
		}
		// 残時間 5 MINUTE 以上ものを対象外とする
		if (productBidInfo.getRemainingTime() > 5) {
			logger.info("run auto bidding end.[{}]", "NotTime(<5 minute)");
			return;
		}
		// 落札者が本人の場合、何もしない
		TYahooAccount cond = new TYahooAccount();
		cond.setAnonymousName(productBidInfo.getBidLastUser());
		List<TYahooAccount> list = tYahooAccountMapper.selectTYahooAccountList(cond);
		if (list.size() > 0) {
			logger.info("run auto bidding end.[{}]", "AlreadyBid");
			return;
		}

		// real status -> biding




		logger.info("run auto bidding end.");

	}

	/**
	 * 自動入札（定期実施：閾値時間以内ものを自動入札する）★未使用
	 * @param threshold 閾値
	 */
	public void autoBiding(int threshold) {

		// Seleneが無効の場合、何もしない
		if (SeleneConifg.isInvalidMode()) {
			return;
		}

		logger.info("auto biding begin.[{}MINUTE]", threshold);

		// 閾値がない場合、何もしない
		if (threshold == 0) {
			logger.warn("threshold is not setting.");
			return;
		}

		TProductBidInfo cond = new TProductBidInfo();
		cond.setDeleteFlag(false);
		cond.setBidStatus(BidStatus.closed.value);
		cond.setRemainingTime(threshold);
		cond.setRemainingTimeUnit(RemainingTimeUnit.V2.remainingTimeUnit);
		List<TProductBidInfo> list = tProductBidInfoService.selectOpeningBidList(cond);
		if (list.size() > 0) {
			for (TProductBidInfo info : list) {
				yahooAPIService.getExhibitInfoById(info.getProductCode()); // TODO
				logger.info("add queue [{}]", info.getProductCode());
			}
		}

		logger.info("auto biding end.");
	}

	public static void main(String args[]) {
		YahooProductBidInfoService service = new YahooProductBidInfoService();
		System.out.println("begin");
//		service.getExhibitInfoById("h1030142899");
//		service.getExhibitInfoById("f1031551276");
		service.getExhibitInfoById("c1048299050");
		System.out.println("end");
	}

}
