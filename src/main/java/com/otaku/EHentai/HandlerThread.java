package com.otaku.EHentai;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import entity.EHentai;
@Component
public class HandlerThread  extends Observable implements Runnable{

    @Value("${conf.cookies}")
	String cookies;
   
	String text;
	String url;
	String honName;
    String category;
    String uploader;
    Date postTime;
    float  fileSize;
    int    length;
    int    favoratedTimes;
    int    ratingCount;
	float  ratingLabel; 
    String group;
    String femaleCategory;
    String maleCategory;
    String character;
    String language;
    String artist;
    String parody;
    String misc;
    String lastUrl;
    
	@Autowired
	public BlockingQueue<String> toBeHandledQueue;
	
	@Autowired
	public BlockingQueue<EHentai> handledQueue;
	
	@Autowired
	public Logger log;
    
	private final int  THREADNAME;
	private final Pattern TITLE_PATTERN = Pattern.compile("(?<=<h1 id=\"gn\">).{1,150}(?=</h1>    <h1 id=\"gj\">)");
	private final Pattern CATEGORY_PATTERN = Pattern.compile("(?<=alt=\").+(?=class=\"ic\")");
	private final Pattern UPLOADER_PATTERN = Pattern.compile("(?<=<a href=\"https://e-hentai.org/uploader/).{1,15}(?=\">)");
	private final Pattern POSTTIME_PATTERN = Pattern.compile("(?<=<td class=\"gdt2\">).{16}(?=</td>)");
	private final Pattern FILESIZE_PATTERN = Pattern.compile("(?<=<td class=\"gdt2\">).{1,10}(?=MB</td>)");
	private final Pattern PAGES_PATTERN = Pattern.compile("(?<=<td class=\"gdt2\">).{1,10}(?=pages</td>)");
	private final Pattern FAVORITE_PATTERN = Pattern.compile("(?<=<td class=\"gdt2\" id=\"favcount\">).{1,10}(?=times</td>)");
	private final Pattern RATING_CNT_PATTERN = Pattern.compile("(?<=<span id=\"rating_count\">).{1,10}(?=</span>)");
	private final Pattern RATING_AVERAGE_PATTERN = Pattern.compile("(?<=<td id=\"rating_label\" colspan=\"3\">Average: ).{1,10}(?=</td>)");
	private final Pattern ARTIST_PATTERN = Pattern.compile("(?<=<a id=\"ta_artist:).{1,50}(?=\" href)");
	private final Pattern FEMALE_PATTERN = Pattern.compile("(?<=<a id=\"ta_female:).{1,50}(?=\" href)");
	private final Pattern PARODY_PATTERN = Pattern.compile("(?<=<a id=\"ta_parody:).{1,50}(?=\" href)");
	private final Pattern MISC_PATTERN = Pattern.compile("(?<=misc:</td>.{1,300}<a id=\"ta_).{1,50}(?=\" href=\"https://e-hentai.org/tag)");
	private final Pattern MALE_PATTERN = Pattern.compile("(?<=<a id=\"ta_male:).{1,50}(?=\" href)");
	private final Pattern GROUP_PATTERN = Pattern.compile("(?<=<a id=\"ta_group:).{1,50}(?=\" href)");
	private final Pattern CHARACTER_PATTERN = Pattern.compile("(?<=<a id=\"ta_character:).{1,50}(?=\" href)");
	private final Pattern LANGUAGE_PATTERN = Pattern.compile("(?<=<td class=\"gdt1\">Language:</td><td class=\"gdt2\">).{1,20}(?=&nbsp;)");
	private static final int MAX_RETRY_CNT = 5;
	private static Set<String> visitedSet = Collections.synchronizedSet(new HashSet<String>());
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("serial")
	public HashSet<String> CATEGORYSET = new HashSet<String>(){{
			add("doujinshi");
			add("manga");
			add("artist cg");
			add("game cg");
		    add("western");
		    add("non-h");
		    add("image set");
		    add("cospaly");
		    add("asian porn");
		   	add("misc");}};
	
	public HandlerThread(int threadNo,ThreadObserver threadObserver){
		this.THREADNAME = threadNo;
		this.addObserver(threadObserver);
	}

