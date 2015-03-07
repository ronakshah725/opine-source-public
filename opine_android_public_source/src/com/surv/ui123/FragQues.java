package com.surv.ui123;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class FragQues extends Fragment {

	static FragmentTransaction transaction;
	static FragmentManager fm;
	static FragmentAdap pagerAdapter;
	static List<Questions> allQuests;
	static List<Response_yn> allYn;
	static List<Options_Radio> allOpRad;
	static List<Options_RadioC> allOpRadC;
	static List<Options_Checkbox> allOpChB;
	static List<Response_Rating> allRat;
	static List<Otherans> allOther;
	static List<Response_FeedBack> allFb;
	static List<Dateans>allDts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragquest, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		/** Getting a reference to the ViewPager defined the layout file */
		final ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
		MainActivity.actionBarVisiblity(2);
		MainActivity.isSearch=false;
		pager.setOffscreenPageLimit(0);
		/** Getting fragment manager */
		FragmentManager fm = getChildFragmentManager();
		/** Instantiating FragmentPagerAdapter */
		pagerAdapter = new FragmentAdap(fm);
		pagerAdapter.setCount(MainActivity.curr.getNo() + 1);
		allQuests = MainActivity.db.getQuestions(MainActivity.curr.getId());
		allYn = MainActivity.db.getAllYn(MainActivity.curr.getId());
		allOpRad = MainActivity.db.getAllOpRad(MainActivity.curr.getId());
		allOpRadC = MainActivity.db.getAllOpRadC(MainActivity.curr.getId());
		allOpChB = MainActivity.db.getAllOpChB(MainActivity.curr.getId());
		allRat = MainActivity.db.getAllRat(MainActivity.curr.getId());
		allFb = MainActivity.db.getAllFb(MainActivity.curr.getId());
		allOther=MainActivity.db.getAllOtherans(MainActivity.curr.getId());
		allDts=MainActivity.db.getAllDateans(MainActivity.curr.getId());	
		pager.setAdapter(pagerAdapter);
		
		pager.setOnTouchListener(new View.OnTouchListener() {	       

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				 arg0.getParent().requestDisallowInterceptTouchEvent(true);
		            return false;
			}
	    });

		
		
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Log.w("Page Selected", "Page Selected..");				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				 pager.getParent().requestDisallowInterceptTouchEvent(true);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	
	
	

}
