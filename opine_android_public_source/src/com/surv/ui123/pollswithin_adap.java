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
public class pollswithin_adap extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	String extStorageDirectory;	
	public pollswithin_adap(Activity a) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // imageLoader=new
																	// ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
Log.w("Setting Count when nomore",""+FragPolls.nomore);
		
			return (FragPolls.allpollswithin.size());	
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
		ViewHolder h1=new ViewHolder();
		Log.w("Position and size and nomore",position+" "+FragPolls.allpollswithin.size()+" "+FragPolls.nomore);
	
			pollsWithin t = FragPolls.allpollswithin.get(position);
           Log.w("Demanding Position",""+position);
			if (convertView == null) {
				vi = inflater.inflate(R.layout.pollswithin, null);
				h1.ques = (TextView) vi.findViewById(R.id.ques); // title
				h1.nrpl = (TextView) vi.findViewById(R.id.nrpl); // artist name
				h1.qid = (TextView) vi.findViewById(R.id.qid);
Log.w("Setting h1",""+h1);
				vi.setTag(h1);
			} else {
				h1 = (ViewHolder) vi.getTag();
				Log.w("Setting h2",""+h1);
			}
Log.w("Quest setting",t.getQues()+" "+t.getNrpl());
Log.w("Quest setting",t.getQues()+" "+t.getNrpl()+" "+h1.ques);
			h1.ques.setText(t.getQues());
			h1.nrpl.setText("" + t.getNrpl());
			h1.qid.setText("" + t.getQid());		
		

		return vi;
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
		Log.w("changes", "Notified");
	}

	static class ViewHolder {
		TextView ques;
		TextView nrpl;
		TextView qid;
	}

}
