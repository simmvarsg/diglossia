package thamizh.andro.org.diglossia.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Created by Varad on 12/24/2016.
 */
public class AutoCompleteLoading extends AutoCompleteTextView {

    private ProgressBar mLoadingIndicator;
    private Button mCancelButton;
    public int totalcount;
    public AutoCompleteLoading(Context context) {
        super(context);
        totalcount = 0;
    }


    public AutoCompleteLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        totalcount = 0;
    }

    public void setLoadingIndicator(ProgressBar view, Button button) {
        mLoadingIndicator = view;
        mCancelButton = button;
    }


    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        // the AutoCompleteTextview is about to start the filtering so show
        // the ProgressPager
        //mCancelButton.setVisibility(View.INVISIBLE);
        //mLoadingIndicator.setVisibility(View.VISIBLE);
        super.performFiltering(text, keyCode);
    }

    @Override
    public void onFilterComplete(int count) {
        // the AutoCompleteTextView has done its job and it's about to show
        // the drop down so close/hide the ProgreeBar
       // mCancelButton.setVisibility(View.VISIBLE);
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        totalcount = count;
        super.onFilterComplete(count);
    }

}