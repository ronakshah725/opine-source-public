package com.surv.ui123;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

public class SometimesServiceDV extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	static final int Uid = 156652;
	static Context who;
	public static SharedPreferences mPrefs;

	public SometimesServiceDV() {
		super("SometimesServiceDV");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		who = this;
		Log.w("ServiceDV", "Started");
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Uid);
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		Log.w("ServiceDV", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {

		Log.w("In ServiceDV", "Service DV");
		getVouc();
		String[] ids = dbh.getAllVoucIds();
		if (ids != null) {
			if (ids.length != 0) {
				new DlPics(ids, 2, who);
			}
		}
	}

	void getVouc() {
		final JSONParser jsonParser = new JSONParser();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usrnm123", mPrefs.getString("usrnm",
				"")));

		JSONObject jn = null;
		jn = jsonParser.makeHttpRequest(
				getResources().getString(R.string.initvouc), "POST", params,
				who);

		if (jn != null) {
			JSONArray ja = null;
			int num = 0;
			try {
				Log.w("called init", "called init" + jn);
				num = jn.getInt("no");

				Log.w("try mein", "try mein");
                Log.w("no",""+num);
                Log.w("json reply",""+jn);
                if(num!=0)
                {
				ja = jn.getJSONArray("vouchers");
                }
				String ids[] = new String[ja.length()];
				for (int i = 0; i < num; i++) {
					ids[i] = ja.getJSONObject(i).getString("id");
					Log.w("Adding Voucher", ""+ja.getJSONObject(i).getString("id")+" "+ja
							.getJSONObject(i).getString("vcrnm")+" "+ ja
							.getJSONObject(i).getString("descr")+" "+ja
							.getJSONObject(i).getInt("amt"));
					Voucher v = new Voucher(
							ja.getJSONObject(i).getString("id"), ja
									.getJSONObject(i).getString("vcrnm"), ja
									.getJSONObject(i).getString("descr"), ja
									.getJSONObject(i).getInt("amt"), 0);
					dbh.addVoucher(v);
				}
				File folder = new File(Environment
						.getExternalStorageDirectory().toString()
						+ getResources().getString(R.string.opineimages)
						+ getResources().getString(R.string.voucpics));
				folder.mkdirs();
				File noMedia = new File(folder.getAbsolutePath() + "/.nomedia");
				try {
					noMedia.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				new DlPics(ids, 2, MainActivity.champu);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				((Activity) MainActivity.champu).runOnUiThread(new Runnable() {
					public void run() {
						if(MainActivity.checkNull.equals("opine"))
						{
						Fragment fr = new FragRedeem();
						MainActivity.transaction = MainActivity.fm
								.beginTransaction();						

							if (MainActivity.fm.getBackStackEntryCount() > 0)
								{
								MainActivity.fm.popBackStack();
								Log.w("Popped","Popped");
								}

							MainActivity.transaction.replace(R.id.f2, fr,
									"redeem");
							MainActivity.transaction.addToBackStack(null);
							MainActivity.transaction.commit();
						
							AccessSP.setFt(getBaseContext());
						}
					}
				});
			} catch (Exception e) {

			}

		}
	}
}
