package com.ruoyi.aucper.watcher;

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
	private static final String URL_HARUMI = "https://www.31sumai.com/attend/X1604/?utm_source=straight&utm_medium=mm&utm_campaign=X1604&utm_term=limited1&utm_content=1&mkt_tok=NDI2LUJDTy03MDMAAAGEjguP8VGbWQWrGa7tpQYfYuoPiSd6q6UQWwwK-JhxrUkzhaI4ypDwyaxghOSeofclaqXJain7L4gu7Dxm05RqcUzchnJBqtqvUqgu7b6hd8GLTQRc";
	private static final String URL_HAMAMATSUCHO = "https://www.31sumai.com/attend/X1913/?utm_source=AtOnce&utm_medium=mm&utm_campaign=X1913&utm_term=20221031&utm_content=3&mkt_tok=NDI2LUJDTy03MDMAAAGHzAfq4LNWZN9wWWWSQqK0_9Yf6y2yLIptKBfvbYd3Z14eb6JPqGWSnVQk04gG0HombWVfDj8JxVArcjv91EpHxo4mE69LxNpoTS5JGtLKZYt8GwQ";
	private static final String NOT_ACCEPTED = "来場予約を受け付けておりません";

	private static final String MSG_FMT = "%s　予約可能：%s日";

    private static final Logger logger = LoggerFactory.getLogger(ReservationFrameWatcher.class);

    @Autowired
    @Qualifier("poolTargetSourceWebDriver")
    private CommonsPool2TargetSource poolTargetSourceWebDriver;

    /**
     * Watching三井不動産の予約枠
     */
    public void watch31sumai(String params) {

    	WebDriver webDriver = null;
    	try {
    		webDriver = (WebDriver) poolTargetSourceWebDriver.getTarget();

    		/**
    		 * 上記でプールからWebDriverオブジェクトを取得する為に時間が要する為
    		 * 開始ログや更新処理などをここ以降に実装する
    		 */
    		WebDriverRunner.setWebDriver(webDriver);
    		logger.info("@@@@@@@@ watching 31sumai - START [{}]", params);
    		logger.info("### use webdriver:{}", webDriver.toString());

    		if (StringUtils.isEmpty(params)) {
        		logger.info("@@@@@@@@ watching 31sumai - END[not parameter]");
    			return;
    		}
    		String url = null;
    		switch(params) {
    		case "harumi":
    			url = URL_HARUMI;
    			break;
    		case "hamamatsucho":
    			url = URL_HAMAMATSUCHO;
    			break;
    		}
            if (StringUtils.isEmpty(url)) {
        		logger.info("@@@@@@@@ watching 31sumai - END[not supported:{}]", params);
    			return;
            }

        	Selenide.open(url);

        	WebElement header = Selenide.$(By.cssSelector("div.ui-datepicker-header"));

        	if (!header.isDisplayed()) {
        		logger.info("@@@@@@@@ watching 31sumai - END[not accepted]");
    			return;
    		}

        	String msg = "";
        	int count = 1;
        	while (true) {

        		// 年月
            	String yearMount = header.findElement(By.cssSelector("div.ui-datepicker-title")).getText();
            	WebElement calendar = Selenide.$(By.cssSelector("table.ui-datepicker-calendar"));
//            	int ngDays = calendar.findElements(By.cssSelector("table.ui-datepicker-calendar td.status_1")).size();
            	int okDays1 = calendar.findElements(By.cssSelector("table.ui-datepicker-calendar td.status_2")).size();
            	int okDays2 = calendar.findElements(By.cssSelector("table.ui-datepicker-calendar td.status_3")).size();

            	if (okDays1 > 0 || okDays2 > 0) {
                	msg = msg + String.format(MSG_FMT, yearMount, (okDays1 + okDays2)) + System.lineSeparator();
            	}

        		if (count >= 2) {
        			break;
        		}
            	// 次ボタン
            	header.findElement(By.cssSelector("a.ui-datepicker-next")).click();
        		count++;
        	}

        	if (StringUtils.isNotEmpty(msg)) {
        		this.sendMsg(msg);
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

    public void sendMsg(String msg) {

    	Result<Page<WxUser>> users = WxPusher.queryWxUser(APP_TOKEN, 1, 100);
    	Set<String> uids = users.getData().getRecords().stream().map(o -> o.getUid()).collect(Collectors.toSet());

    	Message message = new Message();
    	message.setAppToken("AT_0UihKvBoi92ExbEigVWoUddrEAo9Grlq");
    	message.setContentType(Message.CONTENT_TYPE_TEXT);
    	message.setContent(msg);
    	message.setUids(uids);
//    	message.setUrl("http://wxpuser.zjiecode.com");// 省略可
    	WxPusher.send(message);

    }
}
