package com.ruoyi.aucper.yahooapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ruoyi.aucper.constant.RealStatus;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.mapper.TProductBidInfoMapper;

@Service
public class StatusService {

    @Autowired
    private TProductBidInfoMapper tProductBidInfoMapper;

    @Async
	public void updateRealStatus(String productCode, RealStatus realStatus) {

		TProductBidInfo updateInfo = new TProductBidInfo();
		updateInfo.setProductCode(productCode);
		updateInfo.setRealStatus(realStatus.value);
		tProductBidInfoMapper.updateTProductBidInfo(updateInfo);

    }
}
