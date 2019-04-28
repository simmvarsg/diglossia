package thamizh.andro.org.diglossia.data;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import thamizh.andro.org.diglossia.R;

/**
 * Created by arnald on 4/5/16.
 */
public class CustomerInfo {
    private static final String TAG = "CustomerInfo";
    public String name;
    public String region;
    public String type;
    public String url;
    public String faresurl;
    public String path;
    public int digits; //Added by varad 4/14
    public String countrycode;
    public String server;
    public String AWS;
    public String KEY;
    public String SECRET;

    public boolean isCustomerTypeTaxi() {
        return type.contains("Taxi");
    }

    public static CustomerInfo getCustomerInfo(Context context) {
        AppConfig config = AppConfig.getInstance(context);
        CustomerInfo customer = new CustomerInfo();

        customer.name = config.getCustomer();
        customer.region = config.getRegion();
        customer.type = config.getType();
        customer.url = config.getUrl();
        customer.faresurl = config.getUrl();
        customer.path = config.getPath();
        customer.digits = config.getDigits(); //added by varad 4/14
        customer.countrycode = config.getCountryCode(); //added by varad 5/12
        customer.server = config.getServer();
        customer.AWS = config.getAWS();
        customer.KEY = config.getKEY();
        customer.SECRET = config.getSecret();
       return customer;
    }

    public static CustomerInfo provisionCustomerIfRequired(Context context) {
        AppConfig config = AppConfig.getInstance(context);
        if (!TextUtils.isEmpty(config.getCustomer()) && !TextUtils.isEmpty(config.getServer())) {
            // already provisioned
            return getCustomerInfo(context);
        }

        // provision will be performed one time!
        CustomerInfo customer = getFromXml(context);

        config.setPath(customer.path);
        config.setCustomer(customer.name);
        config.setRegion(customer.region);
        config.setType(customer.type);
        config.setUrl(customer.url);
        config.setFaresUrl(customer.faresurl);
        config.setDigits(customer.digits); //added by varad 4/14
        config.setCountryCode(customer.countrycode);
        config.setServer(customer.server);
        config.setAWS(customer.AWS);
        config.setKEY(customer.KEY);
        config.setSecret(customer.SECRET);
        Log.i(TAG, "" + customer);

        return customer;
    }

    private static CustomerInfo getFromXml(Context context) {
        XmlResourceParser xml = context.getResources().getXml(R.xml.customer_info);
        int xmlEventType;
        CustomerInfo customer = new CustomerInfo();

        try {
            while ((xmlEventType = xml.next()) != XmlResourceParser.END_DOCUMENT) {
                if (xmlEventType != XmlResourceParser.START_TAG) {
                    continue;
                }

                String name = xml.getName();

                if (name.equals("name")) {
                    xml.next();
                    customer.name = xml.getText();
                } else if (name.equals("region")) {
                    xml.next();
                    customer.region = xml.getText();
                } else if (name.equals("type")) {
                    xml.next();
                    customer.type = xml.getText();
                } else if (name.equals("url")) {
                    xml.next();
                    customer.url = xml.getText();
                } else if (name.equals("faresurl")) {
                    xml.next();
                    customer.faresurl = xml.getText();
                } else if (name.equals("path")) {
                    xml.next();
                    customer.path = Environment.getExternalStorageDirectory().getAbsolutePath() + xml.getText() + "/";
                }
                else if (name.equals("digits")) {
                    xml.next();
                    customer.digits = Integer.parseInt(xml.getText());
                }
                else if (name.equals("countrycode")) {
                    xml.next();
                    customer.countrycode = xml.getText();
                }
                else if (name.equals("server")) {
                    xml.next();
                    customer.server = xml.getText();
                }
                else if (name.equals("AWS")) {
                    xml.next();
                    customer.AWS = xml.getText();
                }
                else if (name.equals("KEY")) {
                    xml.next();
                    customer.KEY = xml.getText();
                }
                else if (name.equals("SECRET")) {
                    xml.next();
                    customer.SECRET  = xml.getText();
                }
                else if (name.equals("KEY2")) {
                    xml.next();
                    customer.SECRET  += xml.getText();
                }
            }
        } catch (Exception e) { return null;}

        xml.close();

        return customer;
    }

    @Override
    public String toString() {
        return "name:" + name + " region:" + region + " type:" + type
                + " url:" + url + " faresurl:" + faresurl + " path:" + path;
    }
}
