package com.ruoyi.aucper.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aucper.mapper.TProductBidInfoMapper;
import com.ruoyi.aucper.domain.TProductBidInfo;
import com.ruoyi.aucper.service.ITProductBidInfoService;

/**
 * 商品入札情報Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-05
 */
@Service
public class TProductBidInfoServiceImpl implements ITProductBidInfoService 
{
    @Autowired
    private TProductBidInfoMapper tProductBidInfoMapper;

    /**
     * 查询商品入札情報
     * 
     * @param id 商品入札情報主键
     * @return 商品入札情報
     */
    @Override
    public TProductBidInfo selectTProductBidInfoById(Integer id)
    {
        return tProductBidInfoMapper.selectTProductBidInfoById(id);
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
        return tProductBidInfoMapper.updateTProductBidInfo(tProductBidInfo);
    }

    /**
     * 批量删除商品入札情報
     * 
     * @param ids 需要删除的商品入札情報主键
     * @return 结果
     */
    @Override
    public int deleteTProductBidInfoByIds(Integer[] ids)
    {
        return tProductBidInfoMapper.deleteTProductBidInfoByIds(ids);
    }

    /**
     * 删除商品入札情報信息
     * 
     * @param id 商品入札情報主键
     * @return 结果
     */
    @Override
    public int deleteTProductBidInfoById(Integer id)
    {
        return tProductBidInfoMapper.deleteTProductBidInfoById(id);
    }
}
