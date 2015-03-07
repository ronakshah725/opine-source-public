package com.surv.ui123;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragRedeem extends ListFragment {

	static ListView list1;
	static voucher_adapter adapter;
	boolean is;
	// static View v1;

	static TextView tv2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		is = isOnline();
		if (is) {
			view = inflater.inflate(R.layout.fragredeem, container, false);
		} else {
			view = inflater.inflate(R.layout.no_internet, container, false);
		}

		return view;
	}

	// *************************************************************************************************************
	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) MainActivity.champu
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
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
		if (is) {
			
			MainActivity.cntr = 0;
			MainActivity.allVouchers=MainActivity.db.getAllVouchers();
			MainActivity.actionBarVisiblity(3);
			list1 = (ListView) getActivity().findViewById(android.R.id.list);			
			
tv2=(TextView)getActivity().findViewById(R.id.ressbal);
tv2.setText(""+GlobVars.current_bal);
MainActivity.db.isetcntV();
			// Getting adapter by passing xml data ArrayList
			adapter = new voucher_adapter(getActivity());
			setListAdapter(adapter);
			Button b = (Button) getActivity().findViewById(R.id.redm);			
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (isOnline()) {
						String s = "";
						for (int i = 0; i < MainActivity.allVouchers.size(); i++) {
							if (MainActivity.allVouchers.get(i).getChk() == 1) {
								s = s
										+ ","
										+ MainActivity.allVouchers.get(i)
												.getId();
								Log.w("Selected Vouchers",s);
							}
						}


						if (s.equals("")) {
							new AlertDialog.Builder(getActivity())
									.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle("No Vouchers Selected")
									.setMessage(
											"You have not selected any vouchers to Redeem")
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											}).show();
						} else {
							s=s.substring(1);
							final String sf = s;
							Log.w("sf=",sf);
							TextView tx = (TextView) getActivity()
									.findViewById(R.id.slt);
							final int h = Integer.parseInt("" + tx.getText());
							new AlertDialog.Builder(v.getContext())
									.setTitle("Redeem")
									.setMessage(											
											"Are you sure you want to buy the selected "+h+" vouchers?")
									.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {																							
													//v1.setEnabled(false);													
													AccessSP.setCurBal(
															MainActivity.champu,
															GlobVars.current_bal);
													Log.w("Current bal",""+GlobVars.current_bal);

													SimpleDateFormat sd = new SimpleDateFormat(
															"ddMMyyyyhhmmss",
															Locale.US);
													String ts = sd
															.format(new Date());
													MainActivity.db.addVrcR(sf,
															ts);
													 SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
											            String currentDateandTime = sdf.format(new Date());
											            if(h>1)
											            {
											         logData ld=new logData(h+" Vouchers Requested ",currentDateandTime); 
											         MainActivity.db.addLogM(ld);
											            }
											            else
											            {
											          logData ld=new logData(h+" Voucher Requested ",currentDateandTime); 
											          MainActivity.db.addLogM(ld);
											            }
											         
											         SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
											            String currentDateandTime1 = sdf1.format(new Date());
											         logData ld1=new logData("Points Updated to "+GlobVars.current_pts,currentDateandTime1); 
											         MainActivity.db.addLogM(ld1);
													MainActivity.rePopulateV();
													((MainActivity) MainActivity.champu)
															.runOnUiThread(new Runnable() {
																@Override
																public void run() {
																	FragRedeem.adapter
																			.notifyDataSetChanged();

																	//FragRedeem.v1
																			//.setEnabled(true);
																}
															});
													Log.w("hoyage ji",
															"started");
													TextView tx = (TextView) getActivity()
															.findViewById(R.id.slt);
													tx.setText("0");
													Toast.makeText(
															MainActivity.champu,
															"Voucher Request Sent",
															Toast.LENGTH_SHORT)
															.show();
													Intent in = new Intent(
															MainActivity.champu,
															SometimesServiceV.class);													
													MainActivity.champu
															.startService(in);													
												}
											}).setNegativeButton("No", null)
									.show();
						}
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MainActivity.champu);

						builder.setIcon(android.R.drawable.ic_dialog_alert)
								.setTitle("Not Connection")
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
			// Click event for single list row
			list1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Voucher t = MainActivity.allVouchers.get(position);

					ViewHolder viewHolder = (ViewHolder) view.getTag();
					viewHolder.getBdesc().setFocusableInTouchMode(false);
					if (t.getChk() == 0) {
						if (t.getCost() <= GlobVars.current_bal) {

							GlobVars.current_bal = GlobVars.current_bal
									- t.getCost();
							TextView tx = (TextView) getActivity()
									.findViewById(R.id.slt);
							int h = Integer.parseInt("" + tx.getText());
							tx.setText("" + (h + 1));
							tv2.setText("" + GlobVars.current_bal);
							Log.w("HHHHHHHHH", "" + GlobVars.current_bal);
							viewHolder.getChk().setChecked(true);
							t.toggleChecked();
						} else {
							new AlertDialog.Builder(getActivity())
									.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle("Insufficient Balance")
									.setMessage(
											"Your balance is insufficient for selecting this voucher")
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											}).show();
							Log.w("AAAAAAAAA", "" + GlobVars.current_bal);
							viewHolder.getChk().setChecked(false);
						}

					} else {
						GlobVars.current_bal = GlobVars.current_bal
								+ t.getCost();			
						//TextView tx = (TextView) getActivity().findViewById(
						//		R.id.slt);
						//int h = Integer.parseInt("" + tx.getText());
						//tx.setText("" + (h - 1));
					    tv2.setText("" + GlobVars.current_bal);
						Log.w("GRRRRRRRRRRRR", "" + GlobVars.current_bal);
						viewHolder.getChk().setChecked(false);
						t.toggleChecked();
					}
				}

			});
		}
	}

	public void domysearch(String q) {
		Log.w("Do my search", "Called" + q);
		if (MainActivity.allVouchers != null) {
			MainActivity.allVouchers.clear();
		}
		MainActivity.db.getSearchV(q);
		((Activity) MainActivity.champu).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}

}
