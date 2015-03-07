package com.surv.ui123;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class pollmypoll_adap extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	String extStorageDirectory;

	// public ImageLoader imageLoader;

	public pollmypoll_adap(Activity a) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // imageLoader=new

	}

	public int getCount() {

		Log.w("MyPolls","Count MyPoll:"+FragMyPolls.allmypoll.size());
		return FragMyPolls.allmypoll.size();
		
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder h = new ViewHolder();
		final pollsWithin t = FragMyPolls.allmypoll.get(position);

		if (convertView == null) {
			vi = inflater.inflate(R.layout.mypollrow, null);
			h.ques = (TextView) vi.findViewById(R.id.mypollques);
			h.nrpl=(TextView)vi.findViewById(R.id.mypollnrpl);// title
h.del=(Button)vi.findViewById(R.id.delp);
			vi.setTag(h);
		} else {
			h = (ViewHolder) vi.getTag();
		}

		h.ques.setText(""+t.getQues());	
		h.nrpl.setText("" +t.getNrpl());
		if(t.getIcreated()==1)
		{
			h.del.setVisibility(View.VISIBLE);
			h.del.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(final View v) {
					if (isOnline()) {	
						new AlertDialog.Builder(v.getContext())
						.setTitle("Delete Poll")
						.setMessage(											
								"Are you sure you want to delete this polls?")
						.setPositiveButton(
								"Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										Intent in = new Intent(
												MainActivity.champu,
												SometimesServiceDP.class);	
										in.putExtra("qid",t.getQid());
										MainActivity.champu
												.startService(in);
										v.setEnabled(false);
									}
								}).setNegativeButton("No", null)
						.show();
						
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
		else
		{
			h.del.setVisibility(View.INVISIBLE);
		}
		return vi;
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
		Log.w("changes", "Notified");
	}

	static class ViewHolder {
		public TextView ques;
		public TextView nrpl;
		public Button del;
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
