package com.surv.ui123;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SometimesServiceSPM extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;	
	static Context who;
	public static SharedPreferences mPrefs;
	String ops;

	public SometimesServiceSPM() {
		super("SometimesServiceSPM");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		who = this;
		 FragPollsWithin.loadingMore = true;
		Log.w("ServiceSPM", "Started");
	    
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();	
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
	
		super.onDestroy();
		
		Log.w("ServiceSPM", "Ended");
		sld.close();
		dbh.close();		
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("In ServiceSPM", "Service SPM");
		upSPM();
	}

	void upSPM()
	{
		final JSONParser jsonParser = new JSONParser();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("qid",FragPolls.qid));
		params.add(new BasicNameValuePair("usrnm",mPrefs.getString("usrnm", "")));
		JSONObject jn = null;
		
		if(isOnline())
		{
		jn = jsonParser.makeHttpRequest(
				getResources().getString(R.string.upSPM), "POST", params,
				who);		
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
