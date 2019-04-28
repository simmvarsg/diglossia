package thamizh.andro.org.diglossia.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by arnald on 3/21/16.
 */
public class AppConfig {
    private static AppConfig config = null;
    private Context context = null;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    // TODO: FIXME: avoid hardcoding

    // key constants
    private final String CONFIG_FARES_URL = "faresurl";
    private final String CONFIG_URL = "url";
    private final String CONFIG_CUSTOMER = "customer";
    private final String CONFIG_PATH = "path";
    private final String CONFIG_TYPE = "type";
    private final String CONFIG_REGION = "region";
    private final String CONFIG_BOOKINGURL = "bookingurl";
    private final String CONFIG_DIGITS = "digits";

    private final String CONFIG_NAME = "name";
    private final String CONFIG_PHONE = "phoneno";
    private final String CONFIG_COUNTRY = "countrycode";
    private final String CONFIG_EMAILID = "emailid";
    private final String CONFIG_EMPADDRESS = "addresss";
    private final String CONFIG_EMPID = "empid";
    private final String CONFIG_PASSWORD ="password";
    private final String CONFIG_CAMPUS ="campus";
    private final String CONFIG_TRANSPORTDESK ="transportdesk";
    private final String CONFIG_AUTH = "authenticated";
    private final String CONFIG_AUTOLOGIN = "autologin";
    private final String CONFIG_GENDER = "gender";
    private final String CONFIG_NOTIFYL = "notifylist";
    private final String CONFIG_NOTIFYD = "notifydist";
    private final String CONFIG_SERVER = "server";
    private final String CONFIG_AWS = "aws";
    private final String CONFIG_KEY = "key";
    private final String CONFIG_SECRET = "secret";
    private final String CONFIG_RATING = "rating";
    private final String CONFIG_COOKIES = "cookies";

    // value caching
    private String faresUrl = null;
    private String url = null;
    private String customer = null;
    private String path = null;
    private String type = null;
    private String region = null;
    private String bookingurl = null;
    private int digits = 0;
    private String name = null;
    private String phone = null;
    private String countrycode = null;
    private String emailid = null;
    private String empaddress = null;
    private String empid = null;
    private String password = null;
    private String campus = null;
    private String transportdesk = null;
    private boolean authenticated = false;
    private boolean autologin = false;
    private int gender = 0;
    private String notifylist = "Notified-Boarded-Reached-Deviation-Dropped";
    private String notifydist = "200";
    private String server = "";
    private String aws = "https://sqs.us-east-1.amazonaws.com/711643634223/";
    private String key = "AKIAJUQ5IXQEWKWNRR4Q";
    private String secret = "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";
    private String rating = "5";
    private String cookies = "100";

