package com.surv.ui123;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FragPollOps extends Fragment {
	boolean is;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		is = isOnline();
		if (is)
			view = inflater.inflate(R.layout.fragpollops, container, false);
		else
			view = inflater.inflate(R.layout.no_internet, container, false);

		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		is = isOnline();
		if (is) {
		 TextView qid=(TextView)getActivity().findViewById(R.id.idops);
		 qid.setText(FragPolls.qid);
		 TextView ques=(TextView)getActivity().findViewById(R.id.pollqops);
		 ques.setText(FragPolls.ques);
		 TableLayout tl=(TableLayout)getActivity().findViewById(R.id.tblops);
		 tl.removeAllViews();
		 String op[]=FragPolls.ops.split("~#%&");
		 final Button b=(Button)getActivity().findViewById(R.id.subans);
		 final Button b1=(Button)getActivity().findViewById(R.id.rspm);
		 
			b.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					is = isOnline();
					if (is) {	
						   Intent in = new Intent(MainActivity.champu, SometimesServiceOR.class);						  
						    MainActivity.champu.startService(in);
						
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MainActivity.champu);

						builder.setIcon(android.R.drawable.ic_dialog_alert)
								.setTitle("No Connection")
								.setMessage(
										"Please check your internet connection before sending request")
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();

											}

										}).show();

					}
				}
			});
			
			b1.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(final View v) {

					is = isOnline();
					if (is) {						  
						    new AlertDialog.Builder(getActivity())
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setMessage(
									"Are you sure you want to report spam??")
							.setPositiveButton(
									"OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											Log.w("Questionid",FragPolls.qid);
											 Intent in = new Intent(MainActivity.champu, SometimesServiceSPM.class);						  
											 MainActivity.champu.startService(in);
											dialog.dismiss();
											v.setEnabled(false);
										}
									}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
						
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MainActivity.champu);

						builder.setIcon(android.R.drawable.ic_dialog_alert)
								.setTitle("No Connection")
								.setMessage(
										"Please check your internet connection before sending request")
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();

											}

										}).show();

					}
				}
			});
		 RadioGroup rg=new RadioGroup(getActivity());
		 for(int i=0;i<op.length;i++)
		 {
			 RadioButton r=new RadioButton(getActivity());
			 r.setId(i);
			 r.setText(op[i]);
			 rg.addView(r, i);
			 
		 }
		 rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					FragPolls.res=checkedId;
					Log.w("Checked Id",""+FragPolls.res);
					b.setEnabled(true);
					
				}
			});
		 tl.addView(rg);
		

		}
	}

	

	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) MainActivity.champu
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}
}
