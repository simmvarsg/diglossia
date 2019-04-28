package thamizh.andro.org.diglossia.utils;

import android.app.Dialog;
import android.content.Context;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import thamizh.andro.org.diglossia.R;


public class TransparentProgressDialog extends Dialog {
	
	private ImageView iv;
	
	public TransparentProgressDialog(Context context, int resourceIdOfImage) {
		super(context, R.style.TransparentProgressDialog);
    	WindowManager.LayoutParams wlmp = getWindow().getAttributes();
    	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
    	getWindow().setAttributes(wlmp);
		setTitle("Congratulations");
		setCancelable(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		iv = new ImageView(context);
		iv.setImageResource(R.drawable.happy);
		layout.addView(iv, params);
		addContentView(layout, params);
	}

	@Override
	public void show() {
		super.show();
		ScaleAnimation anim = new ScaleAnimation(0.5f, 1.2f, // Start and end values for the X axis scaling
				0.5f, 1.2f, // Start and end values for the Y axis scaling
				Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}
}
