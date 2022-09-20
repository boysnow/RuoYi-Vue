package com.ruoyi.aucper.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品入札履歴对象 T_BID_HIST
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public class TBidHist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 履歴日時 */
    private Date histDatetime;

    /** 商品コード */
    private String productCode;

    /** 商品タイトル */
    @Excel(name = "商品タイトル")
    private String productTitle;

    /** カテゴリ */
    @Excel(name = "カテゴリ")
    private String category;

    /** 現在価格 */
    @Excel(name = "現在価格")
    private BigDecimal nowPrice;

    /** 保留価格 */
    private BigDecimal onholdPrice;

    /** 入札開始日時 */
    private Date bidStartDate;

    /** 入札終了日時 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入札終了日時", width = 30, dateFormat = "yyyy-MM-dd")
    private Date bidEndDate;

    /** 最後入札者 */
    @Excel(name = "最後入札者")
    private String bidLastUser;

    /** 入札ユーザ１ */
    private String trusteeshipUser1;

    /** 入札ユーザ２ */
    private String trusteeshipUser2;

    /** 入札人数 */
    @Excel(name = "入札人数")
    private Integer bidUserCount;

    /** 残時間 */
    private Integer remainingTime;

    /** 残時間単位 */
    private String remainingTimeUnit;

    /** 入札状態　1：出品中、2：終了、3：取消 */
    private String bidStatus;

    /** 任務区分　0：監視、1：自動入札、2：無効 */
    private String taskKind;

    /** リアルステータス　1：Watching、2：Updating、3：Bidding */
    private String realStatus;

    public void setHistDatetime(Date histDatetime) 
    {
        this.histDatetime = histDatetime;
    }

    public Date getHistDatetime() 
    {
        return histDatetime;
    }
    public void setProductCode(String productCode) 
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }
    public void setProductTitle(String productTitle) 
    {
        this.productTitle = productTitle;
    }

    public String getProductTitle() 
    {
        return productTitle;
    }
    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }
    public void setNowPrice(BigDecimal nowPrice) 
    {
        this.nowPrice = nowPrice;
    }

    public BigDecimal getNowPrice() 
    {
        return nowPrice;
    }
    public void setOnholdPrice(BigDecimal onholdPrice) 
    {
        this.onholdPrice = onholdPrice;
    }

    public BigDecimal getOnholdPrice() 
    {
        return onholdPrice;
    }
    public void setBidStartDate(Date bidStartDate) 
    {
        this.bidStartDate = bidStartDate;
    }

    public Date getBidStartDate() 
    {
        return bidStartDate;
    }
    public void setBidEndDate(Date bidEndDate) 
    {
        this.bidEndDate = bidEndDate;
    }

    public Date getBidEndDate() 
    {
        return bidEndDate;
    }
    public void setBidLastUser(String bidLastUser) 
    {
        this.bidLastUser = bidLastUser;
    }

    public String getBidLastUser() 
    {
        return bidLastUser;
    }
    public void setTrusteeshipUser1(String trusteeshipUser1) 
    {
        this.trusteeshipUser1 = trusteeshipUser1;
    }

    public String getTrusteeshipUser1() 
    {
        return trusteeshipUser1;
    }
    public void setTrusteeshipUser2(String trusteeshipUser2) 
    {
        this.trusteeshipUser2 = trusteeshipUser2;
    }

    public String getTrusteeshipUser2() 
    {
        return trusteeshipUser2;
    }
    public void setBidUserCount(Integer bidUserCount) 
    {
        this.bidUserCount = bidUserCount;
    }

    public Integer getBidUserCount() 
    {
        return bidUserCount;
    }
    public void setRemainingTime(Integer remainingTime) 
    {
        this.remainingTime = remainingTime;
    }

    public Integer getRemainingTime() 
    {
        return remainingTime;
    }
    public void setRemainingTimeUnit(String remainingTimeUnit) 
    {
        this.remainingTimeUnit = remainingTimeUnit;
    }

    public String getRemainingTimeUnit() 
    {
        return remainingTimeUnit;
    }
    public void setBidStatus(String bidStatus) 
    {
        this.bidStatus = bidStatus;
    }

    public String getBidStatus() 
    {
        return bidStatus;
    }
    public void setTaskKind(String taskKind) 
    {
        this.taskKind = taskKind;
    }

    public String getTaskKind() 
    {
        return taskKind;
    }
    public void setRealStatus(String realStatus) 
    {
        this.realStatus = realStatus;
    }

    public String getRealStatus() 
    {
        return realStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("histDatetime", getHistDatetime())
            .append("productCode", getProductCode())
            .append("productTitle", getProductTitle())
            .append("category", getCategory())
            .append("nowPrice", getNowPrice())
            .append("onholdPrice", getOnholdPrice())
            .append("bidStartDate", getBidStartDate())
            .append("bidEndDate", getBidEndDate())
            .append("bidLastUser", getBidLastUser())
            .append("trusteeshipUser1", getTrusteeshipUser1())
            .append("trusteeshipUser2", getTrusteeshipUser2())
            .append("bidUserCount", getBidUserCount())
            .append("remainingTime", getRemainingTime())
            .append("remainingTimeUnit", getRemainingTimeUnit())
            .append("bidStatus", getBidStatus())
            .append("taskKind", getTaskKind())
            .append("realStatus", getRealStatus())
            .append("remark", getRemark())
            .append("deleteFlag", getDeleteFlag())
            .append("updateCount", getUpdateCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
