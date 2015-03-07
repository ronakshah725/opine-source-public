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

public class SometimesServiceR extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	 static final int Uid=156652;
	 static Context who;
	 public static SharedPreferences mPrefs;

	public SometimesServiceR() {
		super("SometimesServiceR");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("ServiceR", "Started");
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
		Log.w("ServiceR", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {			
Log.w("Recharge","hhhhhdhdhddhhd");			
				int c = SometimesServiceR.dbh.getRchRcnt();
				if (c != 0) {
					requestRecharge();
				}
	}

	static void requestRecharge() {
		List<RchReq> req = SometimesServiceR.dbh.getRchTempR();

		for (int i = 0; i < req.size(); i++) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("usrnm",mPrefs
					.getString("usrnm", "")));
		
			params.add(new BasicNameValuePair("amt", "" + req.get(i).getAmt()));
			params.add(new BasicNameValuePair("serv", "" + req.get(i).getServ()));
			params.add(new BasicNameValuePair("phno", "" + req.get(i).getPhno()));
			params.add(new BasicNameValuePair("ts", "" + req.get(i).getTs()));
			Log.w("Recharge","hddhhd"+ req.get(i).getAmt()+" "+req.get(i).getServ()+" "+req.get(i).getPhno()+" "+req.get(i).getTs());
			JSONObject jn = jsonParser.makeHttpRequest(
					who.getResources().getString(R.string.rechReq), "POST", params,who);
			

			if (jn == null) {
				Log.w("No Internet", "Could not upload recharge request");
			} else {
				try {
					if (jn.getInt("state") == 1) {
					SometimesServiceR.dbh.delRch(req.get(i).getAmt(),req.get(i).getServ(),req.get(i).getPhno(), req
								.get(i).getTs());
						Log.w("Recharge","h57575");						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}