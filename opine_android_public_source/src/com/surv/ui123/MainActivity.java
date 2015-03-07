package com.surv.ui123;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gcm.GCMRegistrar;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import com.google.android.gms.common.AccountPicker;

public class MainActivity extends SherlockFragmentActivity {

	static LinearLayout MenuList;
	static LinearLayout RFrg;
	LinearLayout RedFrg;
	LinearLayout QuFrg;
	LinearLayout LFrg;
	TextView v1;
	static List<tilesTable> allTiles;
	static List<Voucher> allVouchers;
	static Expand d = new Expand();
	static Collapse c = new Collapse();
	public static FragmentTransaction transaction;
	public static FragmentManager fm;
	public static DatabaseHandler db;
	public static int cl_position;
	public static SharedPreferences mPrefs;
	public static IcsSpinner spin;
	public static boolean isSearch = false;
	public static boolean isSearchV = false;
	String[] pgs = { "Surveys", "Polls", "Vouchers", "Recharge" };
	String welcomeScreenShownPref = "welcomeScreenShown";
	static int selected = 0;
	static ImageButton buttn, button1;

	static TextView tv1, tv2, tx, usrnm, tvpts, tvval, tv3, tv4;
	static int cntr = 0;
	static SQLiteDatabase sdb;
	// static ImageView trp;
	// static TextView currpts;
	static LinearLayout h = null;
	static tilesTable curr;
	public static String fname;
	public static String checkNull = null;
	static EditText srchQue;
	public static Button Vsrch;
	static int select1 = 0;
	static int select2 = 0;
	static int select3 = 0;

	// static TextView curbal;
	// static TextView currbal;
	// static ImageView coin;

	public static String token = "uninitialised";
	GoogleAccountCredential credential;
	static Context champu = null;
	static RelativeLayout r1, r2, r3, r4, r5, r6;

	public boolean isExpanded() {
		return GlobVars.isExpanded;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPrefs = getSharedPreferences("ui123", MODE_PRIVATE);
		credential = GoogleAccountCredential.usingOAuth2(this, getResources()
				.getString(R.string.scope));
		Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref,
				false);
		checkNull = "opine";
		GlobVars.isExpanded = true;
		final LayoutInflater inflater = (LayoutInflater) getSystemService("layout_inflater");
		View view = inflater.inflate(R.layout.action_bar_edit_mode, null);
		getSupportActionBar().setCustomView(view);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		MainActivity.fm = getSupportFragmentManager();
		mPrefs.getString("usrnm", "");
		mPrefs.getString("fname", "");
		mPrefs.getString("lname", "");
		mPrefs.getInt("gen", 0);
		mPrefs.getString("bday", "");
		mPrefs.getInt("cur_pts", 0);
		mPrefs.getInt("cur_bal", 0);
		mPrefs.getInt("first_tym", 0);
		mPrefs.getInt("first_tymd", 0);
		mPrefs.getInt("democh", 0);
		Log.w("dekhoo", mPrefs.getString("fname", "") + "  hiiiii");
		champu = (Context) this;

