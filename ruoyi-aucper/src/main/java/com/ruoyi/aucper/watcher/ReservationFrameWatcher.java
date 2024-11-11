package com.ruoyi.aucper.watcher;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.Page;
import com.zjiecode.wxpusher.client.bean.Result;
import com.zjiecode.wxpusher.client.bean.WxUser;

/**
 * Watcher
 *
 * @author ruoyi
 * @date 2022-05-06
 */
@Component
public class ReservationFrameWatcher {

	private static final String APP_TOKEN = "AT_0UihKvBoi92ExbEigVWoUddrEAo9Grlq";
	// パークタワー勝どき
	private static final String URL_KACHIDOKI = "https://www.31sumai.com/attend/X1972/";
	// 晴海フラッグ
	private static final String URL_HARUMI = "https://www.31sumai.com/attend/X1604/?utm_source=straight&utm_medium=mm&utm_campaign=X1604&utm_term=limited1&utm_content=1&mkt_tok=NDI2LUJDTy03MDMAAAGEjguP8VGbWQWrGa7tpQYfYuoPiSd6q6UQWwwK-JhxrUkzhaI4ypDwyaxghOSeofclaqXJain7L4gu7Dxm05RqcUzchnJBqtqvUqgu7b6hd8GLTQRc";
	// 浜松町
	private static final String URL_HAMAMATSUCHO = "https://www.31sumai.com/attend/X1913/?utm_source=AtOnce&utm_medium=mm&utm_campaign=X1913&utm_term=20221031&utm_content=3&mkt_tok=NDI2LUJDTy03MDMAAAGHzAfq4LNWZN9wWWWSQqK0_9Yf6y2yLIptKBfvbYd3Z14eb6JPqGWSnVQk04gG0HombWVfDj8JxVArcjv91EpHxo4mE69LxNpoTS5JGtLKZYt8GwQ";
	// パークタワー大阪堂島浜
	private static final String URL_DOUJIMAHAMA = "https://www.31sumai.com/attend/K1902/4/";
	
	private static final String URL_NAKANO = "https://www.31sumai.com/attend/X1514/";
	
	private static final String URL_TOYOMI = "https://www.31sumai.com/attend/X1919/";

	
	// プラウドタワー池袋
	private static final String URL_PROUD_IKEBUKURO = "https://www.proud-web.jp/module/reservation?id=A992";
	
	
	private static final String NOT_ACCEPTED = "来場予約を受け付けておりません";
	private static final String NOT_RESERVATION = "予約枠無";

	private static final String MSG_FMT = "%s　予約可能：%s日";

    private static final Logger logger = LoggerFactory.getLogger(ReservationFrameWatcher.class);

    @Autowired
    @Qualifier("poolTargetSourceWebDriver")
    private CommonsPool2TargetSource poolTargetSourceWebDriver;

    /**
     * Watching三井不動産の予約枠
     */
    public void watch31sumai(String params, Boolean always) {

    	WebDriver webDriver = null;
    	try {
    		webDriver = (WebDriver) poolTargetSourceWebDriver.getTarget();

    		/**
    		 * 上記でプールからWebDriverオブジェクトを取得する為に時間が要する為
    		 * 開始ログや更新処理などをここ以降に実装する
    		 */
    		WebDriverRunner.setWebDriver(webDriver);
    		logger.info("@@@@@@@@ watching 31sumai - START [{}, {}]", params, always);
    		logger.info("### use webdriver:{}", webDriver.toString());

    		if (StringUtils.isEmpty(params)) {
        		logger.info("@@@@@@@@ watching 31sumai - END[not parameter]");
    			return;
    		}
        	always = always ? true : false;
    		String url = null;
        	String msg = "";
    		switch(params) {
    		case "kachidoki":
    			url = URL_KACHIDOKI;
    			msg = "【ParkTower-KACHIDOKI】";
    			break;
    		case "harumi":
    			url = URL_HARUMI;
    			msg = "【晴海】";
    			break;
    		case "hamamatsucho":
    			url = URL_HAMAMATSUCHO;
    			msg = "【浜松町】";
    			break;
    		case "doujimahama":
    			url = URL_DOUJIMAHAMA;
    			msg = "【大阪堂島浜】";
    			break;
    		case "nakano":
    			url = URL_NAKANO;
    			msg = "【中野】";
    			break;
    		case "toyomi":
    			url = URL_TOYOMI;
    			msg = "【豊海】";
    			break;
    		}
            if (StringUtils.isEmpty(url)) {
        		logger.info("@@@@@@@@ watching 31sumai - END[not supported:{}]", params);
    			return;
            }

        	Selenide.open(url);

        	WebElement header = Selenide.$(By.cssSelector("div.ui-datepicker-header"));

    		String sysTime = com.ruoyi.common.utils.DateUtils.dateTimeNow();
			
    		// 予約画面
//    		Selenide.screenshot("page" + sysTime);
			
        	if (!header.isDisplayed()) {
//        		this.sendMsg(NOT_ACCEPTED);
        		logger.info("@@@@@@@@ watching 31sumai - END[not accepted]");
    			return;
    		}

        	boolean existFlag = false;
//        	msg = msg + System.lineSeparator();
        	msg = msg + "<br/>";
        	int count = 1;
        	while (true) {

        		// 年月
            	String yearMount = header.findElement(By.cssSelector("div.ui-datepicker-title")).getText();
            	WebElement calendar = Selenide.$(By.cssSelector("table.ui-datepicker-calendar"));
//            	int ngDays = calendar.findElements(By.cssSelector("table.ui-datepicker-calendar td.status_1")).size();
            	int okDays1 = calendar.findElements(By.cssSelector("table.ui-datepicker-calendar td.status_2")).size();
            	int okDays2 = calendar.findElements(By.cssSelector("table.ui-datepicker-calendar td.status_3")).size();

            	if (okDays1 > 0 || okDays2 > 0) {
            		existFlag = true;
//                	msg = msg + String.format(MSG_FMT, yearMount, (okDays1 + okDays2)) + System.lineSeparator();
                	msg = msg + String.format(MSG_FMT, yearMount, (okDays1 + okDays2)) + "<br/>";
            	}

        		if (count >= 2) {
        			break;
        		}
            	// 次ボタン
            	header.findElement(By.cssSelector("a.ui-datepicker-next")).click();
        		count++;
        	}
        	if (!existFlag) {
//            	msg = msg + "予約枠なし" + System.lineSeparator();
            	msg = msg + NOT_RESERVATION + "<br/>";
        	}

        	if (existFlag || always) {
        		msg = msg + String.format("<a target='_blank' href='%s'>予約サイト</a>", url);
//        		msg = msg + url;
        		this.sendMsg(msg, url);
        	}


    		logger.info("@@@@@@@@ watching 31sumai - END");

    	} catch (Exception e) {
    		logger.error("Failed to watching 31sumai", e);
    	} finally {
            try {
				poolTargetSourceWebDriver.releaseTarget(webDriver);
			} catch (Exception e) {
				logger.error("Failed to release web driver.", e);
			}
		}
    }

