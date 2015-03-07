package com.surv.ui123;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class LogAdap extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	String extStorageDirectory;

	// public ImageLoader imageLoader;

	public LogAdap(Activity a) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // imageLoader=new
	
	}

	public int getCount() {
Log.w("Size of log ",""+LogEnt.allLog.size());
		return LogEnt.allLog.size();
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
		logData t = LogEnt.allLog.get(position);

		if (convertView == null) {
			vi = inflater.inflate(R.layout.listlog_row, null);
			h.logd = (TextView) vi.findViewById(R.id.logd); // title
			h.dt = (TextView) vi.findViewById(R.id.dt); // artist name		
			vi.setTag(h);
		} else {
			h = (ViewHolder) vi.getTag();
		}

		h.logd.setText(t.getLogd());
		h.dt.setText(t.getDt());		
		return vi;
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
		Log.w("changes", "Notified");
	}

	static class ViewHolder {
		TextView logd;
		TextView dt;		
	}

}
