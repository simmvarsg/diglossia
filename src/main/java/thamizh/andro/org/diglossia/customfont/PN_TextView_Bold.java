package thamizh.andro.org.diglossia.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PN_TextView_Bold extends TextView {

	public PN_TextView_Bold(Context context) {
		super(context);
		setFont();
	}
	public PN_TextView_Bold(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public PN_TextView_Bold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "ProximaNova-Bold.otf");
		setTypeface(font, Typeface.NORMAL);
	}

}
