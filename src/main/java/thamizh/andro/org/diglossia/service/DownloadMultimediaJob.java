package thamizh.andro.org.diglossia.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import thamizh.andro.org.diglossia.data.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by arnald on 3/29/16.
 */
public class DownloadMultimediaJob extends AbstractBackgroundJob {
    private static final String TAG = "DownloadMultimediaJob";
    private static final String MEDIA_FILENAME = "PromoAdvert.mp4";
    
    public static final String ACTION_DOWNLOAD_MULTIMEDIA = "com.axlerate.android.custapplib.service.action.DOWNLOAD_MULTIMEDIA";
    private static final boolean downloading = false;
    public DownloadMultimediaJob(Context context, Intent intent) {
        super(context, intent);
    }

    public static void startActionDownloadMultimedia(Context context) {
        Intent intent = new Intent(context, JobHandlerService.class);
        intent.setAction(ACTION_DOWNLOAD_MULTIMEDIA);
        context.startService(intent);
    }

    public static String getPromoVideoPath(AppConfig config) {
        return config.getPath() + MEDIA_FILENAME;
    }

    private void setupMultimedia() {
        // TODO: Don't know what is happening... we need to refactor this
        String outFileName = getPromoVideoPath(config);
        File f = new File(outFileName);
        Log.i(TAG, "Download - " + outFileName);

        if(!f.exists()){
            Log.i(TAG, "Download not exist - " + outFileName);
            try{
                InputStream myInput;
                myInput = context.getAssets().open(config.getCustomer() + " Promo Advert.mp4");
                OutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[8192];
                int length;
                while ((length = myInput.read(buffer))>0){
                    myOutput.write(buffer, 0, length);
                }

                //Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch(Exception e){
            }
        }
    }

    @Override
    public void run() {
        setupMultimedia();
    }
}
