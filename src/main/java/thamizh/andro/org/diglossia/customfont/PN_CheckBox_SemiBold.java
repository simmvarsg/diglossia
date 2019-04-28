package thamizh.andro.org.diglossia.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class PN_CheckBox_SemiBold extends CheckBox {

	public PN_CheckBox_SemiBold(Context context) {
		super(context);
		setFont();
	}
	public PN_CheckBox_SemiBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public PN_CheckBox_SemiBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "ProximaNova-Semibold.otf");
		setTypeface(font, Typeface.NORMAL);
	}

}
