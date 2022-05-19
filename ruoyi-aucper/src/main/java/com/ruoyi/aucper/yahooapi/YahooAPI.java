package com.ruoyi.aucper.yahooapi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.aucper.constant.BidStatus;
import com.ruoyi.aucper.constant.RemainingTimeUnit;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.service.ITProductBidInfoService;
import com.ruoyi.common.exception.job.TaskException;

@Service
public class YahooAPI {

    private static final Logger logger = LoggerFactory.getLogger(YahooAPI.class);

    @Autowired
    private YahooAPIService yahooAPIService;
    @Autowired
    private ITProductBidInfoService tProductBidInfoService;

	public void updateBidInfo(String params) throws TaskException {

		logger.info("update bid info begin.");

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

	public static void main(String args[]) {
		YahooAPIService service = new YahooAPIService();
		System.out.println("begin");
//		service.getExhibitInfoById("h1030142899");
//		service.getExhibitInfoById("f1031551276");
		service.getExhibitInfoById("c1048299050");
		System.out.println("end");
	}

}
