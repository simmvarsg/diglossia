package thamizh.andro.org.diglossia.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class JobHandlerService extends IntentService {
    private static final String TAG = "JobHandlerService";
    public JobHandlerService() {
        super("JobHandlerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            runJob(intent);
        }
    }

    private void runJob(Intent intent) {
        final String action = intent.getAction();

        Log.i(TAG, "Running -> " + action);

        if (DownloadMultimediaJob.ACTION_DOWNLOAD_MULTIMEDIA.equals(action)) {
            new DownloadMultimediaJob(this, intent).run();
        }
        else if (SyncServiceDBJob.ACTION_SYNC_DATABASE.equals(action)) {
            new SyncServiceDBJob(this, intent).run();
        }
    }
}
