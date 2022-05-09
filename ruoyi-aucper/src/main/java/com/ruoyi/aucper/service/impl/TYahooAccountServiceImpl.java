package com.ruoyi.aucper.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aucper.mapper.TYahooAccountMapper;
import com.ruoyi.aucper.domain.TYahooAccount;
import com.ruoyi.aucper.service.ITYahooAccountService;

/**
 * YAHOOアカウント情報Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-05-10
 */
@Service
public class TYahooAccountServiceImpl implements ITYahooAccountService 
{
    @Autowired
    private TYahooAccountMapper tYahooAccountMapper;

    /**
     * 查询YAHOOアカウント情報
     * 
     * @param id YAHOOアカウント情報主键
     * @return YAHOOアカウント情報
     */
    @Override
    public TYahooAccount selectTYahooAccountById(Long id)
    {
        return tYahooAccountMapper.selectTYahooAccountById(id);
    }

    /**
     * 查询YAHOOアカウント情報列表
     * 
     * @param tYahooAccount YAHOOアカウント情報
     * @return YAHOOアカウント情報
     */
    @Override
    public List<TYahooAccount> selectTYahooAccountList(TYahooAccount tYahooAccount)
    {
        return tYahooAccountMapper.selectTYahooAccountList(tYahooAccount);
    }

    /**
     * 新增YAHOOアカウント情報
     * 
     * @param tYahooAccount YAHOOアカウント情報
     * @return 结果
     */
    @Override
    public int insertTYahooAccount(TYahooAccount tYahooAccount)
    {
        tYahooAccount.setCreateTime(DateUtils.getNowDate());
        return tYahooAccountMapper.insertTYahooAccount(tYahooAccount);
    }

    /**
     * 修改YAHOOアカウント情報
     * 
     * @param tYahooAccount YAHOOアカウント情報
     * @return 结果
     */
    @Override
    public int updateTYahooAccount(TYahooAccount tYahooAccount)
    {
        tYahooAccount.setUpdateTime(DateUtils.getNowDate());
        return tYahooAccountMapper.updateTYahooAccount(tYahooAccount);
    }

    /**
     * 批量删除YAHOOアカウント情報
     * 
     * @param ids 需要删除的YAHOOアカウント情報主键
     * @return 结果
     */
    @Override
    public int deleteTYahooAccountByIds(Long[] ids)
    {
        return tYahooAccountMapper.deleteTYahooAccountByIds(ids);
    }

    /**
     * 删除YAHOOアカウント情報信息
     * 
     * @param id YAHOOアカウント情報主键
     * @return 结果
     */
    @Override
    public int deleteTYahooAccountById(Long id)
    {
        return tYahooAccountMapper.deleteTYahooAccountById(id);
    }
}
