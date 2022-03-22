package com.ruoyi.aucper.yahooapi.dto;

import java.math.BigDecimal;
import java.util.Date;

import io.netty.handler.codec.string.LineSeparator;

public class ExhibitInfoDTO {

    /** 商品（オークション）のID */
    private String auctionID;

    /** 商品（オークション）のタイトル */
    private String title;

    /** 出品者のID */
    private String sellerId;

    /** 出品者の評価ポイント */
    private Integer sellerRatingPoint;

    /** 現在価格 */
    private BigDecimal price;

    /** 税 */
    private BigDecimal tax;

    /** 内部保留価格 */
    private BigDecimal insideBidorbuy;

    /** 現在の入札数 */
    private int bids;

    /** 最高額入札者のID */
    private String highestBiddersBidderId;

    /** 最高額入札者の評価ポイント */
    private Integer highestBiddersBidderRatingPoint;

    /** 出品終了日時 */
    private Date endDate;

    /** 出品終了時刻 */
    private String endTime;

    /** 残り時間 */
    private Integer leftTime;

    /** 残り時間単位 */
    private String leftTimeUnit;

    /** 商品（オークション）の状態（open ：出品中、closed ：終了、cancelled ：オークションの取り消し） */
    private String status;

    public enum BID_STATUS {
        open("1", "出品中"), closed("2", "終了"), cancelled("3", "取り消し");
        public String value;
        public String text;
        BID_STATUS(String value, String text) {
            this.value = value;
            this.text = text;
        }
        public static String getText(String value) {
            for (BID_STATUS status : BID_STATUS.values()) {
                if (status.value.equals(value)) {
                    return status.text;
                }
            }
            return null;
        }
        public static String getValue(String name) {
            for (BID_STATUS status : BID_STATUS.values()) {
                if (status.name().equals(name)) {
                    return status.value;
                }
            }
            return null;
        }
    }

    public enum REMAINING_TIME_UNIT {
        MINUTE("1", "分"), HOUR("2", "時間"), DAY("3", "日");

        public String val;
        public String text;
        public int time;

        REMAINING_TIME_UNIT(String val, String text) {
            this.val = val;
            this.text = text;
        }

        private REMAINING_TIME_UNIT setTime(int time) {
            this.time = time;
            return this;
        }

        public static String getVal(String text) {
            for (REMAINING_TIME_UNIT unit : REMAINING_TIME_UNIT.values()) {
                if (unit.text.equals(text)) {
                    return unit.val;
                }
            }
            return null;
        }
        public static String getText(String val) {
            for (REMAINING_TIME_UNIT unit : REMAINING_TIME_UNIT.values()) {
                if (unit.val.equals(val)) {
                    return unit.text;
                }
            }
            return null;
        }
        public static REMAINING_TIME_UNIT getLeftTimeAndUnit(Date startDate, Date endDate) {
            long leftTime = endDate.getTime() - startDate.getTime();

            long time = leftTime / (24 * 60 * 60 * 1000);
            if (time > 0) {
                return DAY.setTime((int)time + 1);
            }
            time = leftTime / (60 * 60 * 1000);
            if (time > 0) {
                return HOUR.setTime((int)time + 1);
            }
            time = leftTime / (60 * 1000);
            if (time > 0) {
                return MINUTE.setTime((int)time + 1);
            }
            double dtime = (double)leftTime / (60 * 1000);
            if (dtime > 0) {
                return MINUTE.setTime(1);
            }

            return MINUTE.setTime(0);
        }
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("商品ID:").append(this.auctionID).append(LineSeparator.WINDOWS.value());
        sb.append("タイトル:").append(this.title).append(LineSeparator.WINDOWS.value());
        sb.append("出品者:").append(this.sellerId).append(LineSeparator.WINDOWS.value());
        sb.append("出品者の評価:").append(this.sellerRatingPoint).append(LineSeparator.WINDOWS.value());
        sb.append("現在価格:").append(this.price).append(LineSeparator.WINDOWS.value());
        sb.append("税:").append(this.tax).append(LineSeparator.WINDOWS.value());
        sb.append("入札数:").append(this.bids).append(LineSeparator.WINDOWS.value());
        sb.append("最高額入札者ID:").append(this.highestBiddersBidderId).append(LineSeparator.WINDOWS.value());
        sb.append("最高額入札者の評価:").append(this.highestBiddersBidderRatingPoint).append(LineSeparator.WINDOWS.value());
        sb.append("開始日時:").append(this.endDate).append(LineSeparator.WINDOWS.value());
        sb.append("終了予定日時:").append(this.endDate).append(LineSeparator.WINDOWS.value());
        sb.append("残り時間:").append(this.leftTime).append(LineSeparator.WINDOWS.value());
        sb.append("残り時間単位:").append(this.leftTimeUnit).append(LineSeparator.WINDOWS.value());
        sb.append("商品状態:").append(this.status).append(LineSeparator.WINDOWS.value());

