package com.otaku.EHentai;

import java.io.IOException;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerThread  extends Observable implements Runnable{

	@Value("${conf.url}")
	String primaryUrl;
	
	@Value("${conf.startPage}")
	int startPage;
	
	@Value("${conf.cookies}")
	String cookies;
	
	@Autowired
	public BlockingQueue<String> toBeHandledQueue;
	
	int threadNo;
	final int MAX_RETRY_CNT = 10;
	int retryCnt = 0;
	private final Pattern COMIC_PATTERN = Pattern.compile("(?<=<div class=\"it5\"><a href=\").{1,50}(?=\" onmouseover)");

	Matcher match;
	Properties currentPageProperty;
	/*
	 * @param String url  协议+网站主域名+后缀 如 https://e-hentai.org/?page=
	 * @param int    start 起始页
	 */
	public ProducerThread(int threadNo, ThreadObserver threadObserver) {
		this.threadNo = threadNo;
		this.addObserver(threadObserver);
	}
	
	public String get_response_html(String url){
		Connection connection  = Jsoup.connect(url).timeout(6000).userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)").cookie("Cookie", cookies).header("REFERER", "https://e-hentai.org/")
				                 .header("Accept-Language","zh-CN,zh;q=0.8'").header("Accept-Charset", "utf-8;q=0.7,*;q=0.7").header("connection", "keep-alive").header("Host", "https://e-hentai.org");
		String html;
		try {
			html = connection.get().html();
		} catch (IOException e) {
			if(retryCnt < MAX_RETRY_CNT){
				System.out.println("retrying..... current cnt: "+retryCnt++);
				return get_response_html(url);
			}
			e.printStackTrace();
			return null;
		}
		return html;
	}
	
	@Override
	public void run() {
		String text; 
		while((text = get_response_html(primaryUrl+startPage++)) != null){
			//currentPageProperty.setProperty("currentPage", String.valueOf(currentPage));
			match = COMIC_PATTERN.matcher(text.replace("\n", "").replace("          ", ""));
			System.out.println("第"+startPage+"页");
			while(match.find()){
				while(toBeHandledQueue.size() > 25){
				    try {
					    Thread.sleep(20000);
				    }catch (InterruptedException e) {
					    e.printStackTrace();
				    }
			    }

				toBeHandledQueue.offer(match.group());
				System.out.println("queue length: "+toBeHandledQueue.size()+"/20");
			}
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println("居然翻完了");
		
	}
}
