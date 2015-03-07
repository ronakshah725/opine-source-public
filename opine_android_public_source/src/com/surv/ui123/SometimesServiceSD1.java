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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SometimesServiceSD1 extends IntentService {
	public SometimesServiceSD1() {
		super("SometimesServiceSD1");
		// TODO Auto-generated constructor stub
	}

	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	 static final int Uid=156652;
	 static Context who;
	 public static SharedPreferences mPrefs;
	 static String zip=null;
	 static String mar=null;
	 static String edu=null;
	 static String occ=null;
	 static String sal=null;


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("ServiceSD1", "Started");
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
		Log.w("ServiceSD1", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
SaveDemo sd=dbh.getDemo();
zip=sd.getZip();
mar=sd.getMar();
edu=sd.getEdu();
occ=sd.getOcc();
sal=sd.getSal();
sendSD();
				
	}

	static void sendSD() {
		
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("usrnm",mPrefs.getString("usrnm", "")));
			params.add(new BasicNameValuePair("zip",zip));
			params.add(new BasicNameValuePair("mar",mar));			
			params.add(new BasicNameValuePair("edu",edu));	
			params.add(new BasicNameValuePair("occ",occ));			
			params.add(new BasicNameValuePair("sal",sal));	
			try
			{
			if(isOnline())
			{
			JSONObject jn = jsonParser.makeHttpRequest(who.getResources().getString(R.string.upsd), "POST", params,who);
			Log.w("Updating by broadcast","hehehe");
			AccessSP.setDemo(who, 0);
			}	
			}
			catch (NullPointerException e)
			{
				Log.w("Null pointer", "SometimesServiceSD1");
				
			}
	}
	
	private static Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) MainActivity.champu.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

}