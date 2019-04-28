package thamizh.andro.org.diglossia.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

public class PN_CheckBox_Regular extends AppCompatCheckBox {

	public PN_CheckBox_Regular(Context context) {
		super(context);
		setFont();
	}
	public PN_CheckBox_Regular(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public PN_CheckBox_Regular(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "ProximaNova-Regular.otf");
		setTypeface(font, Typeface.NORMAL);
	}

}
