package com.ruoyi.aucper.constant;

public enum RemainingTimeUnit {

	/**
		1	２分以内終了もの		BID_END_DATE <= (sysdate() + INTERVAL 2 MINUTE)
		2	１０分以内終了もの		BID_END_DATE <= (sysdate() + INTERVAL 10 MINUTE)
		3	１時間以内終了もの		BID_END_DATE <= (sysdate() + INTERVAL 1 HOUR)
		4	２４時間以内終了もの	BID_END_DATE <= (sysdate() + INTERVAL 24 HOUR)
		5	１日以上終了もの		BID_END_DATE >= (sysdate() + INTERVAL 1 DAY)
	 */
	V1("1", 2, "MINUTE"),
	V2("2", 10, "MINUTE"),
	V3("3", 1, "HOUR"),
	V4("4", 24, "HOUR"),
	V5("5", 1, "DAY");

	RemainingTimeUnit(String val, Integer remainingTime, String remainingTimeUnit) {
		this.val = val;
		this.remainingTime = remainingTime;
		this.remainingTimeUnit = remainingTimeUnit;
	}

	public String val;
    public Integer remainingTime;
    public String remainingTimeUnit;

    public static RemainingTimeUnit getByVal(String val) {
    	for (RemainingTimeUnit unit : RemainingTimeUnit.values()) {
    		if (unit.val.equals(val)) {
    			return unit;
    		}
    	}
    	return null;
    }
}
