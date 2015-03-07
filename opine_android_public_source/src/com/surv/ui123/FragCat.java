package com.surv.ui123;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FragCat extends ListFragment {

	static ListView list1;
	static pollcat_adap adapter;	
	static List<pollCats> allpollCats;
	static String currCat;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragcat, container, false);
		return view;
	}

	// *************************************************************************************************************

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);	
		
		list1 = (ListView) getActivity().findViewById(android.R.id.list);
allpollCats=MainActivity.db.getAllCats();
		adapter = new pollcat_adap(getActivity());		
		setListAdapter(adapter);	
		Button b1= (Button)getActivity().findViewById(R.id.myp);
		b1.setBackgroundColor(getResources().getColor(R.color.orange_d));
		b1.setTextColor(getResources().getColor(R.color.xwhite));
		Button b2= (Button)getActivity().findViewById(R.id.cats);
		b2.setBackgroundColor(getResources().getColor(R.color.xwhite));
		b2.setTextColor(getResources().getColor(R.color.orange_d));
		// Click event for single list row
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(allpollCats.get(position).getnrpl()!=0){
Log.w("Selected Category",""+position);
FragPolls.putProgUICats(position);
			}}
		});
		
}
}