    public static AppConfig getInstance(Context context) {
        if (config == null) {
            config = new AppConfig();
            config.init(context);
        }

        return config;
    }
    private void init(Context context) {
        this.context = context;
        this.editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setFaresUrl(String url) {
        faresUrl = url;
        editor.putString(CONFIG_FARES_URL, url);
        editor.commit();
    }
    public void setDefaultFaresUrl() {
        setFaresUrl("http://dotcabs.com/tariff.html");
    }

    public String getFaresUrl() {
        if (faresUrl == null) {
            faresUrl = prefs.getString(CONFIG_FARES_URL, "");
        }
        return faresUrl;
    }
    public void setUrl(String url) {
        this.url = url;
        editor.putString(CONFIG_URL, url);
        editor.commit();
    }
    public void setDefaultUrl() {
        setUrl("http://dotcabs.com/tariff.html");
    }

    public String getUrl() {
        if (url == null) {
            url = prefs.getString(CONFIG_URL, "");
        }
        return url;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
        editor.putString(CONFIG_CUSTOMER, customer);
        editor.commit();
    }
    public String getCustomer() {
        if (customer == null) {
            customer = prefs.getString(CONFIG_CUSTOMER, "");
        }
        return customer;
    }
    public void setServer(String value) {
        this.server = value;
        editor.putString(CONFIG_SERVER, value);
        editor.commit();
    }


    public String getServer() {
        server = prefs.getString(CONFIG_SERVER, "");
        return server;
    }
    public void setPath(String type) {
        this.path = type;
        editor.putString(CONFIG_PATH, type);
        editor.commit();
    }
    public String getPath() {
        if (path == null) {
            path = prefs.getString(CONFIG_PATH, "");
        }
        return path;
    }
    public void setType(String type) {
        this.type = type;
        editor.putString(CONFIG_TYPE, type);
        editor.commit();
    }
    public String getType() {
        if (type == null) {
            type = prefs.getString(CONFIG_TYPE, "");
        }
        return type;
    }
    public void setDigits(int type) {
        this.digits = type;
        editor.putInt(CONFIG_DIGITS, type);
        editor.commit();
    }
    public int getDigits() {
        digits = prefs.getInt(CONFIG_DIGITS, 10);
        return digits;
    }
    public void setRegion(String region) {
        this.region = region;
        editor.putString(CONFIG_REGION, region);
        editor.commit();
    }



    public String getRegion() {
        if (region == null) {
            region = prefs.getString(CONFIG_REGION, "");
        }
        return region;
    }
    public void setBookingUrl(String url) {
        this.bookingurl = url;
        editor.putString(CONFIG_BOOKINGURL, url);
        editor.commit();
    }



    public String getBookingUrl() {
        if (bookingurl == null) {
            bookingurl = prefs.getString(CONFIG_BOOKINGURL, "");
        }
        return bookingurl;
    }
    public void setName(String value) {
        this.name = value;
        editor.putString(CONFIG_NAME, value);
        editor.commit();
    }
    public String getName() {
        if (name == null) {
            name = prefs.getString(CONFIG_NAME, "");
        }
        return name;
    }
    public void setPhone(String value) {
        this.phone = value;
        editor.putString(CONFIG_PHONE, value);
        editor.commit();
    }
    public String getPhone() {
        if (phone == null) {
            phone = prefs.getString(CONFIG_PHONE, "");
        }
        return phone;
    }
    public void setCountryCode(String value) {
        this.countrycode = value;
        editor.putString(CONFIG_COUNTRY, value);
        editor.commit();
    }
    public String getCountryCode() {
        if (countrycode == null) {
            countrycode = prefs.getString(CONFIG_COUNTRY, "");
        }
        return countrycode;
    }
    public void setEmail(String value) {
        this.emailid = value;
        editor.putString(CONFIG_EMAILID, value);
        editor.commit();
    }
    public String getEmail() {
        if (emailid == null) {
            emailid = prefs.getString(CONFIG_EMAILID, "");
        }
        return emailid;
    }
    public void setEmpAddress(String value) {
        this.empaddress = value;
        editor.putString(CONFIG_EMPADDRESS, value);
        editor.commit();
    }
    public String getEmpAddress() {
        if (empaddress == null) {
            empaddress = prefs.getString(CONFIG_EMPADDRESS, "");
        }
        return empaddress;
    }
    public void setCampus(String value) {
        this.campus = value;
        editor.putString(CONFIG_CAMPUS, value);
        editor.commit();
    }
    public String getCampus() {
        if (campus == null) {
            campus = prefs.getString(CONFIG_EMPADDRESS, "");
        }
        return campus;
    }
    public void setTransportDesk(String value) {
        this.transportdesk = value;
        editor.putString(CONFIG_TRANSPORTDESK, value);
        editor.commit();
    }
    public String getTransportDesk() {
        if (transportdesk == null) {
            transportdesk = prefs.getString(CONFIG_TRANSPORTDESK, "");
        }
        return transportdesk;
    }
    public void setEmpID(String value) {
        this.empid = value;
        editor.putString(CONFIG_EMPID, value);
        editor.commit();
    }
    public String getEmpID() {
        if (empid == null) {
            empid = prefs.getString(CONFIG_EMPID, "");
        }
        return empid;
    }
    public void setPassword(String value) {
        this.password = value;
        editor.putString(CONFIG_PASSWORD, value);
        editor.commit();
    }
    public String getPassword() {
        if (password == null) {
            password = prefs.getString(CONFIG_PASSWORD, "");
        }
        return password;
    }
    public void setAuth(boolean value) {
        this.authenticated = value;
        editor.putBoolean(CONFIG_AUTH, value);
        editor.commit();
    }
    public boolean getAuth() {
        authenticated = prefs.getBoolean(CONFIG_AUTH,false);
        return authenticated;
    }
    public void setAutoLogin(boolean value) {
        this.autologin = value;
        editor.putBoolean(CONFIG_AUTOLOGIN, value);
        editor.commit();
    }
    public boolean getAutoLogin() {
        autologin = prefs.getBoolean(CONFIG_AUTOLOGIN,false);
        return autologin;
    }
    public void setGender(int value) {
        this.gender = value;
        editor.putInt(CONFIG_GENDER, value);
        editor.commit();
    }
    public int getGender() {
        gender = prefs.getInt(CONFIG_GENDER,0);
        return gender;
    }
    public String getNotifyList() {
        return notifylist;
    }
    public void setNotifyList(String value) {
        this.notifylist = value;
        editor.putString(CONFIG_NOTIFYL, value);
        editor.commit();
    }
    public String getNotifyDist() {
        return notifydist;
    }
    public void setNotifyDist(String value) {
        this.notifydist = value;
        editor.putString(CONFIG_NOTIFYD, value);
        editor.commit();
    }
    public void setAWS(String customer) {
        this.aws = customer;
        editor.putString(CONFIG_AWS, customer);
        editor.commit();
    }
    public String getAWS() {
        if (aws == null) {
            aws = prefs.getString(CONFIG_AWS, "");
        }
        return aws;
    }
    public void setKEY(String customer) {
        this.key = customer;
        editor.putString(CONFIG_KEY, customer);
        editor.commit();
    }
    public String getKEY() {
        if (key == null) {
            key = prefs.getString(CONFIG_KEY, "");
        }
        return key;
    }
    public void setSecret(String customer) {
        this.secret = customer;
        editor.putString(CONFIG_SECRET, customer);
        editor.commit();
    }
    public String getSecret() {
        if (secret == null) {
            secret = prefs.getString(CONFIG_SECRET, "");
        }
        return secret;
    }
    public void setRating(String customer) {
        this.rating = customer;
        editor.putString(CONFIG_RATING, customer);
        editor.commit();
    }
    public String getRating() {
        rating = prefs.getString(CONFIG_RATING, "");
        return rating;
    }
    public void setCookies(String customer) {
        this.cookies = customer;
        editor.putString(CONFIG_COOKIES, customer);
        editor.commit();
    }
    public String getCookies() {
        cookies = prefs.getString(CONFIG_COOKIES, "");
        return cookies;
    }
    public void setAny(String name, String value) {
        editor.putString(name, value);
        editor.commit();
    }
    public String getAny(String name) {
        return prefs.getString(name, "");
    }

}
