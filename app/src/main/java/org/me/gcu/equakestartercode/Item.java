package org.me.gcu.equakestartercode;

import java.io.Serializable;
/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */

/**Item class helps in creating a new item record
 * It will be used when reading the feeds*/
public class Item implements Serializable{
    private String title;
    private Description description;
    private String link;
    private String category;

    public Item(){ }

    // getters and setters:::::::::::::::::::::::::::::::::::::::::::::::::::
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}
