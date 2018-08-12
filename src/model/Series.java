package model;

import java.util.Objects;

public class Series {
    private String name;
    private String imgLargeURL;
    private String imgFullURL;
    private String id;

    public Series(String id) {
        this.id = id;
        name = "";
        imgLargeURL = "";
        imgFullURL = "";
    }

    /**
     * getters and setters
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setImgLargeURL(String imgLargeURL) {
        this.imgLargeURL = imgLargeURL;
    }

    public void setImgFullURL(String imgFullURL) {
        this.imgFullURL = imgFullURL;
    }

    public String getImgLargeURL() {
        return imgLargeURL;
    }

    public String getImgFullURL() {
        return imgFullURL;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    /**
     * two series object are equal if their ids are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Series)) return false;
        Series series = (Series) o;
        return Objects.equals(id, series.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
