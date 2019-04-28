package thamizh.andro.org.diglossia.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import thamizh.andro.org.diglossia.data.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class dbhelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    //private static String DB_PATH = "/data/data/fiz.andr.app.touch/databases/";
	public String strVendPath;

 
    private static String DB_NAME = "diglossia.sqlite";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 	AppConfig config;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public dbhelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
		config = AppConfig.getInstance(context);
		strVendPath = config.getPath();
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase(String myPath) throws IOException{
 
    	boolean dbExist = checkDataBase(myPath);
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase(myPath);
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
	public void createDataBase() throws IOException{
		String myPath = strVendPath + DB_NAME;
		boolean dbExist = checkDataBase(myPath);

		if(dbExist){
			//do nothing - database already exist
		}else{

			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase(myPath);

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(String myPath){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		//String myPath = strVendPath + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase(String myPath) throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(myPath.replace(strVendPath,""));
    	File dataDirectory = new File(strVendPath); 
		// have the object build the directory structure, if needed. 
    	boolean dirresult = true;
		if(!dataDirectory.exists())			
			dirresult = dataDirectory.mkdir();
		if (dirresult){
	    	// Path to the just created empty db
	    	//String outFileName = DB_PATH + DB_NAME;
	    	String outFileName = myPath;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
		}
 
    }
 
    public SQLiteDatabase openDataBase() throws SQLException{
 
    	//Open the database
    	//DB_PATH =strDbPath;
        String myPath = strVendPath + DB_NAME;
        try{
			if(checkDataBase(myPath)) {
				myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
				return myDataBase;
			}
			else{
				copyDataBase(myPath);
				myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
				return myDataBase;
			}
        }
		catch(IOException e){
			return null;
		}
        catch(SQLiteException e){
        	return null;
        }
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 	}
    
    public Cursor getEntries () {
    	return myDataBase.rawQuery ("Select * from OutforDelivery",null );
    	}

    public Cursor getEntries (String strDate) {
    	String[] strArr= new String[1];
    	strArr[0]=strDate;
    	return myDataBase.rawQuery ("Select * from OutforDelivery where Today = ?",strArr );
    	}
    
    public Cursor getEntriesforStatus (String strStatus) {
    	String[] strArr= new String[1];
    	strArr[0]=strStatus;
    	//N-delivery New ; F- delivery Failed S-Delivery Success
    	return myDataBase.rawQuery ("Select * from OutforDelivery where Status IN(?)",strArr );
    	}    

    public int SaveData (ContentValues cv, String AWBnr) {
    	return myDataBase.update("OutforDelivery", cv, "HAWB=?", new String[] {AWBnr});
    	}
    public Cursor getAddress(String Hawb) {
    	String[] strArr= new String[1];
    	strArr[0]=Hawb;
    	return myDataBase.rawQuery ("Select Name, ifNull(Add1,'No Address') address from OutforDelivery where Hawb ='"+Hawb+"'",null );	
  }

	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
    public SQLiteDatabase openFutureDataBase(String myPath) throws SQLException{

		try{
			if(checkDataBase(myPath)) {
				myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
				return myDataBase;
			}
			else{
				copyDataBase(myPath);
				myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
				return myDataBase;
			}
		}
		catch(IOException e){
			return null;
		}
		catch(SQLiteException e){
			return null;
		}
    }
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
}