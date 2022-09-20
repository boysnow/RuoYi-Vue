package com.ruoyi.aucper.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.aucper.domain.TBidHist;

/**
 * 商品入札履歴Service接口
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public interface ITBidHistService
{
    /**
     * 查询商品入札履歴
     *
     * @param histDatetime 商品入札履歴主键
     * @return 商品入札履歴
     */
    public TBidHist selectTBidHistByHistDatetime(Date histDatetime);

    /**
     * 查询商品入札履歴列表
     *
     * @param tBidHist 商品入札履歴
     * @return 商品入札履歴集合
     */
    public List<TBidHist> selectTBidHistList(TBidHist tBidHist);

    /**
     * 新增商品入札履歴
     *
     * @param tBidHist 商品入札履歴
     * @return 结果
     */
    public int insertTBidHist(TBidHist tBidHist);

    /**
     * 修改商品入札履歴
     *
     * @param tBidHist 商品入札履歴
     * @return 结果
     */
    public int updateTBidHist(TBidHist tBidHist);

    /**
     * 批量删除商品入札履歴
     *
     * @param histDatetimes 需要删除的商品入札履歴主键集合
     * @return 结果
     */
    public int deleteTBidHistByHistDatetimes(Date[] histDatetimes);

    /**
     * 删除商品入札履歴信息
     *
     * @param histDatetime 商品入札履歴主键
     * @return 结果
     */
    public int deleteTBidHistByHistDatetime(Date histDatetime);

    /**
     * 商品入札履歴を入札情報から作成する
     *
     * @param productCodes 需要删除的商品入札情報主键集合
     * @return 结果
     */
    public int insertTBidHistFromBid(String[] productCodes);
}
