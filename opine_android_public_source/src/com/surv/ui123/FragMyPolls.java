package com.surv.ui123;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FragMyPolls extends ListFragment {

	static ListView list1;
	static pollmypoll_adap adapter;	
	static List<pollsWithin> allmypoll;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mypoll, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		list1 = (ListView) getActivity().findViewById(android.R.id.list);
		allmypoll=MainActivity.db.getAllPollsWithin();
				adapter = new pollmypoll_adap(getActivity());
				setListAdapter(adapter);	
		Button b1= (Button)getActivity().findViewById(R.id.cats);
		b1.setBackgroundColor(getResources().getColor(R.color.orange_d));
		b1.setTextColor(getResources().getColor(R.color.xwhite));
		Button b2= (Button)getActivity().findViewById(R.id.myp);
		b2.setBackgroundColor(getResources().getColor(R.color.xwhite));
		b2.setTextColor(getResources().getColor(R.color.orange_d));
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(allmypoll.get(position).getNrpl()!=0)
				{
				FragPolls.qid1=allmypoll.get(position).getQid();
				FragPolls.ques1=allmypoll.get(position).getQues();
				FragPolls.ops1=allmypoll.get(position).getOps();
				FragPolls.res1=allmypoll.get(position).getRes();
				FragPolls.nrpl=allmypoll.get(position).getNrpl();
				PieActivity p = new PieActivity();
				Intent pi=p.execute(MainActivity.champu);
				startActivity(pi);
				}
		      }
		});

	}
	
}