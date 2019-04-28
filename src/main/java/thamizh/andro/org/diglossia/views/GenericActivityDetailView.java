package thamizh.andro.org.diglossia.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

import thamizh.andro.org.diglossia.R;
import thamizh.andro.org.diglossia.adapters.QuestionsListViewAdapter;
import thamizh.andro.org.diglossia.model.qna;
import thamizh.andro.org.diglossia.share.Share;
import thamizh.andro.org.diglossia.utils.CommonFunctions;

/**
 * Created by Varad on 04/09/2017.
 * This is the primary view for Cashflow sTransactionCompleted with three tabs to show Income, Expenses and Combined views
 */

public class GenericActivityDetailView extends AppCompatActivity {
    private String mActivitytype = "";
    private ListView mExpListView;
    private ActionBar mActionBar;
    private TextView mTitleTextView;
    private CheckBox mShowAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_activity_detail_listview);
        initialiseComponents();
        mExpListView.setAdapter(getQuestions(GenericActivityDetailView.this,Share.mylist));
    }
    void initialiseComponents(){
        mExpListView = findViewById(R.id.lstView);
        mShowAll = findViewById(R.id.chkShowAll);
        mShowAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mExpListView.setAdapter(getQuestions(getApplicationContext(),Share.mylist));
            }
        });
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.details_title_layout, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Pending Questions");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
    public QuestionsListViewAdapter getQuestions(final Context context, List<qna> qnaList){
        QuestionsListViewAdapter QuestionListAdapter = new QuestionsListViewAdapter(context);
        try {
            for (int i = 0; i < qnaList.size(); i++) {
                qna question = qnaList.get(i);
                if(question.isStatus() == mShowAll.isChecked()) {
                    String groupHeader = String.format("%s", question.getQuestion());
                    QuestionListAdapter.addSectionHeaderItem(groupHeader);
                    QuestionListAdapter.addItem(question);
                    mExpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final qna question = (qna)adapterView.getAdapter().getItem(i);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context,
                                    AlertDialog.THEME_HOLO_LIGHT);

                            builder.setTitle(context.getResources().getString(R.string.AppName));
                            builder.setMessage("Do you want to Approve?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    question.setStatus(true);
                                    Share.mylist.get(Share.mylist.indexOf(question)).setStatus(true);
                                    CommonFunctions.updateFireBase(question,getApplicationContext());
                                    mExpListView.setAdapter(getQuestions(GenericActivityDetailView.this,Share.mylist));
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }

                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    });
                }
            }
        }
        catch(Exception e){

        }
        return QuestionListAdapter;
    }
}
