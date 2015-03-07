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

public class SometimesServicePO extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;	
	static Context who;
	public static SharedPreferences mPrefs;
	String ops;

	public SometimesServicePO() {
		super("SometimesServicePO");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		who = this;
		 FragPollsWithin.loadingMore = true;
		Log.w("ServicePO", "Started");
	    
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();	
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
	
		super.onDestroy();
		
		Log.w("ServiceP", "Ended");
		sld.close();
		dbh.close();		
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("In ServicePO", "Service PO");
		dlOPs();
	}

	void dlOPs()
	{
		final JSONParser jsonParser = new JSONParser();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("qid",FragPolls.qid));	
		JSONObject jn = null;
		
		if(isOnline())
		{
		jn = jsonParser.makeHttpRequest(
				getResources().getString(R.string.dlOps), "POST", params,
				who);
Log.w("JSON REcieved",""+jn);
try {
	
	if(jn!=null)
	{
	FragPolls.ops=jn.getString("ops");	
	try {
		((MainActivity) MainActivity.champu)
				.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							FragPolls.showOps();
						} catch (NullPointerException e) {
							Log.w("Exception", "MainActivity null");
						}
					}
				});
	} catch (NullPointerException e) {
		Log.w("Exception123", "MainActivity null");
	}
	}
	
} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		}
		else
		{
			FragPolls.noInternet();
			
		}
		
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
