package com.ruoyi.aucper.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.service.ITProductBidInfoService;
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

    /**
     * 查询商品入札情報列表
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProductBidInfo tProductBidInfo)
    {
        startPage();
        tProductBidInfo.setDeleteFlag(false);
        List<TProductBidInfo> list = tProductBidInfoService.selectOpeningBidList(tProductBidInfo);
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
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(tProductBidInfoService.selectTProductBidInfoById(id));
    }

    /**
     * 新增商品入札情報
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:add')")
    @Log(title = "商品入札情報", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProductBidInfo tProductBidInfo)
    {
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
        return toAjax(tProductBidInfoService.updateTProductBidInfo(tProductBidInfo));
    }

    /**
     * 删除商品入札情報
     */
    @PreAuthorize("@ss.hasPermi('aucper:bid:remove')")
    @Log(title = "商品入札情報", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(tProductBidInfoService.deleteTProductBidInfoByIds(ids));
    }
}
