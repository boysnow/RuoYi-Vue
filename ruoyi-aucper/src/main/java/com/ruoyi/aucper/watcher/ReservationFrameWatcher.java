package com.ruoyi.aucper.watcher;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.codeborne.selenide.Selenide;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;

/**
 * Watcher
 *
 * @author ruoyi
 * @date 2022-05-06
 */
@Component
public class ReservationFrameWatcher {

	private static final String MSG_FMT = "%s　予約可能：%s日";

    /**
     * Watching晴海フラッグの予約枠
     */
    public void watchHarumiFlag() {

    	String url = "https://www.31sumai.com/attend/X1604/?utm_source=straight&utm_medium=mm&utm_campaign=X1604&utm_term=limited1&utm_content=1&mkt_tok=NDI2LUJDTy03MDMAAAGEjguP8VGbWQWrGa7tpQYfYuoPiSd6q6UQWwwK-JhxrUkzhaI4ypDwyaxghOSeofclaqXJain7L4gu7Dxm05RqcUzchnJBqtqvUqgu7b6hd8GLTQRc";

    	Selenide.open(url);


    	// 今現在の年月
    	WebElement header = Selenide.$(By.cssSelector("div.ui-datepicker-header"));

    	String msg = "";
    	int count = 1;
    	while (true) {

        	String yearMount = header.findElement(By.cssSelector("div.ui-datepicker-title")).getText();
        	WebElement calendar = Selenide.$(By.cssSelector("table.ui-datepicker-calendar"));
//        	int ngDays = calendar.findElements(By.cssSelector("table.ui-datepicker-calendar td.status_1")).size();
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

    }

    public void sendMsg(String msg) {

    	Message message = new Message();
    	message.setAppToken("AT_0UihKvBoi92ExbEigVWoUddrEAo9Grlq");
    	message.setContentType(Message.CONTENT_TYPE_TEXT);
    	message.setContent(msg);
    	message.setUid("UID_TFI5OLrG1ImYcf5nchxLNDG7P6B5");
    	message.setUrl("http://wxpuser.zjiecode.com");// 省略可
    	WxPusher.send(message);

    }
}
