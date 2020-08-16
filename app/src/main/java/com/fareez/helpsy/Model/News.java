package com.fareez.helpsy.Model;

public class News {

    private String NewsAuthor, NewsContent, NewsImage, NewsTitle, nid, publishDate, publishTime;

    public News()
    {

    }


    public News(String newsAuthor, String newsContent, String newsImage, String newsTitle, String nid, String publishDate, String publishTime) {
        NewsAuthor = newsAuthor;
        NewsContent = newsContent;
        NewsImage = newsImage;
        NewsTitle = newsTitle;
        this.nid = nid;
        this.publishDate = publishDate;
        this.publishTime = publishTime;
    }

    public String getNewsAuthor() {
        return NewsAuthor;
    }

    public void setNewsAuthor(String newsAuthor) {
        NewsAuthor = newsAuthor;
    }

    public String getNewsContent() {
        return NewsContent;
    }

    public void setNewsContent(String newsContent) {
        NewsContent = newsContent;
    }

    public String getNewsImage() {
        return NewsImage;
    }

    public void setNewsImage(String newsImage) {
        NewsImage = newsImage;
    }

    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
