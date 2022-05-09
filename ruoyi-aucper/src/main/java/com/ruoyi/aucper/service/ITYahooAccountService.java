package com.ruoyi.aucper.service;

import java.util.List;
import com.ruoyi.aucper.domain.TYahooAccount;

/**
 * YAHOOアカウント情報Service接口
 * 
 * @author ruoyi
 * @date 2022-05-10
 */
public interface ITYahooAccountService 
{
    /**
     * 查询YAHOOアカウント情報
     * 
     * @param id YAHOOアカウント情報主键
     * @return YAHOOアカウント情報
     */
    public TYahooAccount selectTYahooAccountById(Long id);

    /**
     * 查询YAHOOアカウント情報列表
     * 
     * @param tYahooAccount YAHOOアカウント情報
     * @return YAHOOアカウント情報集合
     */
    public List<TYahooAccount> selectTYahooAccountList(TYahooAccount tYahooAccount);

    /**
     * 新增YAHOOアカウント情報
     * 
     * @param tYahooAccount YAHOOアカウント情報
     * @return 结果
     */
    public int insertTYahooAccount(TYahooAccount tYahooAccount);

    /**
     * 修改YAHOOアカウント情報
     * 
     * @param tYahooAccount YAHOOアカウント情報
     * @return 结果
     */
    public int updateTYahooAccount(TYahooAccount tYahooAccount);

    /**
     * 批量删除YAHOOアカウント情報
     * 
     * @param ids 需要删除的YAHOOアカウント情報主键集合
     * @return 结果
     */
    public int deleteTYahooAccountByIds(Long[] ids);

    /**
     * 删除YAHOOアカウント情報信息
     * 
     * @param id YAHOOアカウント情報主键
     * @return 结果
     */
    public int deleteTYahooAccountById(Long id);
}
