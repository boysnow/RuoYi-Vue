package com.ruoyi.aucper.controller;

import java.util.Date;
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

import com.ruoyi.aucper.domain.TBidHist;
import com.ruoyi.aucper.service.ITBidHistService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 商品入札履歴Controller
 *
 * @author ruoyi
 * @date 2022-09-04
 */
@RestController
@RequestMapping("/aucper/bidhist")
public class TBidHistController extends BaseController
{
    @Autowired
    private ITBidHistService tBidHistService;

    /**
     * 查询商品入札履歴列表
     */
    @PreAuthorize("@ss.hasPermi('aucper:bidhist:list')")
    @GetMapping("/list")
    public TableDataInfo list(TBidHist tBidHist)
    {
        startPage();
        List<TBidHist> list = tBidHistService.selectTBidHistList(tBidHist);
        return getDataTable(list);
    }

    /**
     * 导出商品入札履歴列表
     */
    @PreAuthorize("@ss.hasPermi('aucper:bidhist:export')")
    @Log(title = "商品入札履歴", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TBidHist tBidHist)
    {
        List<TBidHist> list = tBidHistService.selectTBidHistList(tBidHist);
        ExcelUtil<TBidHist> util = new ExcelUtil<TBidHist>(TBidHist.class);
        util.exportExcel(response, list, "商品入札履歴数据");
    }

    /**
     * 获取商品入札履歴详细信息
     */
    @PreAuthorize("@ss.hasPermi('aucper:bidhist:query')")
    @GetMapping(value = "/{histDatetime}")
    public AjaxResult getInfo(@PathVariable("histDatetime") Date histDatetime)
    {
        return AjaxResult.success(tBidHistService.selectTBidHistByHistDatetime(histDatetime));
    }

    /**
     * 新增商品入札履歴
     */
    @PreAuthorize("@ss.hasPermi('aucper:bidhist:add')")
    @Log(title = "商品入札履歴", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TBidHist tBidHist)
    {
        return toAjax(tBidHistService.insertTBidHist(tBidHist));
    }

    /**
     * 修改商品入札履歴
     */
    @PreAuthorize("@ss.hasPermi('aucper:bidhist:edit')")
    @Log(title = "商品入札履歴", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TBidHist tBidHist)
    {
        return toAjax(tBidHistService.updateTBidHist(tBidHist));
    }

    /**
     * 删除商品入札履歴
     */
    @PreAuthorize("@ss.hasPermi('aucper:bidhist:remove')")
    @Log(title = "商品入札履歴", businessType = BusinessType.DELETE)
	@DeleteMapping("/{histDatetimes}")
    public AjaxResult remove(@PathVariable Date[] histDatetimes)
    {
        return toAjax(tBidHistService.deleteTBidHistByHistDatetimes(histDatetimes));
    }
}
