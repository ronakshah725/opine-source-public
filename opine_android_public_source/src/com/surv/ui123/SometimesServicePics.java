package com.surv.ui123;

import java.io.File;
import java.io.IOException;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class SometimesServicePics extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	static final int Uid = 156652;
	static Context who;
	public static SharedPreferences mPrefs;
	static String sid = null;

	public SometimesServicePics() {
		super("SometimesServicePics");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("ServicePics", "Started");
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Uid);
		who = this;
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		Log.w("ServicePics", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		getPics();

	}

	static void getPics() {

		File folder = new File(Environment.getExternalStorageDirectory()
				.toString()
				+ who.getResources().getString(R.string.opineimages)
				+ who.getResources().getString(R.string.tilepics));
		folder.mkdirs();
		File noMedia = new File(folder.getAbsolutePath() + "/.nomedia");
		try {
			noMedia.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] ids = dbh.getAllSurvIds();
		if (ids != null) {
			if (ids.length != 0) {
				new DlPics(ids, 1, who);
			}
		}
	}

}