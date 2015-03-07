package com.surv.ui123;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SometimesServiceU extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;

	static Context who;
	public static SharedPreferences mPrefs;

	public SometimesServiceU() {
		super("SometimesServiceU");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("ServiceU", "Started");
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();

		who = this;
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		Log.w("ServiceU", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {

		uploadResponses();
	}

	void uploadResponses() {
		List<String> sid = dbh.getSubmittingTiles();
		JSONParser jsonParser = new JSONParser();
		Log.w("Count Submitting...", "" + sid.size());
		for (int i = 0; i < sid.size(); i++) {
			List<QueTyp> qt = dbh.getAllQus(sid.get(i));
			Creatjo c = new Creatjo();
			String q1 = "";
			for (int j = 0; j < qt.size(); j++) {
				int t = qt.get(j).getTyp();
				String q = "";
				String res = null;
				String res1=null;
				if (t == 1 || t == 2 || t == 4||t==6) {
					res = ""
							+ dbh.getResponseI(sid.get(i), qt.get(j).getQno(),
									t);
					if(t==6)
					{
						res1=""+dbh.getResponseO(sid.get(i), qt.get(j).getQno());
					}
					if(res1==null)
					{
						res1="";
					}
				}

				if (t == 3 || t == 5||t==7) {
					res = dbh.getResponseS(sid.get(i), qt.get(j).getQno(), t);
				}
				q = c.addKey("qno") + c.addValue("" + qt.get(j).getQno()) + ","
						+ c.addKey("typ") + c.addValue("" + t) + ","
						+ c.addKey("res") + c.addValue(res)+","+c.addKey("res1")+c.addValue(res1);

				q = "{" + q + "}";
				if (j != (qt.size() - 1)) {
					q = q + ",";
				}
				q1 = q1 + q;
			}
			String s = "{" + c.addKey("usrnm")
					+ c.addValue(mPrefs.getString("usrnm", "")) + ","
					+ c.addKey("id") + c.addValue(sid.get(i)) + ","
					+ c.addKey("no") + c.addValue("" + qt.size()) + ","
					+ c.addKey("responses") + "[" + q1 + "]}";
			Log.w("Json String", s);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("res", s));
			JSONObject jn = null;
			if (isOnline()) {
				jn = jsonParser.makeHttpRequest(
						getResources().getString(R.string.upld), "POST",
						params, who);
				Log.w("", "");
			}

			if (jn != null) {
				try {
					Log.w("Uploaded status", "" + jn.getInt("state"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			/*
			 * for (int j = 0; j < qt.size(); j++) { int t = qt.get(j).getTyp();
			 * 
			 * String res = null; if (t == 1 || t == 2 || t == 4) { res = "" +
			 * dbh.getResponseI(sid.get(i), qt.get(j).getQno(), t); }
			 * 
			 * if (t == 3 || t == 5) { res = dbh.getResponseS(sid.get(i),
			 * qt.get(j).getQno(), t); }
			 * 
			 * List<NameValuePair> params = new ArrayList<NameValuePair>();
			 * params.add(new BasicNameValuePair("usrnm", mPrefs.getString(
			 * "usrnm", ""))); params.add(new BasicNameValuePair("id",
			 * sid.get(i)));
			 * 
			 * params.add(new BasicNameValuePair("qno", "" +
			 * qt.get(j).getQno())); params.add(new BasicNameValuePair("typ", ""
			 * + qt.get(j).getTyp())); params.add(new BasicNameValuePair("res",
			 * "" + res));
			 * 
			 * if (j == sid.size() - 1) { params.add(new
			 * BasicNameValuePair("fin", "1")); } else { params.add(new
			 * BasicNameValuePair("fin", "0")); } // sending modified data
			 * through http request // Notice that update product url accepts
			 * POST method if (isOnline()) {
			 * jsonParser.makeHttpRequest(getResources()
			 * .getString(R.string.upld), "POST", params, who); Log.w("",""); }
			 * 
			 * }
			 */

		}

	}

	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) who
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

}
