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
import android.util.Log;

public class SometimesServiceNP extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	public static SharedPreferences mPrefs;
	public static Context who;

	public SometimesServiceNP() {
		super("SometimesServiceNP");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("ServiceNP", "Started");
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
		Log.w("ServiceNP", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		String ques = intent.getExtras().getString("ques");
		String ops = intent.getExtras().getString("options");
		String catnm=intent.getExtras().getString("catnm");
		sendNP(ques, ops, catnm);
	}

	static void sendNP(String ques, String ops, String catnm) {

		JSONParser jsonParser = new JSONParser();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usrnm", mPrefs
				.getString("usrnm", "")));
		params.add(new BasicNameValuePair("catnm", catnm));
		params.add(new BasicNameValuePair("ques", "" + ques));
		params.add(new BasicNameValuePair("ops", "" + ops));
		Log.w("Sending NP",
				"" + ques + ops + " " + mPrefs.getString("usrnm", ""));

		JSONObject jn = jsonParser.makeHttpRequest(who.getResources()
				.getString(R.string.upnp), "POST", params, who);
		if(jn!=null)
		{
			String qid="";
			try {
				qid = jn.getString("qid");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pollsWithin p=new pollsWithin(catnm,qid, ques,0, ops,"",1);
			dbh.addPollsn(p);
		}
		try {
			Log.w("State Service NP",""+jn.getInt("state"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}