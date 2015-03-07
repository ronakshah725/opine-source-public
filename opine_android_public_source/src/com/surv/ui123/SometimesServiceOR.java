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

public class SometimesServiceOR extends IntentService {
	
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;	
	static Context who;
	public static SharedPreferences mPrefs;
	public SometimesServiceOR() {
		super("SeviceOR");
		// TODO Auto-generated constructor stub
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
	
		Log.w("ServicePO", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("In ServicePO", "Service PO");
		upOR();
	}

	void upOR()
	{
		final JSONParser jsonParser = new JSONParser();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("qid",FragPolls.qid));	
		params.add(new BasicNameValuePair("usrnm",mPrefs.getString("usrnm", "")));
		params.add(new BasicNameValuePair("res",""+FragPolls.res));
		JSONObject jn1 = null;
		
		if(isOnline())
		{
		jn1 = jsonParser.makeHttpRequest(
"http://www.acjs.co/opInEtemp/pagal/upkl.php", "POST", params,
				who);
Log.w("JSON REcieved Service  OR","Pollrply::"+jn1+"  "+getResources().getString(R.string.upOR));
if(jn1!=null)
{
	try {
		Log.w("JSON sts: ","JSON sts"+jn1.getInt("state"));
	} catch (JSONException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
try {
	int st=jn1.getInt("state");
	Log.w("Service OR","sts: "+st);
	if(st==1)
	{
		pollsWithin p=new pollsWithin(FragCat.currCat, FragPolls.qid, FragPolls.ques, 0, FragPolls.ops,"",0);
		for(int j=0;j<FragPolls.allpollswithin.size();j++)
		{
			if((FragPolls.allpollswithin.get(j).getQid()).equals(FragPolls.qid))
			{
				FragPolls.allpollswithin.remove(j);
				break;
			}
		}
		Log.w("In service OR","Pollrply: "+p.getQid());
		dbh.addPolls(p);
		
	}
} catch (JSONException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
Log.w("Response uploaded","");
try {
	((MainActivity) MainActivity.champu)
			.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						FragPolls.allpollswithin.clear();
						FragPolls.mult=1;
						FragPollsWithin.adapter.notifyDataSetChanged();
						FragPolls.fm.popBackStack();

					} catch (NullPointerException e) {
						Log.w("Exception", "MainActivity null");
					}
				}
			});
} catch (NullPointerException e) {
	Log.w("Exception123", "MainActivity null");
}
}
else
{

Log.w("Error Uploading","Poll response");
	
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
