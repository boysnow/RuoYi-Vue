package com.ruoyi.aucper.constant;

public enum TaskKind {

	Watch("0", "監視"), AutoBid("1", "自動入札"), Invalid("2", "無効");
    public String value;
    public String text;
    TaskKind(String value, String text) {
        this.value = value;
        this.text = text;
    }
}
