package entity;

import java.util.Date;

import dao.EHentaiDao_Imp;
import dao.EHentaiDao_Interface;

public class EHentai {
	
    final static EHentaiDao_Interface hentaiDao = new EHentaiDao_Imp();
    
	String url;
	String honName;
    String category;
    String uploader;
    Date postTime;
    // MB
    float  fileSize;
    int    length;
    int    favoratedTimes;
    int    ratingCount;
	float  ratingLabel; 
	String groupCategory;
	String femaleCategory;
	String maleCategory;
	String language;
	String character;
	String artist;
	String parody;
    //combined with reclass
	String misc;
    Date createTime;
    
    {

    }
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHonName() {
		return honName;
	}
	public void setHonName(String honName) {
		this.honName = honName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public float getFileSize() {
		return fileSize;
	}
	public void setFileSize(float fileSize) {
		this.fileSize = fileSize;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getFavoratedTimes() {
		return favoratedTimes;
	}
	public void setFavoratedTimes(int favoratedTimes) {
		this.favoratedTimes = favoratedTimes;
	}
	public int getRatingCount() {
		return ratingCount;
	}
	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}
	public float getRatingLabel() {
		return ratingLabel;
	}
	public void setRatingLabel(float ratingLabel) {
		this.ratingLabel = ratingLabel;
	}
	public String getgroupCategory() {
		return groupCategory;
	}
	public void setgroupCategory(String groupCategory) {
		this.groupCategory = groupCategory;
	}
	public String getFemaleCategory() {
		return femaleCategory;
	}
	public void setFemaleCategory(String femaleCategory) {
		this.femaleCategory = femaleCategory;
	}
	public String getMaleCategory() {
		return maleCategory;
	}
	public void setMaleCategory(String maleCategory) {
		this.maleCategory = maleCategory;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getParody() {
		return parody;
	}
	public void setParody(String parody) {
		this.parody = parody;
	}
	public String getMisc() {
		return misc;
	}
	public void setMisc(String misc) {
		this.misc = misc;
	}
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public void saveData(){
        hentaiDao.putManga(this);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("url: ").append(getUrl()).append("\ntitle: ").append(getHonName()).append("\npost time: ").append(getPostTime())
		  .append("\nartist: ").append(getArtist()).append("\ncharacter: ").append(getCharacter()).append("\nuploader: ").append(getUploader()).append("\ncategory: ").append("\nparody: ").append(getParody()).append("\nmisc: ").append(getMisc())
		  .append("\ncategory: ").append(getCategory()).append("\nrating: ").append(getRatingLabel()).append("\nratingCnt: ").append(getRatingCount()).append("\nfavoratedTime: ").append(getFavoratedTimes()).append("\nfileSize: ").append(getFileSize())
		  .append("\npages: ").append(getLength()).append("\ngroupCategory: ").append(getgroupCategory()).append("\nfemaleCategory: ").append(getFemaleCategory()).append("\nmaleCategory: ").append(getMaleCategory())
		  .append("\nlanguage: ").append(getLanguage()).append("\n");
		return sb.toString();
	}
}
