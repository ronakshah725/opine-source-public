package com.surv.ui123;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SometimesServiceV extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	static final int Uid = 156652;
	static Context who;
	public static SharedPreferences mPrefs;

	public SometimesServiceV() {
		super("SometimesServiceV");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		who = this;
		Log.w("ServiceV", "Started");
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
		Log.w("ServiceV", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		int c = SometimesServiceV.dbh.getVcrRcnt();
		if (c != 0) {
			requestVouc();
		}
	}

	static void requestVouc() {
		List<tempReq> req = SometimesServiceV.dbh.getAllTempR();
Log.w("Requests Size",""+req.size());
		for (int i = 0; i < req.size(); i++) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("usrnm", mPrefs.getString(
					"usrnm", "")));
			params.add(new BasicNameValuePair("s", "" + req.get(i).getVcr()));
			params.add(new BasicNameValuePair("ts", "" + req.get(i).getTs()));
			Log.w("Voucher ids",""+req.get(i).getVcr());
			Log.w("Ts",""+req.get(i).getTs());
			JSONObject jn = jsonParser.makeHttpRequest(who.getResources()
					.getString(R.string.voucReq), "POST", params, who);

			if (jn == null) {
				Log.w("No Internet", "Could not upload voucher request");
			} else {
				try {
					Log.w("Json Uploading survey",""+jn);
					if (jn.getInt("state") == 1) {
						SometimesServiceV.dbh.delTemp(req.get(i).getVcr(), req
								.get(i).getTs());

					} /*
					 * else if (jn.getInt("state") == 3) { String vid[] =
					 * req.get(i).getVcr().split(","); int cost = 0; for (int j
					 * = 0; j < vid.length; j++) { cost = cost +
					 * SometimesServiceV.dbh.getVCost(vid[j]); }
					 * AccessSP.setCurBal(who, cost);
					 * SometimesServiceV.dbh.delTemp(req.get(i).getVcr(), req
					 * .get(i).getTs()); }
					 */
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
