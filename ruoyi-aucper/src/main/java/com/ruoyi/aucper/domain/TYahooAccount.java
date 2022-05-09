package com.ruoyi.aucper.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * YAHOOアカウント情報对象 T_YAHOO_ACCOUNT
 * 
 * @author ruoyi
 * @date 2022-05-10
 */
public class TYahooAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** YAHOOアカウントID */
    @Excel(name = "YAHOOアカウントID")
    private String yahooAccountId;

    /** パスワード */
    @Excel(name = "パスワード")
    private String password;

    /** 匿名 */
    @Excel(name = "匿名")
    private String anonymousName;

    /** 優先度 */
    @Excel(name = "優先度")
    private Integer priority;

    /** 会話回数 */
    @Excel(name = "会話回数")
    private Integer conversationCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setYahooAccountId(String yahooAccountId) 
    {
        this.yahooAccountId = yahooAccountId;
    }

    public String getYahooAccountId() 
    {
        return yahooAccountId;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setAnonymousName(String anonymousName) 
    {
        this.anonymousName = anonymousName;
    }

    public String getAnonymousName() 
    {
        return anonymousName;
    }
    public void setPriority(Integer priority) 
    {
        this.priority = priority;
    }

    public Integer getPriority() 
    {
        return priority;
    }
    public void setConversationCount(Integer conversationCount) 
    {
        this.conversationCount = conversationCount;
    }

    public Integer getConversationCount() 
    {
        return conversationCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("yahooAccountId", getYahooAccountId())
            .append("password", getPassword())
            .append("anonymousName", getAnonymousName())
            .append("priority", getPriority())
            .append("conversationCount", getConversationCount())
            .append("deleteFlag", getDeleteFlag())
            .append("updateCount", getUpdateCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
