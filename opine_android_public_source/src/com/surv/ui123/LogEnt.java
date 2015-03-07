package com.surv.ui123;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LogEnt extends ListFragment {

	static ListView list1;
	static LogAdap adapter;
	static List<logData> allLog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.w("LogFrag", "created");
		View view = inflater.inflate(R.layout.fraglog, container, false);
		return view;
	}

	// *************************************************************************************************************

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		MainActivity.actionBarVisiblity(9);
		list1 = (ListView) getActivity().findViewById(android.R.id.list);

		allLog = MainActivity.db.getAllLogData();
		// Getting adapter by passing xml data ArrayList
		adapter = new LogAdap(getActivity());
		setListAdapter(adapter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.w("LogFrag", "destroyed");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.w("LogFrag", "Paused");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.w("LogFrag", "Resumed");
	}

}
