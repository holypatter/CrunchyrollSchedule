package model;

public class LatestEpisode {
    private int epNum;
    private String name;
    private String premiumAvailableTime;

    public LatestEpisode(int epNum, String name, String premiumAvailableTime) {
        this.epNum = epNum;
        this.name = name;
        this.premiumAvailableTime = premiumAvailableTime;
    }

    public int getEpNum() {
        return epNum;
    }

    public String getName() {
        return name;
    }

    public String getPremiumAvailableTime() {
        return premiumAvailableTime;
    }
}
