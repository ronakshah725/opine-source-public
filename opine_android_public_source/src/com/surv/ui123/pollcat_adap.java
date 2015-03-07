package com.surv.ui123;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class pollcat_adap extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	String extStorageDirectory;

	// public ImageLoader imageLoader;

	public pollcat_adap(Activity a) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // imageLoader=new

	}

	public int getCount() {

		return FragCat.allpollCats.size();
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
		final pollCats t = FragCat.allpollCats.get(position);

		if (convertView == null) {
			vi = inflater.inflate(R.layout.catrow, null);
			h.catnm = (TextView) vi.findViewById(R.id.catnm);
			h.addpc=(LinearLayout)vi.findViewById(R.id.addpc);// title

			vi.setTag(h);
		} else {
			h = (ViewHolder) vi.getTag();
		}

		h.catnm.setText("" + t.getCats()+" ("+t.getnrpl()+")");
		h.addpc.setOnClickListener(new RelativeLayout.OnClickListener() {
			@Override
			public void onClick(View v) {
			    final Dialog dialog = new Dialog(MainActivity.champu);
				dialog.setContentView(R.layout.adpoll);
				
				Button dialogButton = (Button) dialog.findViewById(R.id.cncl);
				final EditText et=(EditText)dialog.findViewById(R.id.pollq);
				TextView tv=(TextView)dialog.findViewById(R.id.nadops);
				final Button dialogButton1 = (Button) dialog.findViewById(R.id.crt);
				final Button dialogButton2 = (Button) dialog.findViewById(R.id.addop);
				final TableLayout tl= (TableLayout)dialog.findViewById(R.id.tblay);
				tl.removeAllViews();
				final TableRow tr1=new TableRow(MainActivity.champu);
				TableRow tr2=new TableRow(MainActivity.champu);
				final InputFilter[] FilterArray = new InputFilter[2];
				FilterArray[0] = new InputFilter.LengthFilter(50);
				FilterArray[1] = new InputFilter() {
					@Override
					public CharSequence filter(CharSequence arg0, int arg1, int arg2,
							Spanned arg3, int arg4, int arg5) {
						for (int i = arg1; i < arg2; i++) {
							if (!Character.isLetterOrDigit(arg0
									.charAt(i))
									&& arg0.charAt(i) != '.'
									&& arg0.charAt(i) != ','
									&& arg0.charAt(i) != ' '&&arg0.charAt(i) != '?') {
								return "";
							}
						}
						
						return null;
					}
				};
				et.setFilters(FilterArray);
				EditText et1=new EditText(MainActivity.champu);
				EditText et2=new EditText(MainActivity.champu);
				Button bt1=new Button(MainActivity.champu);
				Button bt2=new Button(MainActivity.champu);
				et1.setFilters(FilterArray);				
				et1.setId(1);
				et1.setHeight(35);
				et1.setWidth(150);
				et1.setHint("Option 1");
				tr1.addView(et1);				
				tl.addView(tr1);
				et2.setFilters(FilterArray);
				et2.setId(2);
				et2.setHint("Option 2");
				et2.setHeight(35);
				et2.setWidth(150);
				tr2.addView(et2);
				tl.addView(tr2);
				tv.setText("2");
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialogButton2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {					
TableRow tr=new TableRow(MainActivity.champu);
EditText edt=new EditText(MainActivity.champu);
edt.setFilters(FilterArray);
TextView tv=(TextView)dialog.findViewById(R.id.nadops);
int num=Integer.parseInt(""+tv.getText());
num=num+1;
tv.setText(""+num);
edt.setId(num);
edt.setHeight(30);
edt.setWidth(200);
edt.setHint("Option "+num);
tr.addView(edt);
tl.addView(tr);
if(num==5)
{
	((Button) v).setEnabled(false);
}

					}
				});
				
				dialogButton1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TextView tv=(TextView)dialog.findViewById(R.id.nadops);
						int num=Integer.parseInt(""+tv.getText());
						boolean can=true;
						if("".equals(et.getText()))
						{
						can=false;	
						}
					for(int i=0;i<num;i++)
					{
						int id = MainActivity.champu.getResources().getIdentifier(""+(i+1), "id", MainActivity.champu.getPackageName());
						EditText ex=(EditText) dialog.findViewById(id);
						if(ex!=null)
						{
						if("".equals(""+ex.getText()))
						{
							can=false;
						}
						}
					}
					if(can)
					{
						if(isOnline())
						{
							String options="";
							for(int i=0;i<num;i++)
							{
								int id = MainActivity.champu.getResources().getIdentifier(""+(i+1), "id", MainActivity.champu.getPackageName());
								EditText ex=(EditText) dialog.findViewById(id);
								
								if(ex!=null)
								{
								options=options+ex.getText();
								ex.setText("");
								tv.setText("2");
								if((i+1)!=num)
								{
									options=options+"~#%&";
								}
								}
							}
							String ques=""+et.getText();
							et.setText("");
							Intent intent = new Intent(
									MainActivity.champu,
									SometimesServiceNP.class);
							intent.putExtra("ques", ques);
							intent.putExtra("options", options);
							intent.putExtra("catnm",t.getCats());
							MainActivity.champu
									.startService(intent);	
							dialog.dismiss();
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MainActivity.champu);

							builder.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle("No Internet")
									.setMessage(
											"Please check your internet connection")
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
					}
					else
					{
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
					}
				});
				dialog.setTitle("Create a Poll - "+t.getCats());
				dialog.show();
				
			}
		});
		return vi;
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
		Log.w("changes", "Notified");
	}

	static class ViewHolder {
		public TextView catnm;
		public LinearLayout addpc;
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
