package org.me.gcu.equakestartercode;

import java.io.Serializable;

public class Item implements Serializable {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String latitude;
    private String longitude;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
//
//    @Override
//    public String toString() {
//        return "Item{" +
//                "title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", link='" + link + '\'' +
//                ", pubDate='" + pubDate + '\'' +
//                ", category='" + category + '\'' +
//                ", latitude='" + latitude + '\'' +
//                ", longitude='" + longitude + '\'' +
//                '}';
//    }
}
