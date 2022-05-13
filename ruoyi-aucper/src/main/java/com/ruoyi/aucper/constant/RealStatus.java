package com.ruoyi.aucper.constant;

public enum RealStatus {

	Watching("1", "監視中"), Updating("2", "更新中"), Bidding("3", "入札中");
    public String value;
    public String text;
    RealStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }
}
