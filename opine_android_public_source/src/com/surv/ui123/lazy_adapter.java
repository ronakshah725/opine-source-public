package com.surv.ui123;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class lazy_adapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	String extStorageDirectory;

	// public ImageLoader imageLoader;

	public lazy_adapter(Activity a) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // imageLoader=new
																	// ImageLoader(activity.getApplicationContext());
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString()
				+ MainActivity.champu.getResources().getString(
						R.string.opineimages)
				+ MainActivity.champu.getResources().getString(
						R.string.tilepics) + "/";
	}

	public int getCount() {

		return MainActivity.db.getCnt();
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
		tilesTable t = MainActivity.allTiles.get(position);

		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_row, null);
			h.cmpnm = (TextView) vi.findViewById(R.id.cmpnm); // title
			h.nodl = (TextView) vi.findViewById(R.id.nodl); // artist name
			h.score = (TextView) vi.findViewById(R.id.score);
			//h.survid = (TextView) vi.findViewById(R.id.survid);
			h.sts = (TextView) vi.findViewById(R.id.sts);
			h.listrow = (LinearLayout) vi.findViewById(R.id.thumbnail);
			vi.setTag(h);
		} else {
			h = (ViewHolder) vi.getTag();
		}

		Log.w("Tilessssssssssssss", t.getId());
		h.cmpnm.setText(t.getCompNm());
		h.nodl.setText("" + t.getNodl());
		h.score.setText("" + t.getPts());
		//h.survid.setText(t.getId());
        Log.w("Getting the status", "Status: "+t.getSts()+" "+t.getId());
		if (t.getSts() == 1) {
			h.sts.setText("Draft");
		} else if (t.getSts() == 2) {
			h.sts.setText("Ready");
		} else if (t.getSts() == 3) {
			h.sts.setText("Submitting");
		} else if (t.getSts() == 4) {
			h.sts.setText("Submitted");
		} else if (t.getSts() == 5) {
			h.sts.setText("Error");
		} else
		{
			h.sts.setText("");
		}
		String pathName = extStorageDirectory
				+ t.getId()
				+ MainActivity.champu.getResources().getString(
						R.string.picextjpg);
		File pic = new File(pathName);
		if (pic.exists()) {
			Resources res = MainActivity.champu.getResources();
			Bitmap bitmap = BitmapFactory.decodeFile(pathName);
			BitmapDrawable bd = new BitmapDrawable(res, bitmap);
			if (Build.VERSION.SDK_INT >= 16) {

				h.listrow.setBackground(bd);
			} else {
				h.listrow.setBackgroundDrawable(bd);
			}
		} else {
			if (Build.VERSION.SDK_INT >= 16) {
				h.listrow.setBackground(((MainActivity) activity)
						.getResources().getDrawable(R.drawable.rihanna));
			} else {
				h.listrow.setBackgroundDrawable(((MainActivity) activity)
						.getResources().getDrawable(R.drawable.rihanna));
			}
		}

		// h.listrow.setLayoutParams(new
		// LayoutParams((int)(GlobVars.screenwidth*0.75),
		// (int)(GlobVars.screenheight*0.45)));
		return vi;
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
		Log.w("changes", "Notified");
	}

	static class ViewHolder {
		TextView cmpnm;
		TextView nodl;
		TextView pts;
		TextView sts;
		LinearLayout listrow;
		TextView score;
		//TextView survid;
	}

}
