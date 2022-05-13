package com.ruoyi.aucper.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.mapper.TProductBidInfoMapper;
import com.ruoyi.aucper.service.ITProductBidInfoService;
import com.ruoyi.common.utils.DateUtils;

/**
 * 商品入札情報Service业务层处理
 *
 * @author ruoyi
 * @date 2022-05-12
 */
@Service
public class TProductBidInfoServiceImpl implements ITProductBidInfoService
{
    @Autowired
    private TProductBidInfoMapper tProductBidInfoMapper;

    /**
     * 查询商品入札情報
     *
     * @param productCode 商品入札情報主键
     * @return 商品入札情報
     */
    @Override
    public TProductBidInfo selectTProductBidInfoByProductCode(String productCode)
    {
        return tProductBidInfoMapper.selectTProductBidInfoByProductCode(productCode);
    }

    /**
     * 查询商品入札情報列表
     *
     * @param tProductBidInfo 商品入札情報
     * @return 商品入札情報
     */
    @Override
    public List<TProductBidInfo> selectTProductBidInfoList(TProductBidInfo tProductBidInfo)
    {
        return tProductBidInfoMapper.selectTProductBidInfoList(tProductBidInfo);
    }

    /**
     * 新增商品入札情報
     *
     * @param tProductBidInfo 商品入札情報
     * @return 结果
     */
    @Override
    public int insertTProductBidInfo(TProductBidInfo tProductBidInfo)
    {
        tProductBidInfo.setCreateTime(DateUtils.getNowDate());
        return tProductBidInfoMapper.insertTProductBidInfo(tProductBidInfo);
    }

    /**
     * 修改商品入札情報
     *
     * @param tProductBidInfo 商品入札情報
     * @return 结果
     */
    @Override
    public int updateTProductBidInfo(TProductBidInfo tProductBidInfo)
    {
        tProductBidInfo.setUpdateTime(DateUtils.getNowDate());
        return tProductBidInfoMapper.updateTProductBidInfo(tProductBidInfo);
    }

    /**
     * 批量删除商品入札情報
     *
     * @param productCodes 需要删除的商品入札情報主键
     * @return 结果
     */
    @Override
    public int deleteTProductBidInfoByProductCodes(String[] productCodes)
    {
        return tProductBidInfoMapper.deleteTProductBidInfoByProductCodes(productCodes);
    }

    /**
     * 删除商品入札情報信息
     *
     * @param productCode 商品入札情報主键
     * @return 结果
     */
    @Override
    public int deleteTProductBidInfoByProductCode(String productCode)
    {
        return tProductBidInfoMapper.deleteTProductBidInfoByProductCode(productCode);
    }

    /**
     * 開催中入札情報取得
     *
     * @param tProductBidInfo 商品入札情報
     * @return 商品入札情報
     */
    @Override
    public List<TProductBidInfo> selectOpeningBidList(TProductBidInfo tProductBidInfo)
    {
        return tProductBidInfoMapper.selectOpeningBidList(tProductBidInfo);
    }

    /**
     * 入札情報取得（画面監視用）
     *
     * @param tProductBidInfo 商品入札情報
     * @return 商品入札情報
     */
    @Override
    public List<TProductBidInfo> selectWatchingBidList(TProductBidInfo tProductBidInfo)
    {
        return tProductBidInfoMapper.selectWatchingBidList(tProductBidInfo);
    }
}
