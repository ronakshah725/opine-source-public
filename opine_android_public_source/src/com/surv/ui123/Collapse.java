package com.surv.ui123;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class Collapse extends Animation implements Animation.AnimationListener {

	private View view;
	private static long ANIMATION_DURATION;

	private int FromWidth;

	public Collapse() {

	}

	public void getParams(View v) {

		this.view = v;
		ANIMATION_DURATION = 1;

		FromWidth = (int) (GlobVars.screenwidth * 0.75);
		setDuration(ANIMATION_DURATION);
		setRepeatCount(5);
		setFillAfter(false);
		setInterpolator(new AccelerateInterpolator());
		setAnimationListener(this);
		v.startAnimation(this);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
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

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		LayoutParams lyp = view.getLayoutParams();
		lyp.width = lyp.width - (int) (FromWidth / 5);
		view.setLayoutParams(lyp);
	}

}
