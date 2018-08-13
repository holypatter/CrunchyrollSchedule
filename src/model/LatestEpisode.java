package model;

public class LatestEpisode {
    private int epNum;
    private String name;
    private String premiumAvailableTime;
    private String seriesID;

    public LatestEpisode(int epNum, String name, String premiumAvailableTime, String seriesID) {
        this.epNum = epNum;
        this.name = name;
        this.premiumAvailableTime = premiumAvailableTime;
        this.seriesID = seriesID;
    }

    /**
     * getters and setters
     */
    public int getEpNum() {
        return epNum;
    }

    public String getName() {
        return name;
    }

    public void setEpNum(int epNum) {
        this.epNum = epNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPremiumAvailableTime(String premiumAvailableTime) {
        this.premiumAvailableTime = premiumAvailableTime;
    }

    public String getPremiumAvailableTime() {
        return premiumAvailableTime;
    }

    public void setSeriesID(String seriesID) { this.seriesID = seriesID; }

    public String getSeriesID() { return seriesID; }
}
