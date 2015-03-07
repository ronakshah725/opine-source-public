package com.surv.ui123;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragRight extends ListFragment {

	static ListView list1;
	static lazy_adapter adapter;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragright, container, false);
		return view;
	}

	// *************************************************************************************************************

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.w("Frag Right", "yesss");

		MainActivity.actionBarVisiblity(1);
		list1 = (ListView) getActivity().findViewById(android.R.id.list);

		// Getting adapter by passing xml data ArrayList
		adapter = new lazy_adapter(getActivity());
		MainActivity.db.isetcnt();
		setListAdapter(adapter);		
		
		// Click event for single list row
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Fragment fr = new FragQues();
				if (!(MainActivity.allTiles.get(position).getSts() == 3)
						&& !(MainActivity.allTiles.get(position).getSts() == 4)) {

					MainActivity.curr = MainActivity.allTiles.get(position);
					if ((MainActivity.allTiles.get(position).getSts() == 0))
						MainActivity.curr.setSts(1);

					MainActivity.db.updatetilesTable(MainActivity.curr);
					MainActivity.transaction = MainActivity.fm
							.beginTransaction();
					MainActivity.transaction.replace(R.id.f2, fr, "quests");
					MainActivity.transaction.addToBackStack(null);
					MainActivity.transaction.commit();
				}
			}
		});
		
		MainActivity.srchQue.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				domysearch(""+s);
			}

			@Override
			public void beforeTextChanged(
					CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0,
					int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		});
		
	}
	
	
	public void domysearch(String q) {
		Log.w("Do my search", "Called"+q);
		if (MainActivity.allTiles != null) {
			MainActivity.allTiles.clear();
		}
		MainActivity.db.getSearch(q);
		((Activity) MainActivity.champu).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onResume() {
		MainActivity.rePopulate();
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	public static Fragment newInstance() {
		FragRight mFrgment = new FragRight();
		return mFrgment;
	}
}