		Log.w("Openeddddddd", "called");
		MenuList = (LinearLayout) findViewById(R.id.f1);
		RFrg = (LinearLayout) findViewById(R.id.f2);
		LFrg = (LinearLayout) findViewById(R.id.fragl);
		srchQue = (EditText) findViewById(R.id.srchQue);
		InputFilter[] FilterArray = new InputFilter[2];
		FilterArray[0] = new InputFilter.LengthFilter(20);
		FilterArray[1] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence arg0, int arg1, int arg2,
					Spanned arg3, int arg4, int arg5) {
				for (int i = arg1; i < arg2; i++) {
					if (!Character.isLetterOrDigit(arg0.charAt(i))
							|| arg0.length() > 10) {
						return "";
					}
				}

				return null;
			}
		};
		srchQue.setFilters(FilterArray);
		// srchVcr=(EditText)findViewById(R.id.srchVcr);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		GlobVars.screenwidth = metrics.widthPixels;
		GlobVars.screenheight = metrics.heightPixels;
		LayoutParams lyp = RFrg.getLayoutParams();
		lyp.width = GlobVars.screenwidth;
		RFrg.setLayoutParams(lyp);
		lyp = LFrg.getLayoutParams();
		lyp.width = GlobVars.screenwidth;
		LFrg.setLayoutParams(lyp);

		buttn = (ImageButton) findViewById(R.id.sort_btn);
		button1 = (ImageButton) findViewById(R.id.srch_btn);
		Vsrch = (Button) findViewById(R.id.vsrch);
		// tv1 = (TextView) findViewById(R.id.curr_pts);
		// tv2 = (TextView) findViewById(R.id.ressbal);
		// tv3 = (TextView) findViewById(R.id.cur2);
		tvpts = (TextView) findViewById(R.id.pts);
		tvpts.setText("" + mPrefs.getInt("cur_pts", 0));
		// tv1.setText("" + mPrefs.getInt("cur_pts", 0));
		// tv3.setText("" + mPrefs.getInt("cur_bal", 0));
		// tv2.setText("" + mPrefs.getInt("cur_bal", 0));
		GlobVars.current_pts = mPrefs.getInt("cur_pts", 0);
		GlobVars.current_bal = mPrefs.getInt("cur_bal", 0);

		/*
		 * tv1.setText("" + mPrefs.getInt("cur_pts", 0)); tv2.setText("" +
		 * mPrefs.getInt("cur_bal", 0)); tvpts.setText("" +
		 * mPrefs.getInt("cur_pts", 0)); tvval.setText("" +
		 * mPrefs.getInt("cur_bal", 0)); usrnm.setText("" +
		 * mPrefs.getString("usrnm", ""));
		 */
		// r1 = (RelativeLayout) findViewById(R.id.one);
		r2 = (RelativeLayout) findViewById(R.id.two);
		// r3 = (RelativeLayout) findViewById(R.id.three);
		r4 = (RelativeLayout) findViewById(R.id.four);
		r5 = (RelativeLayout) findViewById(R.id.five);
		r6 = (RelativeLayout) findViewById(R.id.six);
		tx = (TextView) findViewById(R.id.slt);
		h = (LinearLayout) findViewById(R.id.imageView1);
		MainActivity.db = new DatabaseHandler(MainActivity.champu);
		MainActivity.sdb = MainActivity.db.getWritableDatabase();
		MainActivity.db.deleteSub();
		if (mPrefs.getInt("first_tymd", 0) == 0) {
			MainActivity.db.saveDemo();
			AccessSP.setFirstTymd(MainActivity.champu, 1);
			Log.w("Save demo", "init demo");
		}
		spin = (IcsSpinner) findViewById(R.id.spinner);
		spin.setOnItemSelectedListener(new IcsAdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(IcsAdapterView<?> parent, View view,
					int position, long id) {
				String s = pgs[position];
				//Selected page executes automatically on start of app
				//this should not happen as the spinner was not even touched
				// transaction = fm.beginTransaction();
				Log.w("Selected Page", "" + s);
				if (!GlobVars.isExpanded)
					toggle();
				renderFrag(s);

			}

			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {

			}
		});

		MyCustomAdapter mc = new MyCustomAdapter(getSupportActionBar()
				.getThemedContext(), R.layout.row, pgs);
		mc.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		spin.setAdapter(mc);
		actionBarVisiblity(0);

		// getSupportActionBar().setDisplayShowCustomEnabled(true);
		// MainActivity.actionBarVisiblity(1);

		MainActivity.buttn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				MainActivity.showDialogButtonClick();

				if (!GlobVars.isExpanded) {
					MainActivity.toggle();

				}
			}
		});

		MainActivity.button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (!GlobVars.isExpanded)
					toggle();
				actionBarVisiblity(6);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInputFromWindow(
						v.getApplicationWindowToken(),
						InputMethodManager.SHOW_FORCED, 0);
				isSearch = true;
			}

		});

		MainActivity.Vsrch.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				if (!GlobVars.isExpanded)
					toggle();
				actionBarVisiblity(7);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInputFromWindow(
						v.getApplicationWindowToken(),
						InputMethodManager.SHOW_FORCED, 0);
				isSearchV = true;
			}

		});

		MainActivity.h.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(final View arg0) {

				((Activity) MainActivity.champu).runOnUiThread(new Runnable() {
					public void run() {
						if (isSearch) {
							actionBarVisiblity(1);
							isSearch = false;
						} else {
							MainActivity.toggle();
						}
					}
				});

			}
		});

		h.setEnabled(false);
		MainActivity.transaction = MainActivity.fm.beginTransaction();
		Fragment fr = new ProgressUI();
		MainActivity.transaction.add(R.id.f2, fr, "tiles");
		MainActivity.transaction.commit();

		if ("".equals(mPrefs.getString("fname", ""))) {

			Log.w(mPrefs.getString("usrnm", ""), "UserNm is");
			if ("".equals(mPrefs.getString("usrnm", "")))
				chooseAccount();
			else {
				Log.w("Calling", "Servic1");
				Intent intent = new Intent(this, SometimesService.class);
				startService(intent);
			}
		} else {
			// Never called in my case since the fname value in
			// sharedpreferences in always empty
			// maybe that's y the right fragment always stays blank

			Intent intent = new Intent(this, SometimesServiceU.class);
			startService(intent);
			transaction = fm.beginTransaction();
			Fragment frm = FragRight.newInstance();
			transaction.replace(R.id.f2, frm, "tiles");
			transaction.commit();
			Log.w("hua replae", "hee");
			getSupportActionBar().setHomeButtonEnabled(true);
			GlobVars.isDoing = false;
			actionBarVisiblity(1);
			h.setEnabled(true);
		}

		// following bundle is always null since there is no extras associated with the intent on launching the activity
		Intent i = getIntent();
		if (i != null) {
			Bundle extras = i.getExtras();
			if (extras == null)
				Log.w("Can't replace rfrag", "bundle object is null!");
			if (extras != null) {

				transaction = fm.beginTransaction();
				fr = new FragRight();
				transaction.replace(R.id.f2, fr, "tiles");
				transaction.addToBackStack(null);
				transaction.commit();
				Log.w("Getting intent", "Getting intent");
				transaction = fm.beginTransaction();
				fr = new FragQues();
				transaction.replace(R.id.f2, fr, "quests");
				transaction.addToBackStack(null);
				transaction.commit();

			}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.w("yessssssss", resultCode + " " + Activity.RESULT_CANCELED + " "
				+ Activity.RESULT_OK);
		switch (requestCode) {

		case 2:
			if (resultCode == Activity.RESULT_OK && data != null
					&& data.getExtras() != null) {

				credential.setSelectedAccountName(data.getExtras().getString(
						AccountManager.KEY_ACCOUNT_NAME));
				Log.w("" + AccountManager.KEY_ACCOUNT_NAME, "Selcted Acnt");
				AccessSP.setUsrnm(MainActivity.champu, data.getExtras()
						.getString(AccountManager.KEY_ACCOUNT_NAME));

				Log.w("Calling", "Serviceeeee2");
				/*
				 * Intent intent = new Intent(this, SometimesService.class);
				 * intent.putExtra("which", "1"); startService(intent);
				 */
				if (GCMRegistrar.getRegistrationId(getApplicationContext())
						.equals("")) {
					GCMRegistrar.register(getApplicationContext(),
							getResources().getString(R.string.projno));
					Log.w(GCMRegistrar
							.getRegistrationId(getApplicationContext()),
							"Registration id");
				}

			} else {
				finish();
			}
			break;

		case 11:

			if (resultCode == Activity.RESULT_CANCELED) {
				Log.w("tokennsndnddkd", token);
				finish();
			}

			if (resultCode == Activity.RESULT_OK) {
				{
					Log.w("Calling", "Serviceeeee3");
					if (GCMRegistrar.getRegistrationId(getApplicationContext())
							.equals("")) {
						GCMRegistrar.register(getApplicationContext(),
								getResources().getString(R.string.projno));
						Log.w(GCMRegistrar
								.getRegistrationId(getApplicationContext()),
								"Registration id");
					}
					MainActivity.showDemo();
					Intent intent = new Intent(this, SometimesService.class);
					startService(intent);

					Log.w("hoho", token);
				}
				// makeRequ();

			}
			break;
		}

	}

	static void showDialogButtonClick() {

		AlertDialog.Builder builder = new AlertDialog.Builder(champu);
		builder.setTitle("Sort by ..");

		final CharSequence[] choiceList = { "Survey ID", "Company Name",
				"Submissions left", "Maximum Points" };

		builder.setSingleChoiceItems(choiceList, selected,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selected = which;
						allTiles.clear();
						allTiles = db.getAllTiles(which + 1);
						GlobVars.sort = which + 1;
						((MainActivity) MainActivity.champu)
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										FragRight.adapter
												.notifyDataSetChanged();
									}
								});

						selected = which;
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	@SuppressWarnings("unused")
	void updateData() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File direct = new File(getExternalFilesDir(null), "/WhattoUp");

			if (!direct.exists()) {
				try {
					direct.createNewFile();

				} catch (IOException e) {

					Log.w("Testing", "Error Creating file");
				}
			}

			try {
				FileOutputStream f = new FileOutputStream(direct);
				f.write(("x2,9000\n").getBytes());
				f.write(("x1,2000\n").getBytes());
				f.close();
			} catch (Exception e) {

			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(direct));

				String line;

				while ((line = reader.readLine()) != null) {

					String s[] = line.split(",");
					Log.w("no 1", "" + s[0] + s[1]);
					int i;
					for (i = 0; i < allTiles.size(); i++) {
						if (allTiles.get(i).getId().equals(s[0]))
							break;
					}

					if (i != allTiles.size()) {
						allTiles.get(i).setNodl(Integer.parseInt(s[1]));
						Log.w("uodateee", s[0] + s[1]);
						db.updatetilesTable(allTiles.get(i));
					}
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						FragRight.adapter.notifyDataSetChanged();
					}
				});

			} catch (Exception ex) {
				Log.w("errrr", "errrr");
			}
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Toast.makeText(this, "sdCard Read Only", Toast.LENGTH_SHORT).show();
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			Toast.makeText(this, "sdCard is not writable and readable",
					Toast.LENGTH_SHORT).show();
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

	}

	@SuppressWarnings("unused")
	void deleteData() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		Log.w("Delete", "ffggdaffa");
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File direct = new File(getExternalFilesDir(null), "/WhattoDel");

			if (!direct.exists()) {
				try {
					direct.createNewFile();

				} catch (IOException e) {

					Log.w("Testing", "Error Creating file");
				}
			}

			try {
				FileOutputStream f = new FileOutputStream(direct);
				f.write(("x4,\n").getBytes());
				f.write(("#\n").getBytes());
				f.write(("v2,v6,\n").getBytes());
				f.write(("#\n").getBytes());
				f.close();
			} catch (Exception e) {

			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(direct));

				String line;
				line = reader.readLine();
				Log.w("gggggggggggggggggggg", line);
				while (!line.equals("#")) {
					Log.w("What", line);
					String s[] = line.split(",");
					for (int i = 0; i < s.length; i++) {
						db.deletetab("tilesInfo", s[i]);
						db.deletetab("Questions", s[i]);
						db.deletetab("vt", s[i]);
					}
					line = reader.readLine();
				}
				line = reader.readLine();
				while (!line.equals("#")) {
					Log.w("What", line);
					String s[] = line.split(",");
					for (int i = 0; i < s.length; i++) {
						db.deletetab("Vouchers", s[i]);
					}
					line = reader.readLine();
				}

			} catch (Exception ex) {

			}
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Toast.makeText(this, "sdCard Read Only", Toast.LENGTH_SHORT).show();
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			Toast.makeText(this, "sdCard is not writable and readable",
					Toast.LENGTH_SHORT).show();
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

	}

	public static void toggle() {

		((SherlockFragmentActivity) MainActivity.champu).getSupportActionBar()
				.setHomeButtonEnabled(false);
		GlobVars.isDoing = true;
		h.setEnabled(false);
		FragLeft.b.setEnabled(false);
		FragLeft.b1.setEnabled(false);
		FragLeft.b2.setEnabled(false);

		GlobVars.isExpanded = !GlobVars.isExpanded;
		if (!GlobVars.isExpanded) {
			RFrg.clearFocus();
			RFrg.requestFocus();
			d.getParams(MenuList);
		} else {

			c.getParams(MenuList);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		boolean bl = isOnline();
		Log.w("jujuju", "opened");
		if (bl) {
			Intent in = new Intent(MainActivity.champu, SometimesServiceU.class);
			in.putExtra("which", "2");
			MainActivity.champu.startService(in);
		}
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onStop();
		sdb.close();
		db.close();
		checkNull = null;
		Log.w("Called", "destroy");
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_MENU) & (!GlobVars.isDoing)) {
			if (!isSearch && !isSearchV)
				toggle();
			return true;

		} else if ((keyCode == KeyEvent.KEYCODE_BACK) & (!GlobVars.isDoing)) {
			if (!GlobVars.isExpanded) {
				toggle();
				return true;
			} else if (GlobVars.isExpanded) {
				if (isSearch) {
					actionBarVisiblity(1);
					allTiles.clear();
					allTiles = db.getAllTiles(GlobVars.sort);
					MainActivity.db.setsrchcnt(allTiles.size());
					Log.w("Back from search", "htyhg");
					((MainActivity) MainActivity.champu)
							.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									FragRight.adapter.notifyDataSetChanged();
								}
							});
					isSearch = false;
					return true;
				}
				if (isSearchV) {
					actionBarVisiblity(3);
					srchQue.setVisibility(View.INVISIBLE);
					Vsrch.setVisibility(View.VISIBLE);
					allVouchers.clear();
					allVouchers = db.getAllVouchers();
					MainActivity.db.setsrchcnt(allVouchers.size());
					Log.w("Back from search", "htyhg");
					((MainActivity) MainActivity.champu)
							.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									FragRedeem.adapter.notifyDataSetChanged();
								}
							});
					isSearchV = false;
					return true;
				}
				if (FragPolls.fm != null
						&& FragPolls.fm.getBackStackEntryCount() > 0) {
					FragPolls.fm.popBackStack();
					return true;
				}
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	public static void rePopulate() {
		allTiles = db.getAllTiles(0);
	}

	public static void rePopulateV() {
		allVouchers = db.getAllVouchers();

	}

	public static void actionBarVisiblity(int c) {
		switch (c) {
		case 0:
			// tv1.setText("" + GlobVars.current_pts);
			// r1.setVisibility(View.INVISIBLE);
			spin.setVisibility(View.INVISIBLE);
			r2.setVisibility(View.INVISIBLE);
			// r3.setVisibility(View.INVISIBLE);
			r4.setVisibility(View.INVISIBLE);
			r5.setVisibility(View.INVISIBLE);
			// r6.setVisibility(View.INVISIBLE);
			r6.setVisibility(View.INVISIBLE);
			break;
		case 1:
			// tv1.setText("" + GlobVars.current_pts);
			// r1.setVisibility(View.VISIBLE);
			spin.setSelection(0);
			spin.setVisibility(View.VISIBLE);
			r2.setVisibility(View.VISIBLE);
			// r3.setVisibility(View.INVISIBLE);
			r4.setVisibility(View.INVISIBLE);
			r5.setVisibility(View.INVISIBLE);
			srchQue.setVisibility(View.INVISIBLE);
			// r6.setVisibility(View.INVISIBLE);
			r6.setVisibility(View.INVISIBLE);
			break;
		case 2:
			// tv1.setText("" + GlobVars.current_pts);
			// r1.setVisibility(View.VISIBLE);
			spin.setVisibility(View.INVISIBLE);
			r2.setVisibility(View.INVISIBLE);
			// r3.setVisibility(View.INVISIBLE);
			r4.setVisibility(View.INVISIBLE);
			r5.setVisibility(View.INVISIBLE);
			srchQue.setVisibility(View.INVISIBLE);
			// r6.setVisibility(View.INVISIBLE);
			r6.setVisibility(View.INVISIBLE);
			break;
		case 3:
			// tv2.setText("" + GlobVars.current_bal);
			// r1.setVisibility(View.INVISIBLE);
			spin.setSelection(2);
			spin.setVisibility(View.VISIBLE);
			r2.setVisibility(View.INVISIBLE);
			// r3.setVisibility(View.VISIBLE);
			r4.setVisibility(View.VISIBLE);
			r5.setVisibility(View.INVISIBLE);
			srchQue.setVisibility(View.INVISIBLE);
			// r6.setVisibility(View.INVISIBLE);
			r6.setVisibility(View.INVISIBLE);
			break;
		case 4:
			// tv3.setText("" + GlobVars.current_bal);

			// r1.setVisibility(View.INVISIBLE);
			spin.setSelection(3);
			spin.setVisibility(View.VISIBLE);
			r2.setVisibility(View.INVISIBLE);
			// r3.setVisibility(View.INVISIBLE);
			r4.setVisibility(View.INVISIBLE);
			r5.setVisibility(View.VISIBLE);
			srchQue.setVisibility(View.INVISIBLE);
			// r6.setVisibility(View.VISIBLE);
			r6.setVisibility(View.INVISIBLE);
			break;
		case 6:
			spin.setVisibility(View.INVISIBLE);
			r2.setVisibility(View.INVISIBLE);
			srchQue.setVisibility(View.VISIBLE);
			srchQue.requestFocus();
			srchQue.setHint("Survey ID/Name");
			srchQue.setText("");
			r6.setVisibility(View.INVISIBLE);
			break;
		case 7:
			Vsrch.setVisibility(View.INVISIBLE);
			srchQue.setVisibility(View.VISIBLE);
			srchQue.requestFocus();
			srchQue.setText("");
			srchQue.setHint("Voucher Name");
			spin.setVisibility(View.INVISIBLE);
			r6.setVisibility(View.INVISIBLE);

			break;
		case 8:
			spin.setVisibility(View.VISIBLE);
			r2.setVisibility(View.INVISIBLE);
			r4.setVisibility(View.INVISIBLE);
			r5.setVisibility(View.INVISIBLE);
			r6.setVisibility(View.VISIBLE);
			break;
		case 9:
			spin.setVisibility(View.INVISIBLE);
			r2.setVisibility(View.INVISIBLE);
			r4.setVisibility(View.INVISIBLE);
			r5.setVisibility(View.INVISIBLE);
			r6.setVisibility(View.INVISIBLE);
			break;

		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	public static void renderFrag(String s) {
		transaction = fm.beginTransaction();
		if (s.equals("Surveys")) {
			spin.setSelection(0);
			if (MainActivity.fm.findFragmentByTag("tiles") != null) {
				if (MainActivity.fm.getBackStackEntryCount() > 0) {
					MainActivity.fm.popBackStack();
				}
			}
		} else if (s.equals("Polls")) {
			spin.setSelection(1);
			if (MainActivity.fm.findFragmentByTag("polls") == null) {
				Log.w("Called Frgmnet Change", s);
				if (MainActivity.fm.getBackStackEntryCount() > 0)
					MainActivity.fm.popBackStack();
				Log.w("hahha", "hahah");
				FragPolls frm = new FragPolls();
				transaction.replace(R.id.f2, frm, "polls");
				transaction.addToBackStack(null);
				transaction.commit();
			}

		} else if (s.equals("Vouchers")) {
			spin.setSelection(2);
			if (MainActivity.mPrefs.getInt("first_tym", 0) == 0) {
				Fragment fr = new ProgressUI();

				if (MainActivity.fm.getBackStackEntryCount() > 0)
					MainActivity.fm.popBackStack();

				MainActivity.transaction = MainActivity.fm.beginTransaction();
				MainActivity.transaction.replace(R.id.f2, fr, "progressui");
				Log.w("Placed", "Progress UI Placed");
				MainActivity.transaction.addToBackStack(null);
				MainActivity.transaction.commit();
				Log.w("Placed", "Progress UI Committed");
				Intent intent = new Intent(MainActivity.champu,
						SometimesServiceDV.class);
				MainActivity.champu.startService(intent);

			} else {
				if (MainActivity.fm.findFragmentByTag("redeem") == null) {

					if (MainActivity.fm.getBackStackEntryCount() > 0)
						MainActivity.fm.popBackStack();

					FragRedeem frm = new FragRedeem();
					MainActivity.transaction.replace(R.id.f2, frm, "redeem");
					MainActivity.transaction.addToBackStack(null);
					MainActivity.transaction.commit();
				}
			}

		} else if (s.equals("Recharge")) {

			spin.setSelection(3);
			if (MainActivity.fm.findFragmentByTag("recharge") == null) {
				Log.w("Called Frgmnet Change", s);
				if (MainActivity.fm.getBackStackEntryCount() > 0)
					MainActivity.fm.popBackStack();
				Log.w("hahha", "hahah");
				FragRecharge frm = new FragRecharge();
				transaction.replace(R.id.f2, frm, "recharge");
				transaction.addToBackStack(null);
				transaction.commit();
			}
		} else if (s.equals("LogEnt")) {
			if (MainActivity.fm.findFragmentByTag("logent") == null) {

				if (MainActivity.fm.getBackStackEntryCount() > 0)
					MainActivity.fm.popBackStack();

				Fragment frm = new LogEnt();
				MainActivity.transaction.replace(R.id.f2, frm, "logent");
				MainActivity.transaction.addToBackStack(null);
				MainActivity.transaction.commit();
			}
		}

	}

	public void dispHelp() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Help Screen")
				.setMessage(
						"About Us\nssss\nsaferea\ndfafgbbghfghfdgfdfsfhjbhvgghftyujmnbfghjn cxghjm dfghbjnmiuhbnvcfdrewasxcvbnkhsesfcgvhbjnkbhgfcgvhbj")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}

				}).show();

	}

	private void chooseAccount() {

		startActivityForResult(credential.newChooseAccountIntent(), 2);
	}

	static void updtTiles() {
		MainActivity.allTiles.clear();
		MainActivity.allTiles = db.getAllTiles(GlobVars.sort);
		FragRight.adapter.notifyDataSetChanged();
	}

	public class MyCustomAdapter extends ArrayAdapter<String> {

		public MyCustomAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			TextView label = (TextView) row.findViewById(R.id.pg);
			label.setText(pgs[position]);
			return row;
		}
	}

	public static void showDemo() {
		final Dialog dialog = new Dialog(MainActivity.champu);
		dialog.setContentView(R.layout.demogr);
		Button dialogButton = (Button) dialog.findViewById(R.id.skip);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		SaveDemo sd = MainActivity.db.getDemo1();
		final EditText ezip = (EditText) dialog.findViewById(R.id.ezip);
		ezip.setText(sd.getZip());
		Button bmar = (Button) dialog.findViewById(R.id.bmar);
		final TextView tmar = (TextView) dialog.findViewById(R.id.tmar);
		tmar.setText(sd.getMar());
		bmar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.champu);
				builder.setTitle("Marital Status");

				final CharSequence[] choiceList = { "Single", "Married",
						"Widowed/Divorced/Separated", "Living together" };

				builder.setSingleChoiceItems(choiceList, select1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								select1 = which;
								switch (select1) {
								case 0:
									tmar.setText("Single");
									break;
								case 1:
									tmar.setText("Married");
									break;
								case 2:
									tmar.setText("Widowed/Divorced/Separated");
									break;
								case 3:
									tmar.setText("Living together");
									break;
								}
								dialog.dismiss();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		Button bedu = (Button) dialog.findViewById(R.id.bedu);
		final TextView tedu = (TextView) dialog.findViewById(R.id.tedu);
		tedu.setText(sd.getEdu());
		bedu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.champu);
				builder.setTitle("Eduction");

				final CharSequence[] choiceList = { "Primary school",
						"High school or equivalent", "Some college",
						"Bachelor's degree", "Master's degree", "PhD" };

				builder.setSingleChoiceItems(choiceList, select1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								select1 = which;
								switch (select1) {
								case 0:
									tedu.setText("Primary school");
									break;
								case 1:
									tedu.setText("High school or equivalent");
									break;
								case 2:
									tedu.setText("Some college");
									break;
								case 3:
									tedu.setText("Bachelor's degree");
									break;
								case 4:
									tedu.setText("Master's degree");
									break;
								case 5:
									tedu.setText("PhD");
									break;
								}
								dialog.dismiss();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		Button bocc = (Button) dialog.findViewById(R.id.bocc);
		final TextView tocc = (TextView) dialog.findViewById(R.id.tocc);
		tocc.setText(sd.getOcc());
		bocc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.champu);
				builder.setTitle("Occupation");

				final CharSequence[] choiceList = { "Employed full time",
						"Not employed", "Retired", "Student", "Homemaker",
						"Prefer not to answer" };

				builder.setSingleChoiceItems(choiceList, select2,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								select2 = which;
								switch (select2) {
								case 0:
									tocc.setText("Employed full time");
									break;
								case 1:
									tocc.setText("Not employed");
									break;
								case 2:
									tocc.setText("Retired");
									break;
								case 3:
									tocc.setText("Student");
									break;
								case 4:
									tocc.setText("Homemaker");
									break;
								case 5:
									tocc.setText("Prefer not to answer");
									break;
								}
								dialog.dismiss();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		Button bsal = (Button) dialog.findViewById(R.id.bsal);

		final TextView tsal = (TextView) dialog.findViewById(R.id.tsal);
		tsal.setText(sd.getSal());
		bsal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.champu);
				builder.setTitle("Salary Per Annum");

				final CharSequence[] choiceList = { "Nil", "Upto Rs. 2,00,000",
						"Rs 2,00,001 to Rs 5,00,000",
						"Rs 5,00,001 to Rs 10,00,000",
						"Rs 10,00,001 to Rs 20,00,000",
						"Rs 20,00,001 and above" };

				builder.setSingleChoiceItems(choiceList, select3,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								select3 = which;
								switch (select3) {
								case 0:
									tsal.setText("Nil");
									break;
								case 1:
									tsal.setText("Upto Rs. 2,00,000");
									break;
								case 2:
									tsal.setText("Rs 2,00,001 to Rs 5,00,000");
									break;
								case 3:
									tsal.setText("Rs 5,00,001 to Rs 10,00,000");
									break;
								case 4:
									tsal.setText("Rs 10,00,001 to Rs 20,00,000");
									break;
								case 5:
									tsal.setText("Rs 20,00,001 and above");
									break;

								}
								dialog.dismiss();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		Button dialogButton1 = (Button) dialog.findViewById(R.id.Save);
		dialogButton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccessSP.setDemo(MainActivity.champu, 1);
				Intent in = new Intent(MainActivity.champu,
						SometimesServiceSaveDemo.class);
				in.putExtra("zip", ezip.getText().toString());
				in.putExtra("mar", tmar.getText());
				in.putExtra("edu", tedu.getText());
				in.putExtra("occ", tocc.getText());
				in.putExtra("sal", tsal.getText());
				MainActivity.champu.startService(in);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}
