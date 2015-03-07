package com.surv.ui123;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class Expand extends Animation implements Animation.AnimationListener {
	private View view;
	private static long ANIMATION_DURATION;

	private int ToWidth;

	public void getParams(View v) {

		this.view = v;

		ToWidth = (int) (GlobVars.screenwidth * 0.75);
		ANIMATION_DURATION = 1;
		setDuration(ANIMATION_DURATION);
		setRepeatCount(5);
		setFillAfter(false);
		setInterpolator(new AccelerateInterpolator());

		setAnimationListener(this);
		v.startAnimation(this);
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub

		((MainActivity) view.getContext()).getSupportActionBar()
				.setHomeButtonEnabled(true);
		GlobVars.isDoing = false;
		FragLeft.b.setEnabled(true);
		FragLeft.b1.setEnabled(true);
		FragLeft.b2.setEnabled(true);
		MainActivity.h.setEnabled(true);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

		LayoutParams lyp = view.getLayoutParams();
		lyp.width = lyp.width + (int) (ToWidth / 5);
		view.setLayoutParams(lyp);

	}

}
