package thamizh.andro.org.diglossia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import thamizh.andro.org.diglossia.R;
import thamizh.andro.org.diglossia.model.qna;

/**
 * Created by Varad on 04/16/2017.
 */


public class QuestionsListViewAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> mSectionHeader = new TreeSet<Integer>();
    private ArrayList<qna> mQuestions = new ArrayList<qna>();
    private LayoutInflater mInflater;
    private Context _context;
    public QuestionsListViewAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _context = context;
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }
    public void addItem(final qna question) {
        mQuestions.add(question);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        mSectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public qna getItem(int position) {
        return mQuestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);

        switch (rowType) {
                case TYPE_ITEM:
                    if(position > mSectionHeader.size())
                        position -= mSectionHeader.size();
                    else
                        position -= 1;
                    ViewHolderItem holder = null;
                    if (convertView == null) {
                        holder = new ViewHolderItem();
                        convertView = mInflater.inflate(R.layout.questions_details_list_item, null);
                        holder.lblQuestion = (TextView) convertView.findViewById(R.id.lblQuestion);
                        holder.lblRightAnswer = (TextView) convertView.findViewById(R.id.lblRightAnswer);
                        holder.lblWrongAnswer = (TextView) convertView.findViewById(R.id.lblWrongAnswer);
                        holder.isApproval = (CheckBox) convertView.findViewById(R.id.chkApproval);
                    }
                    else
                        holder = (ViewHolderItem)convertView.getTag();
                    if(holder != null) {
                        holder.lblQuestion.setText(mQuestions.get(position).getQuestion());
                        holder.lblRightAnswer.setText(mQuestions.get(position).getRightans());
                        holder.lblWrongAnswer.setText(mQuestions.get(position).getWrongans());
                        holder.isApproval.setChecked(mQuestions.get(position).isStatus());
                    }
                    convertView.setTag(holder);
                    break;
                case TYPE_SEPARATOR:
                    ViewHolderGroup holderg = null;
                    if (convertView == null) {
                        holderg = new ViewHolderGroup();
                        convertView = mInflater.inflate(R.layout.questions_list_group, null);
                        holderg.lblQuestion = (TextView) convertView.findViewById(R.id.lblQuestion);
                        holderg.lblRightAnswer = (TextView) convertView.findViewById(R.id.lblRightAnswer);
                        holderg.lblWrongAnswer = (TextView) convertView.findViewById(R.id.lblWrongAnswer);
                    }
                    else
                        holderg = (ViewHolderGroup)convertView.getTag();
                    if(holderg != null) {
                        holderg.lblQuestion.setText(mQuestions.get(position).getQuestion());
                        holderg.lblRightAnswer.setText(mQuestions.get(position).getRightans());
                        holderg.lblWrongAnswer.setText(mQuestions.get(position).getWrongans());
                    }
                    convertView.setTag(holderg);
                    break;
            }
        return convertView;
    }

    public static class ViewHolderGroup {
        public TextView lblQuestion;
        public TextView lblRightAnswer;
        public TextView lblWrongAnswer;
    }
    public static class ViewHolderItem {
        public TextView lblQuestion;
        public TextView lblRightAnswer;
        public TextView lblWrongAnswer;
        public CheckBox isApproval;
    }

}