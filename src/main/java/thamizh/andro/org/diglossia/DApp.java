package thamizh.andro.org.diglossia;

import android.app.Application;
import android.content.Context;
import com.google.firebase.FirebaseApp;

public class DApp extends Application {
    private static Context context;
    public static final long serialVersionUID = 1831382741031374244L;

    public DApp() {
    }

    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
            this.startBackgroundJobs();
            FirebaseApp.initializeApp(this);
    }


    private void startBackgroundJobs() {

    }
}