package com.surv.ui123;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SometimesServiceDP extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	 static final int Uid=156652;
	 static Context who;
	 public static SharedPreferences mPrefs;
	 static String sid=null;

	public SometimesServiceDP() {
		super("SometimesServiceDP");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("ServiceDP", "Started");
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
		Log.w("ServiceDP", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {			
sid=intent.getExtras().getString("qid");		
					sendDP();
				
	}

	static void sendDP() {
		
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("usrnm",mPrefs.getString("usrnm", "")));			
			params.add(new BasicNameValuePair("qid", "" +sid ));			
			Log.w("Sending DP",""+sid+" "+mPrefs.getString("usrnm", ""));
			
			JSONObject jn = jsonParser.makeHttpRequest(
					who.getResources().getString(R.string.updp), "POST", params,who);	
			if(jn!=null)
			{
				try {
					if(jn.getInt("state")==1)
					{
						dbh.deletepoll(sid);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

}