	public String get_response_html(String url) throws Exception{
		 Connection connection  = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 ??+"
					              + "??(KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36").cookie("Cookie", cookies).header("Referer", lastUrl);
		 String html= connection.get().html().replaceAll("\n", "").replaceAll("          ", "").replaceAll("         ", "");
		 return html;
	}
	
	public String getTitle(String text){
		Matcher matcher = TITLE_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			return matcher.group();
		}
		return "";
	}
	
	public String getCategory(String text){
		Matcher matcher = CATEGORY_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			for(String category : CATEGORYSET){
				if(matcher.group().toLowerCase().contains(category)){
				    return category;
				}
			}
		}
		return "";
	}
	
	public String getCharacter(String text){
		Matcher matcher = CHARACTER_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group());
		}
		return tmp.toString();
	}
	
	public String getUploader(String text){
		Matcher matcher = UPLOADER_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			return matcher.group();
		}
		return "";
	}
	
	public Date getPostTime(String text){
		Matcher matcher = POSTTIME_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			try {
				return sdf.parse(matcher.group()+":00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public float getFileSize(String text){
		Matcher matcher = FILESIZE_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			return Float.valueOf(matcher.group());
		}
		return 0.0f;
	}
	
	public int getPAGES(String text){
		Matcher matcher = PAGES_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			return Integer.valueOf(matcher.group().trim());
		}
		return 0;
	}
	
	public int getFavoriteCnt(String text){
		Matcher matcher = FAVORITE_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			return Integer.valueOf(matcher.group().trim());
		}
		return 0;
	}
	
	public int getRatingCnt(String text){
		Matcher matcher = RATING_CNT_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			return Integer.valueOf(matcher.group().trim());
		}
		return 0;
	}
	
	public float getRatingAverage(String text){
		Matcher matcher = RATING_AVERAGE_PATTERN.matcher(text);
		matcher.matches();
		if(matcher.find()){
			return Float.valueOf(matcher.group());
		}
		return 0.0f;
	}
	
	public String getArtist(String text){
		Matcher matcher = ARTIST_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group());
		}
		return tmp.toString();
	}
	
	public String getFemale(String text){
		Matcher matcher = FEMALE_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group());
		}
		return tmp.toString();
	}
	
	public String getParody(String text){
		Matcher matcher = PARODY_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group());
		}
		return tmp.toString();
	}
	
	public String getMISC(String text){
		Matcher matcher = MISC_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group());
		}
		return tmp.toString();
	}
	
	public String getMale(String text){
		Matcher matcher = MALE_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group());
		}
		return tmp.toString();
	}
	
	public String getGroup(String text){
		Matcher matcher = GROUP_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group());
		}
		return tmp.toString();
	}
	
	public String getLanguage(String text){
		Matcher matcher = LANGUAGE_PATTERN.matcher(text);
		LinkedList<String> tmp = new LinkedList<String>();
		matcher.matches();
		while(matcher.find()){
			tmp.add(matcher.group().trim());
		}
		return tmp.toString();
	}
	
	public void scan_url(String url, int retry_counts) {
		
		if(visitedSet.contains(url)){
			log.info("==========重复==========");
			return;
		}
		EHentai manga = new EHentai();
		try {
			if(lastUrl == null){
				lastUrl = url;
			}
			text = get_response_html(url);
			lastUrl = url;
			honName = getTitle(text);
			category = getCategory(text);
			uploader = getUploader(text);
			postTime = getPostTime(text);
			fileSize = getFileSize(text);
			length = getPAGES(text);
			favoratedTimes = getFavoriteCnt(text);
			ratingCount = getRatingCnt(text);
			ratingLabel = getRatingAverage(text);
			group = getGroup(text);
			femaleCategory = getFemale(text);
			maleCategory = getMale(text);
			parody = getParody(text);
			misc = getMISC(text);
			maleCategory = getMale(text);
			language = getLanguage(text);
			artist = getArtist(text);
		    character = getCharacter(text);
			
			manga.setUrl(url);
            manga.setHonName(honName);
            manga.setCategory(category);
            manga.setUploader(uploader);
            manga.setPostTime(postTime);
            manga.setFileSize(fileSize);
            manga.setLength(length);
            manga.setFavoratedTimes(favoratedTimes);
            manga.setRatingCount(ratingCount);
            manga.setRatingLabel(ratingLabel);
            manga.setgroupCategory(group);
            manga.setFemaleCategory(femaleCategory);
            manga.setMaleCategory(maleCategory);
            manga.setParody(parody);
            manga.setMisc(misc);
            manga.setLanguage(language);
            manga.setArtist(artist);
            manga.setCharacter(character);
			log.info("\n"+manga);
			log.info("toBeHandledQueue: "+toBeHandledQueue.size()+"/1000\n");
			handledQueue.offer(manga);
			visitedSet.add(url);
		}catch(HttpStatusException e){
			if(e.toString().contains("Status=4")){
				log.error("err took place in thread "+this.THREADNAME);
				return;
			}
			if( retry_counts != 0){
				scan_url(url, retry_counts-1);
			}
			visitedSet.add(url);
			log.error(url+"这个地址有毒或者是cdn限制");
			return;
		}catch(IllegalArgumentException e){
			log.error("err took place in thread "+this.THREADNAME);
			e.printStackTrace();
			visitedSet.add(url);
			return;
		}catch (SocketTimeoutException e){
			log.info("read time out, reconnect...  url:  "+url);
			if( retry_counts != 0){
				scan_url(url, retry_counts-1);
			}
			visitedSet.add(url);
			log.error("err took place in thread "+this.THREADNAME);
			return;
		}catch (Exception e) {
			if( retry_counts != 0){
				scan_url(url, retry_counts-1);
			}
			e.printStackTrace();
			visitedSet.add(url);
			log.error("err took place in thread "+this.THREADNAME);
			return;
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				if(handledQueue.size() > 100){
					log.info("数据库线程队列深度大于100");
			        Thread.sleep(10000);
				}
				url = toBeHandledQueue.take();
				Thread.sleep((long) (Math.random()*8000));
				scan_url(url, MAX_RETRY_CNT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

