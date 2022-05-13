package com.ruoyi.aucper.yahooapi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.aucper.constant.BidStatus;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.service.ITProductBidInfoService;

@Service
public class YahooAPI {

    private static final Logger logger = LoggerFactory.getLogger(YahooAPI.class);

    @Autowired
    private YahooAPIService yahooAPIService;
    @Autowired
    private ITProductBidInfoService tProductBidInfoService;

	public void getAllProduct() {

		logger.info("getAllProduct begin.");

		TProductBidInfo cond = new TProductBidInfo();
		cond.setDeleteFlag(false);
		cond.setBidStatus(BidStatus.closed.value);
		List<TProductBidInfo> list = tProductBidInfoService.selectOpeningBidList(cond);
		if (list.size() == 0) {
			return;
		}

		for (TProductBidInfo info : list) {
			yahooAPIService.getExhibitInfoById(info.getProductCode());
			logger.info("add queue [{}]", info.getProductCode());
		}

		logger.info("getAllProduct end.");
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
