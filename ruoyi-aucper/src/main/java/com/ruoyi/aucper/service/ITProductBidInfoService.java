package com.ruoyi.aucper.service;

import java.util.List;

import com.ruoyi.aucper.domain.TProductBidInfo;

/**
 * 商品入札情報Service接口
 *
 * @author ruoyi
 * @date 2022-05-06
 */
public interface ITProductBidInfoService
{
    /**
     * 查询商品入札情報
     *
     * @param id 商品入札情報主键
     * @return 商品入札情報
     */
    public TProductBidInfo selectTProductBidInfoById(Integer id);

    /**
     * 查询商品入札情報列表
     *
     * @param tProductBidInfo 商品入札情報
     * @return 商品入札情報集合
     */
    public List<TProductBidInfo> selectTProductBidInfoList(TProductBidInfo tProductBidInfo);

    /**
     * 新增商品入札情報
     *
     * @param tProductBidInfo 商品入札情報
     * @return 结果
     */
    public int insertTProductBidInfo(TProductBidInfo tProductBidInfo);

    /**
     * 修改商品入札情報
     *
     * @param tProductBidInfo 商品入札情報
     * @return 结果
     */
    public int updateTProductBidInfo(TProductBidInfo tProductBidInfo);

    /**
     * 批量删除商品入札情報
     *
     * @param ids 需要删除的商品入札情報主键集合
     * @return 结果
     */
    public int deleteTProductBidInfoByIds(Integer[] ids);

    /**
     * 删除商品入札情報信息
     *
     * @param id 商品入札情報主键
     * @return 结果
     */
    public int deleteTProductBidInfoById(Integer id);

    /**
     * 查询商品入札情報列表
     *
     * @param tProductBidInfo 商品入札情報
     * @return 商品入札情報集合
     */
    public List<TProductBidInfo> selectOpeningBidList(TProductBidInfo tProductBidInfo);
}
