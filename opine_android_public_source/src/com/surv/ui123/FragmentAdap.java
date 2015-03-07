package com.surv.ui123;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class FragmentAdap extends FragmentPagerAdapter {

	int PAGE_COUNT = 0;
	MainActivity m = new MainActivity();

	/** Constructor of the class */
	public FragmentAdap(FragmentManager fm) {
		super(fm);
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {

		MyFragment myFragment = new MyFragment();
		Bundle data = new Bundle();
		data.putInt("current_page", arg0 + 1);
		Log.w("This is current Page", "" + (arg0 + 1));
		myFragment.setArguments(data);
		return myFragment;
	}

	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (GlobVars.isExpanded) {
			MainActivity.toggle();

		}
	}

	public void setCount(int c) {
		PAGE_COUNT = c;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if ((position + 1) != FragQues.pagerAdapter.getCount()) {
			if (FragQues.allQuests.get(position).getIsop() == 1) {
				return "Q #" + (position + 1) + " (Optional)";
			} else {
				return "Q #" + (position + 1);
			}
		} else {
			return "Thank You!";
		}
	}
}