        return sb.toString();
    }

    /**
     * @return auctionID
     */
    public String getAuctionID() {
        return auctionID;
    }

    /**
     * @param auctionID セットする auctionID
     */
    public void setAuctionID(String auctionID) {
        this.auctionID = auctionID;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title セットする title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return sellerId
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * @param sellerId セットする sellerId
     */
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * @return sellerRatingPoint
     */
    public Integer getSellerRatingPoint() {
        return sellerRatingPoint;
    }

    /**
     * @param sellerRatingPoint セットする sellerRatingPoint
     */
    public void setSellerRatingPoint(Integer sellerRatingPoint) {
        this.sellerRatingPoint = sellerRatingPoint;
    }

    /**
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price セットする price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return tax
     */
    public BigDecimal getTax() {
        return tax;
    }

    /**
     * @param tax セットする tax
     */
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    /**
     * @return insideBidorbuy
     */
    public BigDecimal getInsideBidorbuy() {
        return insideBidorbuy;
    }

    /**
     * @param insideBidorbuy セットする insideBidorbuy
     */
    public void setInsideBidorbuy(BigDecimal insideBidorbuy) {
        this.insideBidorbuy = insideBidorbuy;
    }

    /**
     * @return bids
     */
    public int getBids() {
        return bids;
    }

    /**
     * @param bids セットする bids
     */
    public void setBids(int bids) {
        this.bids = bids;
    }

    /**
     * @return highestBiddersBidderId
     */
    public String getHighestBiddersBidderId() {
        return highestBiddersBidderId;
    }

    /**
     * @param highestBiddersBidderId セットする highestBiddersBidderId
     */
    public void setHighestBiddersBidderId(String highestBiddersBidderId) {
        this.highestBiddersBidderId = highestBiddersBidderId;
    }

    /**
     * @return highestBiddersBidderRatingPoint
     */
    public Integer getHighestBiddersBidderRatingPoint() {
        return highestBiddersBidderRatingPoint;
    }

    /**
     * @param highestBiddersBidderRatingPoint セットする highestBiddersBidderRatingPoint
     */
    public void setHighestBiddersBidderRatingPoint(Integer highestBiddersBidderRatingPoint) {
        this.highestBiddersBidderRatingPoint = highestBiddersBidderRatingPoint;
    }


    /**
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate セットする endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime セットする endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status セットする status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return leftTime
     */
    public Integer getLeftTime() {
        return leftTime;
    }

    /**
     * @param leftTime セットする leftTime
     */
    public void setLeftTime(Integer leftTime) {
        this.leftTime = leftTime;
    }

    /**
     * @return leftTimeUnit
     */
    public String getLeftTimeUnit() {
        return leftTimeUnit;
    }

    /**
     * @param leftTimeUnit セットする leftTimeUnit
     */
    public void setLeftTimeUnit(String leftTimeUnit) {
        this.leftTimeUnit = leftTimeUnit;
    }

}
