package com.surv.ui123;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class FragLeft extends Fragment {
	static RelativeLayout b, b2, b3;
	static Fragment fr;
	static RelativeLayout b1, b4,b5,b6,b7;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragleft, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		b = (RelativeLayout)getActivity().findViewById(R.id.callred);
		b5=(RelativeLayout)getActivity().findViewById(R.id.callsurv);
		b6=(RelativeLayout)getActivity().findViewById(R.id.callpoll);
		b4 = (RelativeLayout) getActivity().findViewById(R.id.spl);
		b7=(RelativeLayout) getActivity().findViewById(R.id.logent);
		b4.setOnClickListener(new RelativeLayout.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(MainActivity.champu);
				dialog.setContentView(R.layout.speclsurv);
				Button dialogButton = (Button) dialog.findViewById(R.id.Cancel);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				Button dialogButton1 = (Button) dialog.findViewById(R.id.Send);
				Button dialogButton2 = (Button) dialog.findViewById(R.id.ok);
				dialogButton2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {					
						dialog.dismiss();
					}
				});
				// if button is clicked, close the custom dialog
				dialogButton1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						InputFilter[] FilterArray = new InputFilter[2];
						FilterArray[0] = new InputFilter.LengthFilter(20);
						FilterArray[1] = new InputFilter() {
							@Override
							public CharSequence filter(CharSequence arg0, int arg1, int arg2,
									Spanned arg3, int arg4, int arg5) {
								for (int i = arg1; i < arg2; i++) {
									if (!Character.isLetterOrDigit(arg0.charAt(i))
											|| arg0.length() > 10) {
										return "";
									}
								}
								
								return null;
							}
						};
						
						dialog.findViewById(R.id.rel1).setVisibility(
								View.INVISIBLE);
						dialog.findViewById(R.id.rel2).setVisibility(
								View.VISIBLE);
						EditText tv =(EditText) dialog.findViewById(R.id.splid);
						tv.setFilters(FilterArray);
						String sid=""+tv.getText();
						Log.w("survey id",""+sid);
						Intent i = new Intent(v.getContext(), SometimeServiceSP.class);
						i.putExtra("id", sid);
						MainActivity.champu.startService(i);
					}
				});
				dialog.show();
			}
		});

		b.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
                MainActivity.spin.setSelection(2);
                MainActivity.toggle();
               MainActivity.renderFrag("Vouchers");				
			}
		});

		b2 = (RelativeLayout)getActivity().findViewById(R.id.callrec);
		b2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.renderFrag("Recharge");
				MainActivity.toggle();
			}
		});
		
		b5.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
                MainActivity.spin.setSelection(0);
                MainActivity.toggle();
               MainActivity.renderFrag("Surveys");				
			}
		});
		
		b6.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
                MainActivity.spin.setSelection(1);
                MainActivity.toggle();
               MainActivity.renderFrag("Polls");				
			}
		});
		
		b7.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {              
                MainActivity.toggle();
               MainActivity.renderFrag("LogEnt");				
			}
		});

		
		b1 = (RelativeLayout) getActivity().findViewById(R.id.demog);
		b1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			MainActivity.showDemo();
			}
		});

	}

	public static Fragment newInstance() {
		FragLeft mFrgment = new FragLeft();
		return mFrgment;
	}
}
