package thamizh.andro.org.diglossia.model;

/**
 * Created by Varad on 04/16/2017.
 */

public class userModel {
    private String useremail ="";
    private String authToken ="";

    public userModel() {
    }

    public userModel(String useremail, String authToken, boolean hasportfolios) {
        this.useremail = useremail;
        this.authToken = authToken;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
