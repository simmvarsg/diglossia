package thamizh.andro.org.diglossia.share;

import android.content.SharedPreferences;

import java.util.ArrayList;

import thamizh.andro.org.diglossia.model.qna;
import thamizh.andro.org.diglossia.model.userModel;

public class Share {

    // TODO : All Screen
    public static boolean isTripStarted;
    public static boolean reachedCustomerPlace;
    public static boolean driverResting;
    public static boolean isJobAssigned;
    public static String networkTypeString;
    public static String recid;
    public static float curr_distance;
    public static float night_curr_distance;
    public static double cost;
    public static int waitingTime;
    public static int mstrWaitingTime;
    public static int nightmstrWaitingTime;
    public static boolean isPaused = false;
    public static String pauseMessage = "";
    public static int screenWidth;
    public static int screenHeight;
    public static SharedPreferences splogin;
    public static SharedPreferences.Editor spediterlogin;
    public static String pusk_key = "";
    public static userModel currentUser;
    public static ArrayList<qna> mylist = new ArrayList<qna>();
    public static String language = "TAMIL";
}
