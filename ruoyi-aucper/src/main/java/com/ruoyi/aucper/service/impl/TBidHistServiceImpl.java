package com.ruoyi.aucper.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.aucper.domain.TBidHist;
import com.ruoyi.aucper.mapper.TBidHistMapper;
import com.ruoyi.aucper.service.ITBidHistService;
import com.ruoyi.common.utils.DateUtils;

/**
 * 商品入札履歴Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-04
 */
@Service
public class TBidHistServiceImpl implements ITBidHistService
{
    @Autowired
    private TBidHistMapper tBidHistMapper;

    /**
     * 查询商品入札履歴
     *
     * @param histDatetime 商品入札履歴主键
     * @return 商品入札履歴
     */
    @Override
    public TBidHist selectTBidHistByHistDatetime(Date histDatetime)
    {
        return tBidHistMapper.selectTBidHistByHistDatetime(histDatetime);
    }

    /**
     * 查询商品入札履歴列表
     *
     * @param tBidHist 商品入札履歴
     * @return 商品入札履歴
     */
    @Override
    public List<TBidHist> selectTBidHistList(TBidHist tBidHist)
    {
        return tBidHistMapper.selectTBidHistList(tBidHist);
    }

    /**
     * 新增商品入札履歴
     *
     * @param tBidHist 商品入札履歴
     * @return 结果
     */
    @Override
    public int insertTBidHist(TBidHist tBidHist)
    {
        tBidHist.setCreateTime(DateUtils.getNowDate());
        return tBidHistMapper.insertTBidHist(tBidHist);
    }

    /**
     * 修改商品入札履歴
     *
     * @param tBidHist 商品入札履歴
     * @return 结果
     */
    @Override
    public int updateTBidHist(TBidHist tBidHist)
    {
        tBidHist.setUpdateTime(DateUtils.getNowDate());
        return tBidHistMapper.updateTBidHist(tBidHist);
    }

    /**
     * 批量删除商品入札履歴
     *
     * @param histDatetimes 需要删除的商品入札履歴主键
     * @return 结果
     */
    @Override
    public int deleteTBidHistByHistDatetimes(Date[] histDatetimes)
    {
        return tBidHistMapper.deleteTBidHistByHistDatetimes(histDatetimes);
    }

    /**
     * 删除商品入札履歴信息
     *
     * @param histDatetime 商品入札履歴主键
     * @return 结果
     */
    @Override
    public int deleteTBidHistByHistDatetime(Date histDatetime)
    {
        return tBidHistMapper.deleteTBidHistByHistDatetime(histDatetime);
    }

    /**
     * 商品入札履歴を入札情報から作成する
     *
     * @param productCodes 需要删除的商品入札情報主键集合
     * @return 结果
     */
    @Override
    public int insertTBidHistFromBid(String[] productCodes)
    {
        return tBidHistMapper.insertTBidHistFromBid(productCodes);
    }
}
