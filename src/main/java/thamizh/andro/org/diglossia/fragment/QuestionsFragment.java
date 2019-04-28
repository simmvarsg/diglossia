package thamizh.andro.org.diglossia.fragment;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import thamizh.andro.org.diglossia.R;
import thamizh.andro.org.diglossia.data.AppConfig;
import thamizh.andro.org.diglossia.model.qna;
import thamizh.andro.org.diglossia.utils.CommonFunctions;
import thamizh.andro.org.diglossia.utils.TransparentProgressDialog;
import thamizh.andro.org.diglossia.utils.dbhelper;

import static thamizh.andro.org.diglossia.share.Share.mylist;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class QuestionsFragment extends Fragment {
  private OnFragmentInteractionListener mListener;
  private static String TAG ="QuestionsFragement";

  @Bind(R.id.editText2)
  TextView txtTitle;
  @Bind(R.id.question)
  TextView txtQuestion;
  @Bind(R.id.points)
  TextView txtPoints;
  @Bind(R.id.questionGroup)
  RadioGroup questionGroup;
  @Bind(R.id.rightAnswer)
  RadioButton rightAnswer;
  @Bind(R.id.wrongAnswer)
  RadioButton wrongAnswer;
  @Bind(R.id.submitbutton)
  Button submitButton;
  @Bind(R.id.rightImage)
  ImageView rightImageView;
  @Bind(R.id.wrongImage)
  ImageView wrongImageView;
  SQLiteDatabase sdb;
  dbhelper dbh;
  String strVendPath = "";
  AppConfig config;
  int rnd = 0;
  int points = 0;
  qna currQna;
  SharedPreferences savedValues;
  Handler mHandler = new Handler();
  TransparentProgressDialog progressDialog;
  public static Fragment newInstance() {
    QuestionsFragment fragment = new QuestionsFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public QuestionsFragment() {
    // Required empty public constructor
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String tempStr = getActivity().getApplicationContext().getFilesDir().getAbsolutePath();
    if (tempStr.length() > 0){
      strVendPath= tempStr+"/diglossia/";
    }
    else {
      String state = Environment.getExternalStorageState();
      if (state.equals(Environment.MEDIA_MOUNTED) ) {
        tempStr = Environment.getExternalStorageDirectory().getAbsolutePath();
      }
    }
    config = AppConfig.getInstance(getActivity().getApplicationContext());
    config.setPath(strVendPath);
    savedValues = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    points = savedValues.getInt("points",0);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.question_view, container, false);
    ButterKnife.bind(this, view);
    progressDialog = new TransparentProgressDialog(getActivity(),R.drawable.happy);
    txtPoints.setText(String.format("Score - %d", points));
    if(mylist.size() == 0)
      txtQuestion.setText("No questions found");
    else
      nextQuestion();
    return view;
  }

  @OnClick(R.id.submitbutton) void submit() {
    if(questionGroup.getCheckedRadioButtonId() == R.id.rightAnswer || questionGroup.getCheckedRadioButtonId() == R.id.wrongAnswer) {
      String underscores = "";
      for (int i = 0; i < currQna.getQuestion().length(); i++) {
        if (currQna.getQuestion().charAt(i) == '_')
          underscores += "_";
      }
      txtQuestion.setText(currQna.getQuestion().replace(underscores, currQna.getRightans()));
      String answer = "";
      switch (questionGroup.getCheckedRadioButtonId()) {
        case R.id.rightAnswer:
          answer = rightAnswer.getText().toString();
          break;
        case R.id.wrongAnswer:
          answer = wrongAnswer.getText().toString();
          break;
      }
      if (answer.equals(currQna.getRightans())) {
        animate(rightImageView, wrongImageView);
      } else {
        animate(wrongImageView, rightImageView);
        txtTitle.setText(getString(R.string.wrong_answer));
        txtTitle.setTextColor(getResources().getColor(R.color.red));
        questionGroup.setVisibility(View.GONE);
      }
      mHandler.postDelayed(new Runnable() {
        @Override
        public void run() {
          nextQuestion();
        }
      }, 3000);
    }
    else
      CommonFunctions.showAlertDialog(getActivity().getApplicationContext(),"Please select an answer");
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    public void onLogon(String username, String password);
  }
  void nextQuestion(){
    if (mylist.size() > 1) {
      Random random = new Random();
      int tmprnd = rnd;
      while(tmprnd == rnd) {
        tmprnd = random.nextInt(mylist.size());
      }
      rnd = tmprnd;
    }
    currQna = mylist.get(rnd);
    txtQuestion.setText(currQna.getQuestion());
    if(rnd%2 == 0) {
      rightAnswer.setText(currQna.getRightans());
      wrongAnswer.setText(currQna.getWrongans());
    }
    else{
      wrongAnswer.setText(currQna.getRightans());
      rightAnswer.setText(currQna.getWrongans());
    }
    questionGroup.clearCheck();
    rightImageView.setAnimation(null);
    rightImageView.setVisibility(View.GONE);
    wrongImageView.setAnimation(null);
    wrongImageView.setVisibility(View.GONE);
    questionGroup.setVisibility(View.VISIBLE);
    txtTitle.setText(getString(R.string.underline));
    txtTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
  }

  void animate(ImageView rightView, ImageView wrongView){
    txtTitle.setText(getString(R.string.right_answer));
    txtTitle.setTextColor(getResources().getColor(R.color.green));
    points++;
    txtPoints.setText(String.format("%s %d", getString(R.string.points), points));
    questionGroup.setVisibility(View.GONE);
    ScaleAnimation anim = new ScaleAnimation(0.5f, 1.2f, // Start and end values for the X axis scaling
            0.5f, 1.2f, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f);
    anim.setInterpolator(new LinearInterpolator());
    anim.setRepeatCount(Animation.INFINITE);
    anim.setDuration(1500);
    rightView.setVisibility(View.VISIBLE);
    rightView.setAnimation(anim);
    rightView.startAnimation(anim);
    wrongView.setVisibility(View.GONE);
    wrongView.setAnimation(null);
  }

  @Override
  public void onDestroy() {
    SharedPreferences.Editor editor = savedValues.edit();
    editor.putInt("points", points);
    editor.commit();
    super.onDestroy();
  }
}
