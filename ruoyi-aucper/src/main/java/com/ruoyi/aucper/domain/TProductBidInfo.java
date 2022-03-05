package com.ruoyi.aucper.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品入札情報对象 T_PRODUCT_BID_INFO
 * 
 * @author ruoyi
 * @date 2022-03-05
 */
public class TProductBidInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 商品コード */
    @Excel(name = "商品コード")
    private String productCode;

    /** 商品タイトル */
    @Excel(name = "商品タイトル")
    private String productTitle;

    /** 現在価格 */
    @Excel(name = "現在価格")
    private Long nowPrice;

    /** 保留価格 */
    @Excel(name = "保留価格")
    private Long onholdPrice;

    /** 入札カウントダウン */
    private Integer bidCountdown;

    /** 入札開始日時 */
    private Date bidStartDate;

    /** 入札終了日時 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入札終了日時", width = 30, dateFormat = "yyyy-MM-dd")
    private Date bidEndDate;

    /** 最後入札者 */
    @Excel(name = "最後入札者")
    private String bidLastUser;

    /** 托管ユーザ１ */
    @Excel(name = "托管ユーザ１")
    private String trusteeshipUser1;

    /** 托管ユーザ２ */
    @Excel(name = "托管ユーザ２")
    private String trusteeshipUser2;

    /** 入札人数 */
    @Excel(name = "入札人数")
    private Integer bidUserCount;

    /** 残時間 */
    @Excel(name = "残時間")
    private Long remainingTime;

    /** 入札停止任務 */
    private String bidClosingTask;

    /** 価格任務 */
    private String bidPriceTask;

    /** 入札任務 */
    @Excel(name = "入札任務")
    private String bidTask;

    /** 入札状態 */
    @Excel(name = "入札状態")
    private String bidStatus;

    /** 任務状態 */
    @Excel(name = "任務状態")
    private String taskStatus;

    /** 残時間単位 */
    @Excel(name = "残時間単位")
    private String remainingTimeUnit;

    /** リアルステータス */
    @Excel(name = "リアルステータス")
    private String realTimeStatus;

    /** 論理削除フラグ */
    private Integer deleteFlag;

    /** 更新回数 */
    private Long updateCount;

    /** 登録ユーザ */
    private String createUser;

    /** 登録日時 */
    private Date createDatetime;

    /** 更新ユーザ */
    private String updateUser;

    /** 更新日時 */
    private Date updateDatetime;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
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
    public void setNowPrice(Long nowPrice) 
    {
        this.nowPrice = nowPrice;
    }

    public Long getNowPrice() 
    {
        return nowPrice;
    }
    public void setOnholdPrice(Long onholdPrice) 
    {
        this.onholdPrice = onholdPrice;
    }

    public Long getOnholdPrice() 
    {
        return onholdPrice;
    }
    public void setBidCountdown(Integer bidCountdown) 
    {
        this.bidCountdown = bidCountdown;
    }

    public Integer getBidCountdown() 
    {
        return bidCountdown;
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
    public void setRemainingTime(Long remainingTime) 
    {
        this.remainingTime = remainingTime;
    }

    public Long getRemainingTime() 
    {
        return remainingTime;
    }
    public void setBidClosingTask(String bidClosingTask) 
    {
        this.bidClosingTask = bidClosingTask;
    }

    public String getBidClosingTask() 
    {
        return bidClosingTask;
    }
    public void setBidPriceTask(String bidPriceTask) 
    {
        this.bidPriceTask = bidPriceTask;
    }

    public String getBidPriceTask() 
    {
        return bidPriceTask;
    }
    public void setBidTask(String bidTask) 
    {
        this.bidTask = bidTask;
    }

    public String getBidTask() 
    {
        return bidTask;
    }
    public void setBidStatus(String bidStatus) 
    {
        this.bidStatus = bidStatus;
    }

    public String getBidStatus() 
    {
        return bidStatus;
    }
    public void setTaskStatus(String taskStatus) 
    {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() 
    {
        return taskStatus;
    }
    public void setRemainingTimeUnit(String remainingTimeUnit) 
    {
        this.remainingTimeUnit = remainingTimeUnit;
    }

    public String getRemainingTimeUnit() 
    {
        return remainingTimeUnit;
    }
    public void setRealTimeStatus(String realTimeStatus) 
    {
        this.realTimeStatus = realTimeStatus;
    }

    public String getRealTimeStatus() 
    {
        return realTimeStatus;
    }
    public void setDeleteFlag(Integer deleteFlag) 
    {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() 
    {
        return deleteFlag;
    }
    public void setUpdateCount(Long updateCount) 
    {
        this.updateCount = updateCount;
    }

    public Long getUpdateCount() 
    {
        return updateCount;
    }
    public void setCreateUser(String createUser) 
    {
        this.createUser = createUser;
    }

    public String getCreateUser() 
    {
        return createUser;
    }
    public void setCreateDatetime(Date createDatetime) 
    {
        this.createDatetime = createDatetime;
    }

    public Date getCreateDatetime() 
    {
        return createDatetime;
    }
    public void setUpdateUser(String updateUser) 
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser() 
    {
        return updateUser;
    }
    public void setUpdateDatetime(Date updateDatetime) 
    {
        this.updateDatetime = updateDatetime;
    }

    public Date getUpdateDatetime() 
    {
        return updateDatetime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productCode", getProductCode())
            .append("productTitle", getProductTitle())
            .append("nowPrice", getNowPrice())
            .append("onholdPrice", getOnholdPrice())
            .append("bidCountdown", getBidCountdown())
            .append("bidStartDate", getBidStartDate())
            .append("bidEndDate", getBidEndDate())
            .append("bidLastUser", getBidLastUser())
            .append("trusteeshipUser1", getTrusteeshipUser1())
            .append("trusteeshipUser2", getTrusteeshipUser2())
            .append("bidUserCount", getBidUserCount())
            .append("remainingTime", getRemainingTime())
            .append("bidClosingTask", getBidClosingTask())
            .append("bidPriceTask", getBidPriceTask())
            .append("bidTask", getBidTask())
            .append("bidStatus", getBidStatus())
            .append("taskStatus", getTaskStatus())
            .append("remainingTimeUnit", getRemainingTimeUnit())
            .append("realTimeStatus", getRealTimeStatus())
            .append("deleteFlag", getDeleteFlag())
            .append("updateCount", getUpdateCount())
            .append("createUser", getCreateUser())
            .append("createDatetime", getCreateDatetime())
            .append("updateUser", getUpdateUser())
            .append("updateDatetime", getUpdateDatetime())
            .toString();
    }
}
