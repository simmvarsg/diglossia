package thamizh.andro.org.diglossia.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import thamizh.andro.org.diglossia.R;
import thamizh.andro.org.diglossia.data.AppConfig;
import thamizh.andro.org.diglossia.fragment.QuestionBankFragment;
import thamizh.andro.org.diglossia.fragment.QuestionsFragment;
import thamizh.andro.org.diglossia.model.qna;
import thamizh.andro.org.diglossia.share.Share;
import thamizh.andro.org.diglossia.utils.CommonFunctions;
import thamizh.andro.org.diglossia.utils.Screen;
import thamizh.andro.org.diglossia.utils.dbhelper;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static thamizh.andro.org.diglossia.share.Share.mylist;
import static thamizh.andro.org.diglossia.utils.CommonFunctions.changeactivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static int RC_SIGN_IN = 100;
    int points = 0;
    SharedPreferences savedValues;
    String strVendPath = "";
    FirebaseAuth mAuth;
    ImageButton btnKurilNedil;
    ImageButton addQuestions;
    SQLiteDatabase sdb;
    dbhelper dbh;
    AppConfig config;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String tempStr = getApplicationContext().getFilesDir().getAbsolutePath();
        if (tempStr.length() > 0){
            strVendPath= tempStr+"/diglossia/";
        }
        else {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED) ) {
                tempStr = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }
        config = AppConfig.getInstance(getApplicationContext());
        config.setPath(strVendPath);
        btnKurilNedil = findViewById(R.id.kurilnedil);
        btnKurilNedil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScreen(Screen.DASHBOARD,false, "Kuril Nedil");
            }
        });
        addQuestions = findViewById(R.id.addquestions);
        addQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScreen(Screen.ADD_QUESTION,false, "ADD Questions");
            }
        });
        mAuth = FirebaseAuth.getInstance();
        savedValues = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        points = savedValues.getInt("points",0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //getQuestions();
        myRef.addValueEventListener(postListener);
    }
    private void switchToScreen(Screen screen, boolean back, String... args) {
        final FragmentTransaction tx =
                getSupportFragmentManager().beginTransaction();
        if (back) {
            tx.addToBackStack("back");
        }
        try {
            switch (screen) {
                case DASHBOARD:
                    tx.replace(R.id.content_main, QuestionsFragment.newInstance());
                    break;
                case ADD_QUESTION:
                    tx.replace(R.id.content_main, QuestionBankFragment.newInstance());
                    break;
            }
            tx.commit();
        }
        catch(Exception e){
            int i = 0; //dummy line
        }
    }
    public void CreateDirectories(){
        try{
            File file = new File(strVendPath);
            if(!file.exists())
                file.mkdir();
        }
        catch(Exception e){
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (id == R.id.nav_camera) {
            if(currentUser == null)
                launchSignInActivity();
        } else if (id == R.id.nav_gallery) {
            if(currentUser != null)
                switchToScreen(Screen.ADD_QUESTION,false, "Question Bank");
            else
                launchSignInActivity();
        } else if (id == R.id.nav_slideshow) {
            if(currentUser != null)
                changeactivity(MainActivity.this,"views.GenericActivityDetailView",false, true);
            else
                launchSignInActivity();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //CommonFunctions.changeactivity(getApplicationContext(), "ui.MainActivity", true,true);
                // ...
            } else {

                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    void launchSignInActivity(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
    public ArrayList<qna> getQuestions() {
        try{
            dbh = new dbhelper(getApplicationContext());
            Date date = new Date();
            SimpleDateFormat sdfDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = sdfDate1.format(date).toString();
            strVendPath = config.getPath();
            sdb = dbh.openFutureDataBase(strVendPath+"diglossia.sqlite");
            // Select All Query
            String selectQuery = "SELECT  serialno, question, rightans, wrongans, language, status from qna";
            Cursor bCursor = sdb.rawQuery(selectQuery, null);
            int i = 0;
            if (bCursor != null) {
                bCursor.moveToFirst();
                while (bCursor.isAfterLast() == false ) {
                    qna tmpqna = new qna();
                    tmpqna.setSerialno(bCursor.getInt(0));
                    tmpqna.setQuestion(bCursor.getString(1));
                    tmpqna.setRightans(bCursor.getString(2));
                    tmpqna.setWrongans(bCursor.getString(3));
                    tmpqna.setLanguage(bCursor.getString(4));
                    if(bCursor.getInt(5) == 1)
                        tmpqna.setStatus(true);
                    /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    myRef.child("qna").child(tmpqna.getQuestion().replace(" ","").replace("_","")).setValue(tmpqna)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Updated Successfully",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error Updating "+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }); */
                    CommonFunctions.updateFireBase(tmpqna,getApplicationContext());
                    //mylist.add(tmpqna);
                    bCursor.moveToNext();
                }
            }
            sdb.close();
        }
        catch(Exception e){
            e.printStackTrace();
            //displayAlert("Error","Error reading area Names", getActivity().getApplicationContext());
        }
        myRef.addValueEventListener(postListener);
        return mylist;
    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            dbh = new dbhelper(getApplicationContext());
            sdb = dbh.openFutureDataBase(strVendPath + "diglossia.sqlite");
            for (DataSnapshot data : dataSnapshot.child("qna").getChildren()) {
                int status = 0;
                qna tmpqna = (qna)data.getValue(qna.class);
                if(tmpqna.isStatus())
                    status =1;
                if(tmpqna.getQuestion() != null) {
                    String query = String.format("insert into qna (question, rightans, wrongans, enteredby, status) values('%s','%s','%s','%s', %d)", tmpqna.getQuestion(), tmpqna.getRightans(), tmpqna.getWrongans(), "System", status);
                    //sdb.execSQL(query);
                    if(mylist.indexOf(tmpqna) < 0)
                        mylist.add(tmpqna);
                }
            }
            sdb.close();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            // ...
        }
    };
}
