package com.ruoyi.aucper.constant;

public enum BidStatus {

    open("1", "出品中"), closed("2", "終了"), cancelled("3", "取り消し");
    public String value;
    public String text;
    BidStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }
    public static String getText(String value) {
        for (BidStatus status : BidStatus.values()) {
            if (status.value.equals(value)) {
                return status.text;
            }
        }
        return null;
    }
    public static String getValue(String name) {
        for (BidStatus status : BidStatus.values()) {
            if (status.name().equals(name)) {
                return status.value;
            }
        }
        return null;
    }
}
