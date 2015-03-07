package com.surv.ui123;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragPolls extends Fragment {

	static FragmentTransaction transaction;
	static FragmentManager fm;
	static int mult;
	Context who;	
	static List<pollsWithin> allpollswithin;
	static int nomore;
	static String qid;
	static String ques;
	static String ops;
	static int res;
	static String qid1;
	static String ques1;
	static String ops1;
	static String res1;
	static int nrpl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;

		view = inflater.inflate(R.layout.polls1, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		MainActivity.actionBarVisiblity(8);
		allpollswithin=new ArrayList<pollsWithin>();
		fm = getChildFragmentManager();
		Fragment fr = new FragCat();
		mult=1;
		transaction = getChildFragmentManager().beginTransaction();
		transaction.add(R.id.fpolls1, fr).commit();
		
		Button b1= (Button)getActivity().findViewById(R.id.myp);
		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("Tab 1","Tab 1:");
				if(FragPolls.fm.findFragmentByTag("mypolls") == null)
				{
				Fragment fr= new FragMyPolls();
				transaction = getChildFragmentManager().beginTransaction();
				transaction.replace(R.id.fpolls1, fr,"mypolls");
				transaction.addToBackStack(null);
				transaction.commit();
				}				
			}
		});
		Button b2= (Button)getActivity().findViewById(R.id.cats);
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Log.w("Tab 2","Tab 2:");
				if(FragPolls.fm.findFragmentByTag("mypolls") != null)
				{
				boolean pp=FragPolls.fm.popBackStackImmediate();
				Button b1= (Button)getActivity().findViewById(R.id.myp);
				b1.setBackgroundColor(getResources().getColor(R.color.orange_d));
				b1.setTextColor(getResources().getColor(R.color.xwhite));
				Button b2= (Button)getActivity().findViewById(R.id.cats);
				b2.setBackgroundColor(getResources().getColor(R.color.xwhite));
				b2.setTextColor(getResources().getColor(R.color.orange_d));
				Log.w("Popped",""+pp);
				}	
				
			}
		});

	}
	
	public static void putProgUICats(int position)
	{
	Fragment fr= new ProgressUI();
	FragCat.currCat=FragCat.allpollCats.get(position).getCats();
	FragPolls.transaction = fm.beginTransaction();
	FragPolls.transaction.replace(R.id.fpolls1, fr);
	FragPolls.transaction.addToBackStack(null);
	FragPolls.transaction.commit();
	Intent in = new Intent(MainActivity.champu, SometimesServiceP.class);
	MainActivity.champu.startService(in);	
	}
	

	public static void putProgUIPolls(int position)
	{
	Fragment fr= new ProgressUI();
	FragPolls.qid=FragPolls.allpollswithin.get(position).getQid();
	FragPolls.transaction = fm.beginTransaction();
	FragPolls.transaction.replace(R.id.fpolls1, fr);
	FragPolls.transaction.addToBackStack(null);
	FragPolls.transaction.commit();
    Intent in = new Intent(MainActivity.champu, SometimesServicePO.class);
    MainActivity.champu.startService(in);
	}
	
	public static void putPolls()
	{
	FragPolls.fm.popBackStack();
	Fragment fr= new FragPollsWithin();
	FragPolls.transaction=FragPolls.fm.beginTransaction();
	FragPolls.transaction.replace(R.id.fpolls1, fr);
	FragPolls.transaction.addToBackStack(null);
	FragPolls.transaction.commit();	
	}
	public static void showOps()
	{
	FragPolls.fm.popBackStack();
	Fragment fr= new FragPollOps();
	FragPolls.transaction=FragPolls.fm.beginTransaction();
	FragPolls.transaction.replace(R.id.fpolls1, fr);
	FragPolls.transaction.addToBackStack(null);
	FragPolls.transaction.commit();	
	}
	public static void noInternet()
	{
	FragPolls.fm.popBackStack();
	Fragment fr= new FragNoInternet();
	FragPolls.transaction=FragPolls.fm.beginTransaction();
	FragPolls.transaction.replace(R.id.fpolls1, fr);
	FragPolls.transaction.addToBackStack(null);
	FragPolls.transaction.commit();	
	}
}