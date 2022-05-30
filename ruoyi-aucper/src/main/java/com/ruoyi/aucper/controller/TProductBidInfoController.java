package com.ruoyi.aucper.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.ruoyi.aucper.config.YahooAuctionConifg;
import com.ruoyi.aucper.constant.RealStatus;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.service.ITProductBidInfoService;
import com.ruoyi.aucper.yahooapi.StatusService;
import com.ruoyi.aucper.yahooapi.YahooAPIService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 商品入札情報Controller
 *
 * @author ruoyi
 * @date 2022-05-06
 */
@RestController
@RequestMapping("/aucper/bid")
public class TProductBidInfoController extends BaseController
{
    @Autowired
    private ITProductBidInfoService tProductBidInfoService;

    @Autowired
    private YahooAuctionConifg config;

    @Autowired
    private YahooAPIService yahooAPIService;

    @Autowired
    private StatusService statusService;

    /**
     * 查询商品入札情報列表
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProductBidInfo tProductBidInfo)
    {
//        startPage();
        tProductBidInfo.setDeleteFlag(false);
        List<TProductBidInfo> list = tProductBidInfoService.selectWatchingBidList(tProductBidInfo);
        return getDataTable(list);
    }

    /**
     * 导出商品入札情報列表
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:export')")
    @Log(title = "商品入札情報", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TProductBidInfo tProductBidInfo)
    {
        List<TProductBidInfo> list = tProductBidInfoService.selectTProductBidInfoList(tProductBidInfo);
        ExcelUtil<TProductBidInfo> util = new ExcelUtil<TProductBidInfo>(TProductBidInfo.class);
        util.exportExcel(response, list, "商品入札情報数据");
    }

    /**
     * 获取商品入札情報详细信息
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:query')")
    @GetMapping(value = "/{productCode}")
    public AjaxResult getInfo(@PathVariable("productCode") String productCode)
    {
        return AjaxResult.success(tProductBidInfoService.selectTProductBidInfoByProductCode(productCode));
    }

    /**
     * 新增商品入札情報
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:add')")
    @Log(title = "商品入札情報", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProductBidInfo tProductBidInfo)
    {
		// 存在チェック
		TProductBidInfo bidInfo = tProductBidInfoService.selectTProductBidInfoByProductCode(tProductBidInfo.getProductCode());
		if (bidInfo != null) {
            return AjaxResult.error("既に登録済みです");
		}
        return toAjax(tProductBidInfoService.insertTProductBidInfo(tProductBidInfo));
    }

    /**
     * 修改商品入札情報
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:edit')")
    @Log(title = "商品入札情報", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TProductBidInfo tProductBidInfo)
    {
    	// 更新対象項目の再設定
    	TProductBidInfo info = new TProductBidInfo();
    	info.setProductCode(tProductBidInfo.getProductCode());
    	info.setTaskKind(tProductBidInfo.getTaskKind());
    	info.setOnholdPrice(tProductBidInfo.getOnholdPrice());
    	info.setRemark(tProductBidInfo.getRemark());
    	info.setTrusteeshipUser1(tProductBidInfo.getTrusteeshipUser1());
    	info.setTrusteeshipUser2(tProductBidInfo.getTrusteeshipUser2());
        return toAjax(tProductBidInfoService.updateTProductBidInfo(info));
    }

    /**
     * 删除商品入札情報
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:remove')")
    @Log(title = "商品入札情報", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productCodes}")
    public AjaxResult remove(@PathVariable String[] productCodes)
    {
        return toAjax(tProductBidInfoService.deleteTProductBidInfoByProductCodes(productCodes));
    }

    /**
     * 入札関連の設定情報
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:query')")
    @GetMapping(value = "/config")
    public AjaxResult getConfigInfo() {
    	Map<String, String> data = new HashMap<>();
    	data.put("baseUrl", config.getBaseUrl());
        return AjaxResult.success(data);
    }

    /**
     * 商品入札情報の強制更新
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:edit')")
    @Log(title = "商品入札情報の最新情報取得", businessType = BusinessType.UPDATE)
    @PutMapping("/{productCodes}/force")
    public AjaxResult forcedUpdateBidInfo(@PathVariable String[] productCodes)
    {
    	for (String code : productCodes) {
    		statusService.updateRealStatus(code, RealStatus.Updating);
        	yahooAPIService.getExhibitInfoById(code);
    	}
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('aucper:bid:query')")
    @PostMapping(value = "/login")
    public AjaxResult doLogin() {
    	System.out.println("login..................................");


    	Selenide.open("https://login.yahoo.co.jp/config/login");

    	Selenide.screenshot("1001.jpg");

		SelenideElement username = Selenide.$(By.id("username"));
		System.out.println(username.isDisplayed());
		if (username.isDisplayed()) {
			username.val("boysnow336");
			Selenide.$(By.id("btnNext")).click();
	    	Selenide.screenshot("1002.jpg");
		}

		System.out.println("pwdWrap isDisplayed:" + Selenide.$(By.id("pwdWrap")).isDisplayed());
		System.out.println("pwdWrap Height:" + Selenide.$(By.id("pwdWrap")).getSize().getHeight());

		if (Selenide.$(By.id("pwdWrap")).isDisplayed()) {

			Selenide.$(By.id("passwd")).val("mj210213");
	    	Selenide.screenshot("1003.jpg");
//			Selenide.$(By.id("btnSubmit")).click();
		} else if (Selenide.$(By.id("codeWrap")).isDisplayed()) {
			// 認証コード
			Selenide.$(By.id("code")).sendKeys("確認コード入力");
	    	Selenide.screenshot("1004.jpg");
//			Selenide.$(By.id("btnSubmit")).click();
		} else {

			Selenide.$(By.id("btnCodeSend")).click();
		}


    	Selenide.screenshot("1005.jpg");



        return AjaxResult.success();
    }
}
