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


public class QuestionsApprovalListViewAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private ArrayList<qna> mQuestions = new ArrayList<qna>();
    private LayoutInflater mInflater;
    private Context _context;
    public QuestionsApprovalListViewAdapter(Context context) {
        _context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }
    public void addItem(final qna item) {
        mQuestions.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
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
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);

        switch (rowType) {
                case TYPE_ITEM:
                    ViewHolderItem holder = null;
                    if (convertView == null) {
                        holder = new ViewHolderItem();
                        convertView = mInflater.inflate(R.layout.questions_details_list_item, null);
                        holder.lblQuestion = (TextView) convertView.findViewById(R.id.lblQuestion);
                        holder.lblRightAnswer = (TextView) convertView.findViewById(R.id.lblRightAnswer);
                        holder.lblWrongAnswer = (TextView) convertView.findViewById(R.id.lblRightAnswer);
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
                    holderg = new ViewHolderGroup();
                    convertView.setTag(holderg);
                    break;
            }
        return convertView;
    }

    public static class ViewHolderGroup {

    }
    public static class ViewHolderItem {
        public TextView lblQuestion;
        public TextView lblRightAnswer;
        public TextView lblWrongAnswer;
        public CheckBox isApproval;
    }

}