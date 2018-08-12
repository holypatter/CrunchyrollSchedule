package auth;


/**
 * Get the session id by having the user log in to crunchyroll or accessing the website
 * This class only has one instance
 */
public class CrunchyrollSessionId {
    private String id;
    private static CrunchyrollSessionId instance;

    private CrunchyrollSessionId() {
        id = "";
    }

    public static CrunchyrollSessionId getInstance() {
        if (instance == null) {
            instance = new CrunchyrollSessionId();
        }

        return instance;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
