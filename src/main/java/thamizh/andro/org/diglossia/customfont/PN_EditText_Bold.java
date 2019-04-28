package thamizh.andro.org.diglossia.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class PN_EditText_Bold extends AppCompatEditText {

	public PN_EditText_Bold(Context context) {
		super(context);
		setFont();
	}
	public PN_EditText_Bold(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public PN_EditText_Bold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "ProximaNova-Bold.otf");
		setTypeface(font, Typeface.NORMAL);
	}

}
