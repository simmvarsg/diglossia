package thamizh.andro.org.diglossia.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import thamizh.andro.org.diglossia.net.SimpleQueue;
import thamizh.andro.org.diglossia.utils.Constants;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class AudioDownloadService extends Service{
	
	public static OutputStream MsgWriter;
	public static boolean isreceive = false;
	private InputStream btInput = null;
    private android.location.Location prevLoc;
    private android.location.Location currLoc;
    private android.location.Location networkLoc;
    private android.location.Location gpsLoc;
    private LocationManager locationMgr;
    private String debugInfo; 
    private String vehicleid;
    private String [] videoList;
    private String pendingVideos ="";
    private boolean downloading = false;
    private String postDataURL="http://www.terratech.com.sg/track/uat";
    private String videoURL = "http://www.terratech.com.sg/TaxiMeter/VRTrack/Videos/";
    private int updateInterval = 5000;
    private int i = 0;
    private File filesDir;
    private String[] filesList;
    private String strVendPath;
    private String SQSIncomingURL;
    private String SQSDebugURL;
    private String uatprod = "_prod";
    private String videoString;
    private String messageHandle;
    private String customer = "";
    private String City = "";
    protected  List<String> queueIncomingArray;
	private AmazonS3Client s3Client = new AmazonS3Client(
			new BasicAWSCredentials(Constants.ACCESS_KEY_ID,
					Constants.SECRET_KEY)); 
	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	public static final int STATE_CONNECTED_FAILED = 4;  
	public static final int STATE_CONNECTED_ANALDATA = 5;  
	public static final int STATE_CONNECTED_DOSUCCESS = 6; 
	public static final int STATE_CONNECTED_SERVER = 7; 
	public static final int STATE_CANCEL_POS = 8; 
	public static final int STATE_SHOW_CARDINFO = 9;
	public static final int STATE_SHOW_PENDING = 10;
	public static final int STATE_SHOW_PWD = 11;
	private Handler mHandler = new Handler();
    public class OnlyExt implements FilenameFilter { 
    	String ext1; 
    	String ext2;
    	String ext3; 
    	String ext4;
    	public OnlyExt(String ext1, String ext2,String ext3, String ext4) { 
    	this.ext1 = "." + ext1; 
    	this.ext2 = "." + ext2; 
    	this.ext3 = "." + ext3; 
    	this.ext4 = "." + ext4; 
    	} 
    	public boolean accept(File dir, String name) { 
    		if (name.endsWith(ext1))
    			return name.endsWith(ext1); 
    		else if (name.endsWith(ext2))
    			return name.endsWith(ext2);
    		else if (name.endsWith(ext3))
    			return name.endsWith(ext3);
    		else
    			return name.endsWith(ext4);
    	} 
}
	private Runnable updateRunnable = new Runnable() {     
		@Override 
		public void run() {         
			if(isNetworkAvailable())
				queueRunnable(); 
			//mHandler.postDelayed(updateRunnable,300000);
			} 
		}; 
		private void queueRunnable() {     
			if(!downloading){
				new DownloadVideos().execute(this);
			}
	}
	public void onCreate(Bundle savedInstanceState) {
		
	}
	
	
	public void stopReceiveThread(){
		isreceive = false;
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(AudioDownloadService.this,"Service Destroyed...",Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	
	public  void onStart(Intent intent, int startId) {	
		writeVideoLog("onStart");
		String state = Environment.getExternalStorageState();
		try{
			if(intent != null){
				customer = intent.getStringExtra("customer");
				City = intent.getStringExtra("City");
			}
			if (state.equals(Environment.MEDIA_MOUNTED)) {
	        	String tempStr = Environment.getExternalStorageDirectory().getAbsolutePath();
	        	//Toast.makeText(taxi.this, state+" : "+tempStr,Toast.LENGTH_SHORT).show();
	        	if (tempStr.length() > 0){
	        		strVendPath= tempStr+"/Vendtaintment/";
	        	}
	    
	        }
	        else{
	        	strVendPath = "/mnt/sdcard/external_sd/Vendtaintment/";
	        }
		
			try{
				vehicleid = intent.getStringExtra("vehicleid");
				postDataURL = intent.getStringExtra("postDataURL");
				videoString = intent.getStringExtra("videoString");
				messageHandle = intent.getStringExtra("messageHandle");
	
			}
			catch(Exception e){
				vehicleid = "";
			    postDataURL="http://www.terratech.com.sg/track/uat";
			    videoURL = "http://www.terratech.com.sg/TaxiMeter/VRTrack/Videos/";
			    videoString = "";
			}
			writeVideoLog(vehicleid+";"+postDataURL);
				if(postDataURL.contains("uat"))
					uatprod = "_uat";
				try{
					SQSIncomingURL = new URL("https://sqs.us-east-1.amazonaws.com/711643634223/"+vehicleid.replace(" " ,"")+uatprod).toString();
					SQSDebugURL = new URL("https://sqs.us-east-1.amazonaws.com/711643634223/debug"+uatprod).toString();
					if(messageHandle != null){
						DeleteMessageRequest request = new DeleteMessageRequest();
						request.setQueueUrl(SQSIncomingURL);
						request.setReceiptHandle(messageHandle);
						SimpleQueue.getInstance().deleteMessage(request);
					}
				}
				catch(MalformedURLException e){
					Toast.makeText(AudioDownloadService.this, e.toString(),Toast.LENGTH_SHORT).show();
				}
				catch(AmazonServiceException e){
					
				}
				catch(AmazonClientException e){
					
				}
				if(videoString != null && videoString.contains("DV;")){
					videoList = videoString.split(";");
					new DownloadVideos().execute(AudioDownloadService.this);
				}
				else if(videoString != null && videoString.contains("RV;")){
					videoList = videoString.split(";");
					RemoveVideos();
				}
			if(!downloading){
				new DownloadVideos().execute(this);
			}
			Toast.makeText(AudioDownloadService.this, "Downloading Videos.. ",Toast.LENGTH_SHORT).show();
		}
		catch(Exception e){
			Toast.makeText(AudioDownloadService.this, "Cannot start the video download service.. ",Toast.LENGTH_SHORT).show();
		}
		//mHandler.postDelayed(updateRunnable, 300000);
		super.onStart(intent, startId);
	}
	@Override    public int onStartCommand(Intent intent, int flags, int startId) {        
		//Log.i("LocalService", "Received start id " + startId + ": " + intent);        
		// We want this service to continue running until it is explicitly        
		// stopped, so return sticky. 

		onStart(intent, startId);
		return START_STICKY;    
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
		AudioDownloadService getService() {
			return AudioDownloadService.this;
		}
	}
	
	private final IBinder mBinder = new LocalBinder();
	

	

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
	}
	



	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//Toast.makeText(VideoDownloadService.this, "Scaning...",Toast.LENGTH_SHORT).show();
			String action = intent.getAction();
			
		//	Bundle b = intent.getExtras();
			
			

				sendMsg(1);

		}
	};


	protected boolean isNetwork3G() {     
		ConnectivityManager connectivityManager            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);     
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();  
		if(activeNetworkInfo.getType() ==ConnectivityManager.TYPE_WIFI)
			return true;
		TelephonyManager teleMan =  
            (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = teleMan.getNetworkType();
		boolean threeGabove=false;
		writeVideoLog(Integer.toString(networkType));
		switch (networkType)
		{
			case TelephonyManager.NETWORK_TYPE_CDMA:
			    threeGabove = true;
			    break;      
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				threeGabove = true;
			    break;          
			case TelephonyManager.NETWORK_TYPE_HSPA:
				threeGabove = true;
			    break;          
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				threeGabove = true;
			    break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				threeGabove = true;
				break;
			case TelephonyManager.NETWORK_TYPE_IDEN:
				threeGabove = true;
				break;
			default:
				threeGabove = true;
				break;
		}
		return threeGabove; 
	} 
	protected boolean isNetworkAvailable() {     
		ConnectivityManager connectivityManager            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);     
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();  
		//Toast.makeText(VideoDownloadService.this, Boolean.toString(activeNetworkInfo != null),Toast.LENGTH_SHORT).show();
		return activeNetworkInfo != null; 
	} 
    protected class DownloadVideos extends AsyncTask<Context, Void, String> {
    	@Override
    	protected String doInBackground(Context... params) {
	    	int i = 1;
	    	String failedVideos = "";
    		pendingVideos = "DV;";
    		downloading = true;
    		/*for (i = 1;i < videoList.length;i++){
    			pendingVideos += videoList[i]+";";
    		}
    		writeVideoLog("Before"+pendingVideos);
    		String tempURL = "";
    		
    		for (i = 1;i < videoList.length;i++){*/
				if(isNetworkAvailable() && isNetwork3G()){
    				try{	
    					/*writeVideoLog("Downloading "+videoList[i]);
    					tempURL = videoURL+videoList[i].replace(" ", "%20");
		    			URL url = new URL(tempURL);             
		    			HttpURLConnection c = (HttpURLConnection) url.openConnection();      
		    			c.setRequestMethod("GET");  
	    				if(!android.os.Build.VERSION.RELEASE.contains("4."))
	    					c.setDoOutput(true);            
		    			c.connect();              
		    			File file = new File(strVendPath);             
		    			file.mkdirs();             
		    			File outputFile = new File(file, videoList[i]);             
		    			FileOutputStream fos = new FileOutputStream(outputFile);              
		    			InputStream is = c.getInputStream();              
		    			byte[] buffer = new byte[512];             
		    			int len1 = 0;             
		    			while ((len1 = is.read(buffer)) != -1) {                 
		    				fos.write(buffer, 0, len1);             
		    			}             
		    			fos.close();             
		    			is.close();//till here, it works fine - .apk is download to my sdcard in download file
    					//String localDir = Environment.getExternalStorageDirectory() + "/Android/data/m3iVideos";
    					String localfileName= strVendPath;
    					File f = new File(strVendPath);
    					if(!f.isDirectory()) {
    						f.mkdir(); 
    					}*/    				
    					try {
    						// Content type is determined by file extension.

    			        	ObjectListing objects = s3Client.listObjects(customer.replace(" ","").toLowerCase()+City.replace(" ","").toLowerCase());
    			        	List<S3ObjectSummary> list = objects.getObjectSummaries();
    			        	for(S3ObjectSummary image: list) {
    			        		String fileext = image.getKey();
    			        		File tmpfile = new File(strVendPath+fileext);
	    			        	if((fileext.contains(".mp3") || fileext.contains(".aac") || fileext.contains(".wma")) && !tmpfile.exists()){
	    							GetObjectRequest request = new GetObjectRequest(image.getBucketName(), image.getKey());
	    							S3Object response = s3Client.getObject(request);
	    							InputStream reader = new BufferedInputStream(response.getObjectContent());
									//File file = new File();
									File outputFile = new File(strVendPath+image.getKey()+".tmp");
									OutputStream writer = new BufferedOutputStream(new FileOutputStream(outputFile));
									int read = -1;
									while (isNetworkAvailable() && ( read = reader.read() ) != -1 ) {
									    writer.write(read);
									}
									writer.flush();
									writer.close();
									reader.close();
									String threegpfilename = fileext.substring(0,fileext.indexOf("."))+".mp3";
					    			File outputFilemp3 = new File(strVendPath,threegpfilename);
					    			Boolean result = outputFile.renameTo(outputFilemp3);
					    			//pendingVideos = pendingVideos.replaceAll(videoList[i]+";", "");
    			        		}
    			        	}
    			        } catch (Exception exception) {
    			        	SimpleQueue.sendMessage(SQSDebugURL, vehicleid+":"+exception.toString());
    			        }
		    		}
	    			/*catch (IOException e) {   
		    			pendingVideos = pendingVideos.replaceAll(videoList[i]+";", "");
		    			File file = new File(strVendPath);                     
		    			File outputFile = new File(file, videoList[i]);   
		    			outputFile.delete();
		    			failedVideos += videoList[i]+":"+tempURL+":"+e.toString();
		    			SimpleQueue.sendMessage(SQSDebugURL, vehicleid+":"+e.toString());
		    		} */  
		    		catch(Exception e){
		    			/*pendingVideos = pendingVideos.replaceAll(videoList[i]+";", "");
		    			File file = new File(strVendPath);                     
		    			File outputFile = new File(file, videoList[i]);   
		    			outputFile.delete();
		    			failedVideos += videoList[i]+":"+tempURL+":"+e.toString();*/
		    			SimpleQueue.sendMessage(SQSDebugURL, vehicleid+":"+e.toString());
		    		}
    			}
	    	//}
    		writeVideoLog("After"+pendingVideos);
	    	return pendingVideos;
	    }
    	protected void onPostExecute(String result) 
    	{         
    		super.onPostExecute(result);
    		if(result.length() > 3)
    			SimpleQueue.sendMessage(SQSIncomingURL, result);
	    	pendingVideos="";
	    	downloading = false;
	    	stopSelf();
    	}
    }
	    private void RemoveVideos() {
	    		pendingVideos = "RV;";
	    		String tempURL = "";
	    		String failedVideos = "";
	    		if(videoList[1].contains("ALL")){
	    			FilenameFilter filefilter = new OnlyExt("wmv","mp4","mkv","3gp");         	
	    	    	filesDir = new File(strVendPath);
	    	    	videoList = filesDir.list(filefilter);
	    	    	Arrays.sort(videoList);
	    		}
	    		for (int i = 0;i < videoList.length;i++){
	    			pendingVideos += videoList[i]+";";
	    		}
	    		pendingVideos = pendingVideos.substring(0,pendingVideos.length()-1);
	    		for (int i = 0;i < videoList.length;i++){
	    		try{
	            	File file = new File(strVendPath+videoList[i]); 
	            	boolean deleted = file.delete();
	            	pendingVideos = pendingVideos.replaceAll(videoList[i]+";", "");
	            }
	            catch (Exception e){
	    			pendingVideos = pendingVideos.replaceAll(videoList[i]+";", "");
	    			File file = new File(strVendPath);                     
	    			File outputFile = new File(file, videoList[i]);   
	    			outputFile.delete();
	    			failedVideos += videoList[i]+":"+tempURL+":"+e.toString();
	    			SimpleQueue.sendMessage(SQSDebugURL, vehicleid+":"+e.toString());
	            }
	    	}
	    		if(pendingVideos.length() > 3)
	    			SimpleQueue.sendMessage(SQSIncomingURL, pendingVideos);
	    	pendingVideos="";    
	    	stopSelf();
	    }

    	private void writeVideoLog(String data){
    		Date date = new Date(); 
    		SimpleDateFormat sdfDate = new SimpleDateFormat ("ddMMyyyy"); 
    		SimpleDateFormat sdfTime= new SimpleDateFormat ("HH:mm:ss:SS"); 
    		File root = new File(strVendPath+"Data");
            File file = new File(root, "VideoDowloadLog"+sdfDate.format(date)+".txt");
            try {
            	if (root.canWrite())
            	{
                	FileWriter filewriter = new FileWriter(file,true);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(sdfTime.format(date)+":\n"+data + "\n");
                    out.close();
                }
            } catch (IOException e) {

            }

    	}


}
