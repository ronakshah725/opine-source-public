package com.surv.ui123;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

public class SometimesServiceP extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;	
	static Context who;
	public static SharedPreferences mPrefs;

	public SometimesServiceP() {
		super("SometimesServiceP");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		who = this;
		 FragPollsWithin.loadingMore = true;
		Log.w("ServiceP", "Started");
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();	
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
	
		super.onDestroy();
		// TODO Auto-generated method stub
		if(FragPolls.mult==1)
		{
		FragPolls.putPolls();
		}
		else
		{
		try {
			((MainActivity) MainActivity.champu)
					.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								FragPollsWithin.adapter.notifyDataSetChanged();							
								
							} catch (NullPointerException e) {
								Log.w("Exception", "MainActivity null");
							}
						}
					});
		} catch (NullPointerException e) {
			Log.w("Exception123", "MainActivity null");
		}

		}
		FragPolls.mult=FragPolls.mult+1;
		Log.w("ServiceP", "Ended");
		sld.close();
		dbh.close();
		FragPollsWithin.loadingMore = false;
		
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("In ServiceP", "Service P");
		dlQues();
	}

	void dlQues()
	{
		final JSONParser jsonParser = new JSONParser();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usrnm",mPrefs.getString("usrnm", "")));
		params.add(new BasicNameValuePair("catnm",FragCat.currCat));
		params.add(new BasicNameValuePair("mult",""+FragPolls.mult));
		String ihave="0";
		Log.w("unam",mPrefs.getString("usrnm", "")+" "+FragCat.currCat+" "+FragPolls.mult);
		for(int i=0;i<FragPolls.allpollswithin.size();i++)
		{
			ihave=ihave+","+FragPolls.allpollswithin.get(i).getQid();
		}
		
		params.add(new BasicNameValuePair("ihave",""+ihave));
		Log.w("ihave","ihave:"+ihave);
		JSONObject jn = null;
		
		if(isOnline())
		{
		jn = jsonParser.makeHttpRequest(
				getResources().getString(R.string.dlQues), "POST", params,
				who);
Log.w("JSON REcieved",""+jn);
//Insert into db
Log.w("Mult",""+FragPolls.mult);
try {
	FragPolls.nomore=jn.getInt("nomore");
	if(FragPolls.nomore==0)
	{
		JSONArray ja=jn.getJSONArray("quesall");
	if(ja!=null)
	{
	for(int i=0; i<ja.length();i++)
	{
		pollsWithin p= new pollsWithin(ja.getJSONObject(i).getString("id"), ja.getJSONObject(i).getString("qid"), ja.getJSONObject(i).getString("ques"), ja.getJSONObject(i).getInt("nrpl"),"","",0);
		FragPolls.allpollswithin.add(p);
	}	
	}
	}
	else
	{
		try {
			((MainActivity) MainActivity.champu)
					.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								FragPollsWithin.list1.removeFooterView(FragPollsWithin.v1);				
								
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