    /**
     * Watching野村不動産Proudの予約枠
     */
    public void watchProud(String params, Boolean always) {

    	WebDriver webDriver = null;
    	try {
    		webDriver = (WebDriver) poolTargetSourceWebDriver.getTarget();

    		/**
    		 * 上記でプールからWebDriverオブジェクトを取得する為に時間が要する為
    		 * 開始ログや更新処理などをここ以降に実装する
    		 */
    		WebDriverRunner.setWebDriver(webDriver);
    		logger.info("@@@@@@@@ watching Proud - START [{}, {}]", params, always);
    		logger.info("### use webdriver:{}", webDriver.toString());

    		if (StringUtils.isEmpty(params)) {
        		logger.info("@@@@@@@@ watching Proud - END[not parameter]");
    			return;
    		}
        	always = always ? true : false;
    		String url = null;
        	String msg = "";

    		switch(params) {
    		case "proud-ikebukuro":
    			url = URL_PROUD_IKEBUKURO;
    			msg = "【ProudTower池袋】";
    			break;
    		}
            if (StringUtils.isEmpty(url)) {
        		logger.info("@@@@@@@@ watching Proud - END[not supported:{}]", params);
    			return;
            }
        	

    		// 予約画面
        	Selenide.open(url);

        	WebElement dataTable = Selenide.$(By.cssSelector("table.day-select-tbl"));

    		String sysTime = com.ruoyi.common.utils.DateUtils.dateTimeNow();

        	if (!dataTable.isDisplayed()) {
        		logger.info("@@@@@@@@ watching Proud - END[not accepted]");
    			return;
    		}


        	boolean existFlag = false;
        	msg = msg + "<br/>";
        	int count = 0;
        	

    		// 予約可能枠を取得
        	List<WebElement> days = dataTable.findElements(By.cssSelector("tr"));
        	for (WebElement day : days) {
        		List<WebElement> reservableTimes = day.findElements(By.cssSelector("td.ok"));
        		if (reservableTimes.size() == 0) {
        			continue;
        		}
        		// 予約可能の日がある場合
        		boolean reservable = reservableTimes.stream().allMatch(e -> e.isDisplayed());
        		if (reservable) {
        			count++;
        		}
        	}
        	
        	if (count > 0) {
        		existFlag = true;
            	msg = msg + String.format(MSG_FMT, "直近", count) + "<br/>";
        	}
        	
        	if (!existFlag) {
            	msg = msg + NOT_RESERVATION + "<br/>";
        	}

        	if (existFlag || always) {
        		msg = msg + String.format("<a target='_blank' href='%s'>予約サイト</a>", url);
        		this.sendMsg(msg, url);
        	}
        	

    		logger.info("@@@@@@@@ watching Proud - END");

    	} catch (Exception e) {
    		logger.error("Failed to watching Proud", e);
    	} finally {
            try {
				poolTargetSourceWebDriver.releaseTarget(webDriver);
			} catch (Exception e) {
				logger.error("Failed to release web driver.", e);
			}
		}
    	
    }

    public void sendMsg(String msg, String url) {

    	Result<Page<WxUser>> users = WxPusher.queryWxUser(APP_TOKEN, 1, 100);
    	Set<String> uids = users.getData().getRecords().stream().map(o -> o.getUid()).collect(Collectors.toSet());

    	Message message = new Message();
    	message.setAppToken(APP_TOKEN);
    	message.setContentType(Message.CONTENT_TYPE_HTML);
    	message.setContent(msg);
    	message.setUids(uids);
    	message.setUrl(url);// 省略可
    	WxPusher.send(message);

    }
}
