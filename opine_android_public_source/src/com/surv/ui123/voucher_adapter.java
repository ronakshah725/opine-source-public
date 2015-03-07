package com.surv.ui123;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class voucher_adapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	int pos;
	String extStorageDirectory;
    
	public voucher_adapter(Activity a) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString()
				+ MainActivity.champu.getResources().getString(
						R.string.opineimages)
				+ MainActivity.champu.getResources().getString(
						R.string.voucpics) + "/";

	}

	public int getCount() {

		return MainActivity.db.getSrchCntV();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Voucher t = MainActivity.allVouchers.get(position);
		TextView vcrnm;
		TextView desc;
		TextView cost;
		TextView vid;
		CheckBox chk;
		Button bdesc;
		LinearLayout vouc;
		
Log.w("Setting datafor", ""+position);
		// Create a new row view
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.voucher, null);

			// Find the child views.
			vcrnm = (TextView) convertView.findViewById(R.id.vcrnm);
			desc = (TextView) convertView.findViewById(R.id.desc);
			cost = (TextView) convertView.findViewById(R.id.cost);
			chk = (CheckBox) convertView.findViewById(R.id.chk);
			vouc = (LinearLayout) convertView.findViewById(R.id.vouc);
            vid=(TextView)convertView.findViewById(R.id.vid);
            bdesc=(Button)convertView.findViewById(R.id.bdesc);
          
            
			final ViewHolder vh = new ViewHolder();
			vh.vcrnm = vcrnm;
			vh.desc = desc;
			vh.cost = cost;
			vh.chk = chk;
			vh.vouc = vouc;
			vh.vid=vid;
			vh.bdesc=bdesc;

			// Optimization: Tag the row with it's child views, so we don't have
			// to
			// call findViewById() later when we reuse the row.
			convertView.setTag(vh);

			// If CheckBox is toggled, update the planet it is tagged with.
			vh.chk.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;					
					Voucher t = (Voucher) cb.getTag();
					Log.w("heheheh", "hehhehe");
					if (cb.isChecked()) {
						Log.w("Checkedd", "Checkeddd");
						if (t.getCost() <= GlobVars.current_bal) {

							GlobVars.current_bal = GlobVars.current_bal
									- t.getCost();
							FragRedeem.tv2.setText("" + GlobVars.current_bal);

							t.setChk(1);
							cb.setChecked(true);
							TextView tx=(TextView)((MainActivity) MainActivity.champu).findViewById(R.id.slt);
							int h=Integer.parseInt(""+tx.getText());
							tx.setText(""+(h+1));
						} else {
							new AlertDialog.Builder(activity)
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

							t.setChk(0);
							cb.setChecked(false);
						}

					} else {
						GlobVars.current_bal = GlobVars.current_bal
								+ t.getCost();
						FragRedeem.tv2.setText("" + GlobVars.current_bal);
						t.setChk(0);
						
						cb.setChecked(false);
						TextView tx=(TextView)((MainActivity) MainActivity.champu).findViewById(R.id.slt);
						int h=Integer.parseInt(""+tx.getText());
						tx.setText(""+(h-1));
					}
				}
			});
			
			vh.bdesc.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {

					new AlertDialog.Builder(MainActivity.champu)							
							.setTitle(""+vh.getVcrnm().getText())
							.setMessage(""+vh.desc.getText()
									)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				}
			});
		}
		// Reuse existing row view
		else {
			// Because we use a ViewHolder, we avoid having to call
			// findViewById().
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			vcrnm = viewHolder.getVcrnm();
			desc = viewHolder.getDesc();
			cost = viewHolder.getCost();
			chk = viewHolder.getChk();
			vouc = viewHolder.getVouc();
			vid=viewHolder.getVid();
			bdesc=viewHolder.getBdesc();
		}

		// Tag the CheckBox with the Planet it is displaying, so that we can
		// access the planet in onClick() when the CheckBox is toggled.
		chk.setTag(t);

		// Display planet data
		vcrnm.setText("" + t.getVcrnm());
		desc.setText("" + t.getDesc());
		cost.setText("" + t.getCost());
		vid.setText(""+t.getId());
		if (t.getChk() == 1)
			chk.setChecked(true);
		else
			chk.setChecked(false);
		
		String pathName = extStorageDirectory
				+ vid.getText()
				+ MainActivity.champu.getResources().getString(
						R.string.picextjpg);
		File pic = new File(pathName);
		if (pic.exists()) {
			Resources res = MainActivity.champu.getResources();
			Bitmap bitmap = BitmapFactory.decodeFile(pathName);
			BitmapDrawable bd = new BitmapDrawable(res, bitmap);
			if (Build.VERSION.SDK_INT >= 16) {

				vouc.setBackground(bd);
			} else {
				vouc.setBackgroundDrawable(bd);
			}
		} else {
			if (Build.VERSION.SDK_INT >= 16) {
				vouc.setBackground(((MainActivity) activity)
						.getResources().getDrawable(R.drawable.rihanna));
			} else {
				vouc.setBackgroundDrawable(((MainActivity) activity)
						.getResources().getDrawable(R.drawable.rihanna));
			}
		}
		
		return convertView;
	}

	/*
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) {
	 * 
	 * View view;
	 * 
	 * 
	 * if (convertView == null) {
	 * 
	 * LayoutInflater inflator =activity.getLayoutInflater(); view =
	 * inflator.inflate(R.layout.voucher, null);
	 * 
	 * final ViewHolder h = new ViewHolder();
	 * h.vcrnm=(TextView)view.findViewById(R.id.vcrnm); // title h.vouc=
	 * (LinearLayout)view.findViewById(R.id.vouc); // artist name h.desc =
	 * (TextView) view.findViewById(R.id.desc); h.cost = (TextView)
	 * view.findViewById(R.id.cost); h.chk=(CheckBox)
	 * view.findViewById(R.id.chk); h.chk.setOnClickListener( new
	 * View.OnClickListener() { public void onClick(View v) { CheckBox cb =
	 * (CheckBox) v ; Voucher t = (Voucher) cb.getTag();
	 * 
	 * if(cb.isChecked()) t.setChk(1); else t.setChk(0);
	 * 
	 * } });
	 * 
	 * view.setTag(h); h.chk.setTag(MainActivity.allVouchers.get(position)); }
	 * else { view=convertView;
	 * 
	 * 
	 * }
	 * 
	 * ViewHolder holder = (ViewHolder) view.getTag();
	 * 
	 * Voucher t = MainActivity.allVouchers.get(position);
	 * 
	 * holder.vcrnm.setText(""+t.getVcrnm());
	 * holder.desc.setText(""+t.getDesc()); holder.cost.setText(""+t.getCost());
	 * 
	 * if(t.getChk()==0) holder.chk.setChecked(false); else
	 * holder.chk.setChecked(true);
	 * 
	 * if(t.getVouc().equalsIgnoreCase("x")) { if (Build.VERSION.SDK_INT >= 16)
	 * { holder.vouc.setBackground(((MainActivity)
	 * activity).getResources().getDrawable(R.drawable.rihanna)); } else {
	 * holder.vouc.setBackgroundDrawable(((MainActivity)
	 * activity).getResources().getDrawable(R.drawable.rihanna)); } } else {
	 * 
	 * }
	 * 
	 * return view;
	 * 
	 * }
	 */

	/*
	 * @SuppressWarnings("deprecation") public View getView(int position, View
	 * convertView, ViewGroup parent) {
	 * 
	 * ViewHolder h=new ViewHolder(); Voucher t =
	 * MainActivity.allVouchers.get(position);
	 * 
	 * if (convertView == null) { View vi = convertView; vi =
	 * inflater.inflate(R.layout.voucher, null);
	 * 
	 * h.vcrnm = (TextView) vi.findViewById(R.id.vcrnm); // title h.vouc=
	 * (LinearLayout) vi.findViewById(R.id.vouc); // artist name h.desc =
	 * (TextView) vi.findViewById(R.id.desc); h.cost = (TextView)
	 * vi.findViewById(R.id.cost); h.chk=(CheckBox)vi.findViewById(R.id.chk);
	 * convertView.setTag(h); pos=position;
	 * 
	 * h.chk.setOnClickListener( new View.OnClickListener() { public void
	 * onClick(View v) { CheckBox cb = (CheckBox) v; } }); } else { h =
	 * (ViewHolder) convertView.getTag(); }
	 * 
	 * 
	 * h.chk.setOnCheckedChangeListener(new
	 * CompoundButton.OnCheckedChangeListener() {
	 * 
	 * @Override public void onCheckedChanged(CompoundButton buttonView, boolean
	 * isChecked) {
	 * 
	 * if(MainActivity.allVouchers.get(pos).getCost()>GlobVars.current_bal) {
	 * Log.w("Insufficient", "Insuffiecient balc"); /* new
	 * AlertDialog.Builder(()
	 * .setIcon(android.R.drawable.ic_dialog_alert).setTitle
	 * ("Help Screen").setMessage
	 * ("Your balance is insufficient for this voucher").setPositiveButton(
	 * "OK", new DialogInterface.OnClickListener() { public void
	 * onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
	 * }).show()); buttonView.setSelected(false); } else {
	 * GlobVars.current_bal=GlobVars
	 * .current_bal-MainActivity.allVouchers.get(pos).getCost();
	 * MainActivity.tv2.setText(""+GlobVars.current_bal); Log.w("Insufficient",
	 * ""+GlobVars.current_bal); buttonView.setSelected(true); }
	 * 
	 * } }); vi.setTag(h); } else { h=(ViewHolder)vi.getTag(); }
	 * 
	 * 
	 * h.vcrnm.setText(""+t.getVcrnm()); h.desc.setText(""+t.getDesc());
	 * h.cost.setText(""+t.getCost());
	 * 
	 * h.chk.setText(""+t.getChk());
	 * 
	 * 
	 * 
	 * 
	 * 
	 * if(t.getVouc().equalsIgnoreCase("x")) { if (Build.VERSION.SDK_INT >= 16)
	 * { h.vouc.setBackground(((MainActivity)
	 * activity).getResources().getDrawable(R.drawable.rihanna)); } else {
	 * h.vouc.setBackgroundDrawable(((MainActivity)
	 * activity).getResources().getDrawable(R.drawable.rihanna)); } } else {
	 * 
	 * }
	 * 
	 * return vi; }
	 */
	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
		Log.w("changes", "Notified");
	}

}
