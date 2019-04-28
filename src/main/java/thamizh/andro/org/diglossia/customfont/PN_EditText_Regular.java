package thamizh.andro.org.diglossia.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class PN_EditText_Regular extends EditText {
	
	public PN_EditText_Regular(Context context) {
		super(context);
		setFont();
	}
	public PN_EditText_Regular(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public PN_EditText_Regular(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "ProximaNova-Regular.otf");
		setTypeface(font, Typeface.NORMAL);
	}

}
