package com.surv.ui123;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragPollsWithin extends ListFragment {

	static ListView list1;
	static pollswithin_adap adapter;
	static boolean loadingMore = false;
	static View v1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragpollswithin, container, false);
		v1 = View.inflate(MainActivity.champu,R.layout.progressuismall,null);
		return view;
	}

	// *************************************************************************************************************

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);	
		
		list1 = (ListView) getActivity().findViewById(android.R.id.list);		
		list1.addFooterView(v1);
		adapter = new pollswithin_adap(getActivity());		
		setListAdapter(adapter);		
		
		// Click event for single list row
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
Log.w("Selected Poll",""+position);
FragPolls.qid=FragPolls.allpollswithin.get(position).getQid();
FragPolls.ques=FragPolls.allpollswithin.get(position).getQues();
FragPolls.putProgUIPolls(position);
			}
		});	
		

		//Here is where the magic happens
	 list1.setOnScrollListener(new OnScrollListener(){
			
			//useless here, skip!
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			//dumdumdum			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				
				//what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;				
				
				//is the bottom item visible & not loading more already ? Load more !
				if((lastInScreen == totalItemCount) && !(loadingMore)){					
					Intent intent = new Intent(MainActivity.champu, SometimesServiceP.class);
					MainActivity.champu.startService(intent);
				}
			}
		});
	 Intent intent = new Intent(MainActivity.champu, SometimesServiceP.class);
		MainActivity.champu.startService(intent);
				
}
}
