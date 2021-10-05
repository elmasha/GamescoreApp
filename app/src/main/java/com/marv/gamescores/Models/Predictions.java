package com.marv.gamescores.Models;

import java.util.Date;

public class Predictions {
    private String category,doc_ID,image,image2,image3,image4,instagramLink,
    otherLinks,story,story1,story2,story3,story4,subheading1,subheading2,subheading3,subheading4,subtitle,title
            ,twitterLink,youtubeLink;
    private long like,comment;
    private Date timestamp;

    public Predictions() {
        //empty
    }

    public Predictions(String category, String doc_ID, String image, String image2, String image3, String image4, String instagramLink, String otherLinks, String story, String story1, String story2, String story3, String story4, String subheading1, String subheading2, String subheading3, String subheading4, String subtitle, String title,
                       String twitterLink, String youtubeLink, long like, long comment, Date timestamp) {
        this.category = category;
        this.doc_ID = doc_ID;
        this.image = image;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.instagramLink = instagramLink;
        this.otherLinks = otherLinks;
        this.story = story;
        this.story1 = story1;
        this.story2 = story2;
        this.story3 = story3;
        this.story4 = story4;
        this.subheading1 = subheading1;
        this.subheading2 = subheading2;
        this.subheading3 = subheading3;
        this.subheading4 = subheading4;
        this.subtitle = subtitle;
        this.title = title;
        this.twitterLink = twitterLink;
        this.youtubeLink = youtubeLink;
        this.like = like;
        this.comment = comment;
        this.timestamp = timestamp;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDoc_ID() {
        return doc_ID;
    }

    public void setDoc_ID(String doc_ID) {
        this.doc_ID = doc_ID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getOtherLinks() {
        return otherLinks;
    }

    public void setOtherLinks(String otherLinks) {
        this.otherLinks = otherLinks;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getStory1() {
        return story1;
    }

    public void setStory1(String story1) {
        this.story1 = story1;
    }

    public String getStory2() {
        return story2;
    }

    public void setStory2(String story2) {
        this.story2 = story2;
    }

    public String getStory3() {
        return story3;
    }

    public void setStory3(String story3) {
        this.story3 = story3;
    }

    public String getStory4() {
        return story4;
    }

    public void setStory4(String story4) {
        this.story4 = story4;
    }

    public String getSubheading1() {
        return subheading1;
    }

    public void setSubheading1(String subheading1) {
        this.subheading1 = subheading1;
    }

    public String getSubheading2() {
        return subheading2;
    }

    public void setSubheading2(String subheading2) {
        this.subheading2 = subheading2;
    }

    public String getSubheading3() {
        return subheading3;
    }

    public void setSubheading3(String subheading3) {
        this.subheading3 = subheading3;
    }

    public String getSubheading4() {
        return subheading4;
    }

    public void setSubheading4(String subheading4) {
        this.subheading4 = subheading4;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public long getLike() {
        return like;
    }

    public void setLike(long like) {
        this.like = like;
    }

    public long getComment() {
        return comment;
    }

    public void setComment(long comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
