package com.surv.ui123;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SometimeServiceSP extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	 static final int Uid=156652;
	 static Context who;
	 public static SharedPreferences mPrefs;
	 static String sid=null;

	public SometimeServiceSP() {
		super("SometimesServiceSP");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("ServiceSP", "Started");
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();
		nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Uid);
		who=this;
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);		 
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		Log.w("ServiceSP", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {			
sid=intent.getExtras().getString("id");		
					sendSP();
				
	}

	static void sendSP() {
		
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("usrnm",mPrefs.getString("usrnm", "")));			
			params.add(new BasicNameValuePair("id", "" +sid ));			
			Log.w("Sending SP",""+sid+" "+mPrefs.getString("usrnm", ""));
			
			JSONObject jn = jsonParser.makeHttpRequest(
					who.getResources().getString(R.string.upsp), "POST", params,who);	
	}

}