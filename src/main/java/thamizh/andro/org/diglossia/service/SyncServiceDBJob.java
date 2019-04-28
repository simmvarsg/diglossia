package thamizh.andro.org.diglossia.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import thamizh.andro.org.diglossia.net.SimpleQueue;
import thamizh.andro.org.diglossia.utils.Constants;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by arnald on 3/29/16.
 */
public class SyncServiceDBJob extends AbstractBackgroundJob {
    private static final String TAG = "SyncServiceDBJob";
    private static final String DATABASE_NAME = "diglossia.sqlite";
    private static final String EXTRA_CUSTOMER = "com.axlerate.android.custapplib.service.extra.customer";
    private static final String EXTRA_REGION = "com.axlerate.android.custapplib.service.extra.region";

    public static final String ACTION_SYNC_DATABASE = "com.axlerate.android.custapplib.service.action.SYNC_WORKSHOP";
    private static final boolean downloading = false;

    public static void startActionSyncDatabase(Context context) {
        Intent intent = new Intent(context, JobHandlerService.class);
        intent.setAction(ACTION_SYNC_DATABASE);
        context.startService(intent);
    }

    public SyncServiceDBJob(Context context, Intent intent) {
        super(context, intent);
    }

    private int bufferLength = 4096;
    private String postDataURL="http://www.terratech.com.sg/track/uat";
    private String SQSIncomingURL;
    private String SQSDebugURL;
    private String pendingVideos ="";
    private AmazonS3Client s3Client = new AmazonS3Client(
            new BasicAWSCredentials(Constants.ACCESS_KEY_ID,
                    Constants.SECRET_KEY));

    @Override
    public void run() {
        try {
            s3Client.setEndpoint(config.getRegion());
            downloadDatabase();
        } catch(Exception e){
            Log.e(TAG, "Cannot start the download database service");
        }
    }

    private void downloadDatabase() {
        String customer = config.getCustomer();
        String filename = config.getPath() + DATABASE_NAME;

        try {
            File outputFile = new File(filename.replace(".sqlite",".tmp"));
            File outputFileFinal = new File(filename);
            if(!outputFile.exists()) {
                GetObjectRequest request = new GetObjectRequest(customer.toLowerCase().replace(" ", ""), customer.toLowerCase().replace(" ", "") + "_availableWorkshop.sqlite");
                S3Object response = s3Client.getObject(request);
                InputStream reader = new BufferedInputStream(
                        response.getObjectContent());
                File file = new File(filename.replace(".sqlite", ".tmp"));
                OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

                int read = -1;
                byte[] buffer = new byte[bufferLength];
                while ((read = reader.read(buffer, 0, bufferLength)) != -1) {
                    writer.write(buffer, 0, read);
                }

                writer.flush();
                writer.close();
                reader.close();
                outputFile.renameTo(outputFileFinal); // FIXME: We can't do this here, it will impact open database connection
            }
        } catch (Exception exception) {
            pendingVideos = exception.toString();
        }

        if (pendingVideos.length() > 3) {
            SimpleQueue.sendMessage(SQSIncomingURL, pendingVideos);
        }
        pendingVideos = "";
    }
}
