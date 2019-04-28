package thamizh.andro.org.diglossia.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import thamizh.andro.org.diglossia.R;
import thamizh.andro.org.diglossia.data.AppConfig;
import thamizh.andro.org.diglossia.model.qna;
import thamizh.andro.org.diglossia.share.Share;
import thamizh.andro.org.diglossia.utils.CommonFunctions;
import thamizh.andro.org.diglossia.utils.TransparentProgressDialog;
import thamizh.andro.org.diglossia.utils.dbhelper;

import static thamizh.andro.org.diglossia.utils.CommonFunctions.showAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionBankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionBankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class QuestionBankFragment extends Fragment {
  private QuestionsFragment.OnFragmentInteractionListener mListener;

  @Bind(R.id.edit_question)
  EditText txtQuestion;
  @Bind(R.id.edit_wrongAnswer)
  EditText wrongAnswer;
  @Bind(R.id.edit_rightAnswer)
  EditText rightAnswer;
  @Bind(R.id.submitbutton)
  Button submitButton;
  SQLiteDatabase sdb;
  String strVendPath = "";
  AppConfig config;
  int rnd = 0;
  int points = 0;
  SharedPreferences savedValues;
  Handler mHandler = new Handler();
  TransparentProgressDialog progressDialog;
  public static Fragment newInstance() {
    QuestionBankFragment fragment = new QuestionBankFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public QuestionBankFragment() {
    // Required empty public constructor
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String state = Environment.getExternalStorageState();
    if (state.equals(Environment.MEDIA_MOUNTED)) {
      String tempStr = Environment.getExternalStorageDirectory().getAbsolutePath();
      //Toast.makeText(taxi.this, state+" : "+tempStr,Toast.LENGTH_SHORT).show();
      tempStr = getActivity().getApplicationContext().getFilesDir().getAbsolutePath();
      if (tempStr.length() > 0){
        strVendPath= tempStr+"/diglossia/";
      }

    }
    else{
      strVendPath = "/mnt/sdcard/external_sd/diglossia/";
    }
    config = AppConfig.getInstance(getActivity().getApplicationContext());
    config.setPath(strVendPath);
    savedValues = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    points = savedValues.getInt("points",0);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.question_admin_view, container, false);
    ButterKnife.bind(this, view);
    progressDialog = new TransparentProgressDialog(getActivity(),R.drawable.happy);
    return view;
  }

  @OnClick(R.id.submitbutton) void submit() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    qna question = new qna(Share.mylist.size()+1,txtQuestion.getText().toString(),rightAnswer.getText().toString(), wrongAnswer.getText().toString(),Share.language);
    /*myRef.child("qna").child(txtQuestion.getText().toString().replace(" ","").replace("_","")).setValue(question)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                txtQuestion.setText("___________");
                rightAnswer.setText("");
                wrongAnswer.setText("");
                Toast.makeText(getActivity().getApplicationContext(), "Updated Successfully",Toast.LENGTH_SHORT).show();
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error Updating "+e.toString(), Toast.LENGTH_SHORT).show();
              }
            }); */
    if(CommonFunctions.updateFireBase(question, getActivity().getApplicationContext())){
      txtQuestion.setText("___________");
      rightAnswer.setText("");
      wrongAnswer.setText("");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

}
