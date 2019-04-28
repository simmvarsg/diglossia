package thamizh.andro.org.diglossia.views;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import thamizh.andro.org.diglossia.R;
import thamizh.andro.org.diglossia.data.AppConfig;
import thamizh.andro.org.diglossia.share.Share;
import thamizh.andro.org.diglossia.utils.CommonFunctions;
import thamizh.andro.org.diglossia.utils.TransparentProgressDialog;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


public class SplashActivity extends Activity{
	private Context context;


	private SharedPreferences sharedPreferencess;

	private Handler guiThread;

	private Runnable updateTask;

	AppConfig appConfig;

	public static final String PROPERTY_REG_ID = "registration_id";

	private static final String PROPERTY_APP_VERSION = "appVersion";

	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";

	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;

	public static String SENDER_ID = "560139270578";

	private int counter = 1;

	private String regid;



	// TODO : Font Proxima

	public static Typeface Proxima_Bold;

	public static Typeface Proxima_SemiBold;

	public static Typeface Proxima_Regular;



	LocationManager locationManager ;

	String provider;

	private TransparentProgressDialog mProgressDialog;



	private String TAG = SplashActivity.class.getSimpleName();

	@SuppressLint("SimpleDateFormat")

	@Override

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appConfig = AppConfig.getInstance(getApplicationContext());
		setContentView(R.layout.activity_splash);
		this.initComponents();
		call();
	}



	@Override

	protected void onResume() {

		super.onResume();


		// TODO Shared Demo

		SharedPreferences sp = context.getSharedPreferences("APP", 0);

		SharedPreferences.Editor  editr= sp.edit();



	}

	@SuppressWarnings("deprecation")

	private void initComponents() {

		try {

			Share.screenWidth = getWindowManager().getDefaultDisplay().getWidth();

			Share.screenHeight = getWindowManager().getDefaultDisplay().getHeight();

			context = SplashActivity.this;

			sharedPreferencess = PreferenceManager.getDefaultSharedPreferences(this);

			Share.splogin = context.getSharedPreferences("Login", 0);

			//Share.spediterlogin = Share.splogin.edit();

//			Share.locale = context.getResources().getConfiguration().locale.getDisplayCountry();



			//Proxima_Bold = Typeface.createFromAsset(getAssets(), "ProximaNova-Bold.otf");

			//Proxima_SemiBold = Typeface.createFromAsset(getAssets(), "ProximaNova-Semibold.otf");

			//Proxima_Regular = Typeface.createFromAsset(getAssets(), "ProximaNova-Regular.otf");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}



	public void call() {

			SharedPreferences.Editor editor = sharedPreferencess.edit();

			editor.putString("GCMRegID", regid);

			editor.commit();

			Log.e(TAG, "Register ID :" + regid);



			initThreading();

			guiThread.postDelayed(updateTask, 1000);

	}



	private String getRegistrationId(Context context) {

		final SharedPreferences prefs = getGCMPreferences(context);

		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		if (registrationId.length() == 0) {

			Log.v(TAG, "Registration not found.");

			return "";

		}

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,Integer.MIN_VALUE);

		int currentVersion = getAppVersion(context);

		if (registeredVersion != currentVersion || isRegistrationExpired()) {

			Log.v(TAG, "App version changed or registration expired.");

			return "";

		}

		return registrationId;

	}



	private SharedPreferences getGCMPreferences(Context context) {

		return getSharedPreferences(SplashActivity.class.getSimpleName(),

				Context.MODE_PRIVATE);

	}



	private static int getAppVersion(Context context) {

		try {

			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

			return packageInfo.versionCode;

		} catch (NameNotFoundException e) {

			// should never happen

			throw new RuntimeException("Could not get package name: " + e);

		}

	}



	private boolean isRegistrationExpired() {

		final SharedPreferences prefs = getGCMPreferences(context);

		// checks if the information is not stale

		long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME,-1);

		return System.currentTimeMillis() > expirationTime;

	}



	private void setRegistrationId(Context context, String regId) {

		final SharedPreferences prefs = getGCMPreferences(context);

		int appVersion = getAppVersion(context);

		Log.v(TAG, "Saving regId on app version " + appVersion);

		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(PROPERTY_REG_ID, regId);

		editor.putInt(PROPERTY_APP_VERSION, appVersion);

		long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;



		Log.v(TAG, "Setting registration expiry time to " + new Timestamp(expirationTime));

		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);

		editor.commit();

	}



	private void initThreading() {

		guiThread = new Handler();

		updateTask = new Runnable() {

			public void run() {

				changeImage();

			}

		};

	}



	private void changeImage() {

		counter = counter + 1;

		if (counter == 2) {



			runOnUiThread(new Runnable() {

				@Override

				public void run() {

				}

			});
			if (Share.splogin.getBoolean("firsttime", true)) {
				CommonFunctions.changeactivity(context, "views.ShowOnce", true,true);
				SharedPreferences.Editor  editr = Share.splogin.edit();
				editr.putBoolean("firsttime", false);
				editr.commit();
			}
			else {

				Share.pusk_key = regid;
				CommonFunctions.changeactivity(context, "views.LoginActivity", true,true);

			}
		}

	}

	protected void hideDialog() {

		if (mProgressDialog != null && mProgressDialog.isShowing()) {

			mProgressDialog.dismiss();

		}

	}



	protected void showProgressDialog()

	{

		if(mProgressDialog == null)

			mProgressDialog = new TransparentProgressDialog(SplashActivity.this, R.drawable.logoicon);;

		if(!mProgressDialog.isShowing())

			mProgressDialog.show();

	}

}