package com.ruoyi.aucper.yahooapi;

import static com.codeborne.selenide.Selenide.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.ruoyi.aucper.yahooapi.dto.ExhibitInfoDTO;
import com.ruoyi.aucper.yahooapi.dto.ExhibitInfoDTO.BID_STATUS;

public class YahooAPIService {

	// 開始・終了時間
	private static final String FMT_DATE_JSON = "yyyy-MM-dd HH:mm:ss";
	private static final String FMT_DATE_HTML = "yyyy.MM.dd（E）HH:mm";

	public ExhibitInfoDTO getExhibitInfoById(String id) {

		String baseurl = "https://page.auctions.yahoo.co.jp/jp/auction/";


		open(baseurl + id);

		ExhibitInfoDTO exhibitInfoDTO = new ExhibitInfoDTO();
		// 商品（オークション）のID
		exhibitInfoDTO.setAuctionID(id);


		boolean existJson = true;
		Map<String, Map<String, String>> pageData = executeJavaScript("return pageData;");
//		Object pageData = executeJavaScript("return pageData;");
		System.out.println("aaaaaaaaa");
		System.out.println(pageData.get("items"));

//		try {
//			Map<String, String> items = pageData.get("items");
//
//			// 商品（オークション）のタイトル
//			exhibitInfoDTO.setTitle(items.get("productName"));
//			// 現在価格
//			BigDecimal price = BigDecimal.ZERO;
//			try {
//				price = NumberUtils.toScaledBigDecimal(items.get("price"));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			exhibitInfoDTO.setPrice(price);
//			// 現在の入札数
//			exhibitInfoDTO.setBids(NumberUtils.toInt(items.get("bids")));
//			// 出品開始日時
//			String starttime = items.get("starttime");
//			if (StringUtils.isNotEmpty(starttime)) {
//				exhibitInfoDTO.setEndDate(DateUtils.parseDate(starttime, FMT_DATE_JSON));
//			}
//			// 出品終了日時
//			String endDate = items.get("endtime");
//			if (StringUtils.isNotEmpty(endDate)) {
//				exhibitInfoDTO.setEndDate(DateUtils.parseDate(endDate, FMT_DATE_JSON));
//			}
//			// 状態
//			String isClosed = items.get("isClosed");
//			exhibitInfoDTO.setStatus(BID_STATUS.open.value);
//			if ("1".equals(isClosed)) {
//				exhibitInfoDTO.setStatus(BID_STATUS.closed.value);
//			}
//
//		} catch (Exception e) {
//			System.out.println(e);
//			e.printStackTrace();
//			existJson = false;
//		}

		// JSONより取得できなかった場合、スクレイピングする
		existJson = false;
		if (!existJson) {
			// 商品（オークション）のタイトル
			exhibitInfoDTO.setTitle($(".ProductTitle__text").text());
			// 現在価格
			BigDecimal price = BigDecimal.ZERO;
			try {
				NumberFormat format = DecimalFormat.getInstance(Locale.JAPAN);
				price = org.springframework.util.NumberUtils.parseNumber($(".Price__value").text().trim(), BigDecimal.class, format);
			} catch (Exception e) {
				e.printStackTrace();
			}
			exhibitInfoDTO.setPrice(price);

			// 現在の入札数
			String bids = $(".Count__count .Count__number").text();
			bids = bids.replaceAll("[^0-9].*$", "");
			exhibitInfoDTO.setBids(NumberUtils.toInt(bids));

			ElementsCollection elements = $(".ProductDetail__items--primary").$$(".ProductDetail__description");
			// 出品開始日時
			String starttime = elements.get(1).getText().replaceAll("：", "");
			System.out.println("starttime=" + starttime);
			if (StringUtils.isNotEmpty(starttime)) {
				try {
					exhibitInfoDTO.setEndDate(DateUtils.parseDate(starttime, FMT_DATE_HTML));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			// 出品終了日時
			String endDate = elements.get(2).getText().replaceAll("：", "");
			System.out.println("endDate=" + endDate);
			if (StringUtils.isNotEmpty(endDate)) {
				try {
					exhibitInfoDTO.setEndDate(DateUtils.parseDate(endDate, FMT_DATE_HTML));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			// 状態
			String statusStr = $(".Count__count--sideLine .Count__number").getText();
			exhibitInfoDTO.setStatus(BID_STATUS.open.value);
			if (StringUtils.isNotEmpty(statusStr) && statusStr.startsWith(BID_STATUS.closed.text)) {
				exhibitInfoDTO.setStatus(BID_STATUS.closed.value);
			}


		}

		// 最高額入札者のID
		String keyText = "最高額入札者";
		if (BID_STATUS.closed.value.equals(exhibitInfoDTO.getStatus())) {
			keyText = "落札者";
		}

		SelenideElement element = $x("//ul[contains(@class, 'ProductDetail__items--secondary')]/li/dl/dt[text()='" + keyText + "']");
		if (element.exists()) {
			SelenideElement bidderElment = element.parent().find("dd.ProductDetail__description");
			// 「：c*e*f*** / 評価 26459」
			String regex = "^：(.+) / (\\d+)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(bidderElment.getText());
			if(matcher.find()) {
				exhibitInfoDTO.setHighestBiddersBidderId(matcher.group(1));
				String point = matcher.group(2);
				if (StringUtils.isNotEmpty(point)) {
					exhibitInfoDTO.setHighestBiddersBidderRatingPoint(NumberUtils.toInt(point));
				}
	        }
		}

		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=" + "");
		System.out.println(element);




		// 出品者のID
		exhibitInfoDTO.setSellerId($(".Seller__name a").text());
		// 出品者の評価ポイント
		Integer ratingPoint = 0;
		try {
//			ratingPoint = NumberUtils.parseNumber($(".Seller__ratingSum a").text().trim(), Integer.class);
		} catch (Exception e) {
		}
		exhibitInfoDTO.setSellerRatingPoint(ratingPoint);


		Selenide.closeWebDriver();


		System.out.println("---------------------");
		System.out.println(exhibitInfoDTO.toString());


		return null;
	}


	public static void main(String args[]) {
		YahooAPIService service = new YahooAPIService();
		System.out.println("begin");
//		service.getExhibitInfoById("h1030142899");
//		service.getExhibitInfoById("k1037411730");
		service.getExhibitInfoById("x1038482511");
		System.out.println("end");
	}

}
