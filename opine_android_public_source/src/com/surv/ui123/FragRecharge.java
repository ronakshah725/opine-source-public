package com.surv.ui123;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragRecharge extends Fragment {
	boolean is;
	int selected = 0;
	TextView txv, tno;
	TextView tv2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		is = isOnline();
		if (is)
			view = inflater.inflate(R.layout.fragrech, container, false);
		else
			view = inflater.inflate(R.layout.no_internet, container, false);

		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		MainActivity.actionBarVisiblity(4);
		is = isOnline();
		if (is) {
			final Button b1 = (Button) getActivity().findViewById(R.id.upBtn);
			final Button b2 = (Button) getActivity().findViewById(R.id.dwnBtn);
			Button b3 = (Button) getActivity().findViewById(R.id.rechr);
			Button b4 = (Button) getActivity().findViewById(R.id.Choiceoper);
			txv = (TextView) getActivity().findViewById(R.id.selectOp);
			tno = (TextView) getActivity().findViewById(R.id.phno);
			tv2=(TextView)getActivity().findViewById(R.id.ressbal1);
			tv2.setText(""+GlobVars.current_bal);
			final EditText et = (EditText) getActivity().findViewById(R.id.amt);
			et.setFilters(new InputFilter[] { new InputFilterMinMax("0", ""
					+ GlobVars.current_bal) });

			EditText nom = (EditText) getActivity().findViewById(R.id.phno);
			InputFilter[] FilterArray = new InputFilter[2];
			FilterArray[0] = new InputFilter.LengthFilter(20);
			FilterArray[1] = new InputFilter() {

				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					for (int i = start; i < end; i++) {
						if (!Character.isDigit(source.charAt(i))
								|| source.length() > 10) {
							return "";
						}
					}
					return null;
				}
			};
			nom.setFilters(FilterArray);
			b1.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(et.getText().toString().trim().length()==0){
						return;
					}
					int x = Integer.parseInt(String.valueOf(et.getText()));
					if (x != GlobVars.current_bal)
						et.setText("" + (x + 1));
				}

			});

			b2.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(et.getText().toString().trim().length()==0){
						return;
					}
					int x = Integer.parseInt(String.valueOf(et.getText()));
					if (x != 0)
						et.setText("" + (x - 1));
				}
			});

			b4.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					showDialogButtonClick();
				}
			});

			b3.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					is = isOnline();
					if (is) {
						if (Integer.parseInt("" + et.getText()) != 0
								&& !"".equals(txv.getText() + "")
								&& tno.getText().length() == 10) {

							new AlertDialog.Builder(v.getContext())
									.setTitle("Confirm Recharge Amount")
									.setMessage("Rs. " + et.getText()+"\n"+ txv.getText()+"\n"+tno.getText())
									.setPositiveButton(
											"Recharge",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													GlobVars.current_bal = GlobVars.current_bal
															- Integer.parseInt(""
																	+ et.getText());
													AccessSP.setCurBal(
															MainActivity.champu,
															GlobVars.current_bal);
													SimpleDateFormat sd = new SimpleDateFormat(
															"ddMMyyyyhhmmss",
															Locale.US);
													String ts = sd
															.format(new Date());
													MainActivity.db.addRechReq(
															Integer.parseInt(""
																	+ et.getText()),
															"" + txv.getText(),
															"" + tno.getText(),
															ts);
													 SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
											            String currentDateandTime = sdf.format(new Date());
											         logData ld=new logData("Recharge Requested "+et.getText(),currentDateandTime); 
											         MainActivity.db.addLogM(ld);
											         SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
											            String currentDateandTime1 = sdf1.format(new Date());
											         logData ld1=new logData("Points Updated to "+GlobVars.current_pts,currentDateandTime1); 
											         MainActivity.db.addLogM(ld1);
													et.setText("0");
													txv.setText("");
													tno.setText("");
													Log.w("Recharge Values",
															" "
																	+ et.getText()
																	+ " "
																	+ txv.getText()
																	+ " "
																	+ tno.getText());
													Toast.makeText(
															MainActivity.champu,
															"Recharge Request Sent",
															Toast.LENGTH_SHORT)
															.show();
													Intent intent = new Intent(
															MainActivity.champu,
															SometimesServiceR.class);
													MainActivity.champu
															.startService(intent);

												}
											})
									.setNegativeButton("Cancel", null).show();
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MainActivity.champu);

							builder.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle("Invalid Inputs")
									.setMessage(
											"Please check if all fields are filled correctly.")
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();

												}

											}).show();
						}
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

		}
	}

	void showDialogButtonClick() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.champu);
		builder.setTitle("Select Operator");

		final CharSequence[] choiceList = { "AirTel", "Vodafone",
				"Loop Mobile", "DOLPHIN", "IDEA", "Reliance", "TATA DOCOMO",
				"Aircel", "Uninor", "VIDEOCON" };

		builder.setSingleChoiceItems(choiceList, selected,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selected = which;

						switch (selected) {
						case 0:
							txv.setText("Airtel");
							break;
						case 1:
							txv.setText("Vodafone");
							break;
						case 2:
							txv.setText("Loop Mobile");
							break;
						case 3:
							txv.setText("DOLPHIN");
							break;
						case 4:
							txv.setText("IDEA");
							break;
						case 5:
							txv.setText("Reliance");
							break;
						case 6:
							txv.setText("TATA DOCOMO");
							break;
						case 7:
							txv.setText("Aircel");
							break;
						case 8:
							txv.setText("Uninor");
							break;
						case 9:
							txv.setText("VIDEOCON");
							break;
						}

						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();

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
