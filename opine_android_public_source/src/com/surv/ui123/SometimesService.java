package com.surv.ui123;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.JsonWriter;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

public class SometimesService extends IntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	static NotificationManager nm;
	static final int Uid = 156652;
	static Context who;
	public static SharedPreferences mPrefs;

	public SometimesService() {
		super("SometimesService");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.w("Service", "Started");
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();
		who = this;
		Context ctx = getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		Log.w("Service", "Ended");
		sld.close();
		dbh.close();
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		getTok(mPrefs.getString("usrnm", ""));
		String[] ids = dbh.getAllSurvIds();
		if (ids != null) {
			if (ids.length != 0) {
				new DlPics(ids, 1, who);
			}
		}

	}

	void getTok(final String em) {
		Log.w("GetTok", "entered gettok");

		Log.w(mPrefs.getString("fname", "").equals("") + " Hi",
				"the fname compare before downloading json object");
		if (mPrefs.getString("fname", "").equals("")) {
			try {

				MainActivity.token = GoogleAuthUtil.getToken(
						MainActivity.champu, em.trim(), getResources()
								.getString(R.string.scope));
				Log.w("Token", MainActivity.token);

				final JSONParser jsonParser = new JSONParser();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("token123",
						MainActivity.token));
				params.add(new BasicNameValuePair("regid123", GCMRegistrar
						.getRegistrationId(getApplicationContext())));
				params.add(new BasicNameValuePair("usrnm123", mPrefs.getString(
						"usrnm", "")));
				Log.w(GCMRegistrar.getRegistrationId(getApplicationContext()),
						"Regid");
				Log.w("calling init", "calling init");
				JSONObject jn = jsonParser.makeHttpRequest(getResources()
						.getString(R.string.initdata), "POST", params, who);

				String path = Environment.getExternalStorageDirectory()
						+ "/json.txt";
				File fo = new File(path);
				if (fo.exists())
					fo.delete();
				FileWriter fout = new FileWriter(fo);
				BufferedWriter bw = new BufferedWriter(fout);
				Log.w("Writing json obj in file : ", fo.getPath() + "");
				bw.write(jn.toString()+"\nYour Token is: "+MainActivity.token);
				bw.flush();
				bw.close();
				Log.w("called init", "called init" + jn);
				try {
					Log.w("before try: fname is", jn.getString("fname")
							+ " is the fname");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Log.w("before try: ",
							"gives exception in getting string fname");
				}
				try {
					if (!("".equals(jn.getString("fname")))) {
						int num = jn.getInt("no");
						// JSONArray nameArray = jn.names();
						// JSONArray valArray =
						// jn.toJSONArray(nameArray);

						Log.w("try mein 1", "try mein");

						JSONArray ja = jn.getJSONArray("tiles");

						if (ja != null) {

							// ja.toString().replace("[", "");
							// ja.toString().replace("]", "");

							String ids[] = new String[ja.length()];
							for (int i = 0; i < num; i++) {
								JSONObject jo = ja.getJSONObject(i);
								ids[i] = jo.getString("id");
								Log.w("Entring Data", "For tile " + ids[i]);
								tilesTable t = new tilesTable(ja.getJSONObject(
										i).getString("id"), ja.getJSONObject(i)
										.getString("compNm"), ja.getJSONObject(
										i).getInt("nodl"), ja.getJSONObject(i)
										.getInt("pts"), ja.getJSONObject(i)
										.getInt("sts"), ja.getJSONObject(i)
										.getInt("no"), ja.getJSONObject(i)
										.getString("conven"));
								JSONArray ja1 = ja.getJSONObject(i)
										.getJSONArray("questions");
								for (int j = 0; j < ja1.length(); j++) {
									Questions q = new Questions(ja1
											.getJSONObject(j).getString("id"),
											ja1.getJSONObject(j).getInt("qno"),
											ja1.getJSONObject(j).getString(
													"quest"), ja1
													.getJSONObject(j).getInt(
															"typ"), ja1
													.getJSONObject(j).getInt(
															"isOp"), ja1
													.getJSONObject(j).getInt(
															"g"), ja1
													.getJSONObject(j).getInt(
															"spm"));
									dbh.addQuestion(q);
									int tup = ja1.getJSONObject(j)
											.getInt("typ");
									Log.w("The type is", tup + "");
									if (tup == 1) {
										Response_yn or = new Response_yn(ja1
												.getJSONObject(j).getString(
														"id"),
												ja1.getJSONObject(j).getInt(
														"qno"), 0);
										dbh.addResponse_yn(or);
									} else if (tup == 2) {
										String hx = ja1.getJSONObject(j)
												.getString("op");
										String hc[] = hx.split("~#%&");
										Options_Radio or = new Options_Radio(
												ja1.getJSONObject(j).getString(
														"id"), ja1
														.getJSONObject(j)
														.getInt("qno"), hc, 0);
										dbh.addOptions_Radio(or);
									} else if (tup == 3) {
										String hx = ja1.getJSONObject(j)
												.getString("op");
										String hc[] = hx.split("~#%&");
										Log.w("" + hc.length, "Length");
										int rp[] = new int[hc.length];
										Log.w("" + Arrays.toString(rp),
												"Arrays" + " " + rp.length);

										Options_Checkbox or = new Options_Checkbox(
												ja1.getJSONObject(j).getString(
														"id"), ja1
														.getJSONObject(j)
														.getInt("qno"), hc, rp);
										dbh.addOptions_Checkbox(or);
									} else if (tup == 4) {
										Response_Rating or = new Response_Rating(
												ja1.getJSONObject(j).getString(
														"id"), ja1
														.getJSONObject(j)
														.getInt("qno"), 0);
										dbh.addResponse_Rating(or);
									} else if (tup == 5) {
										Response_FeedBack or = new Response_FeedBack(
												ja1.getJSONObject(j).getString(
														"id"), ja1
														.getJSONObject(j)
														.getInt("qno"), "");
										dbh.addResponse_FeedBack(or);
									} else if (tup == 6) {
										String hx = ja1.getJSONObject(j)
												.getString("op");
										Log.w("Op.hx", hx);
										String hc[] = hx.split("~#%&");
										Log.w("" + hc.length, "Length" + " "
												+ Arrays.toString(hc));
										Options_RadioC or = new Options_RadioC(
												ja1.getJSONObject(j).getString(
														"id"), ja1
														.getJSONObject(j)
														.getInt("qno"), hc, 0);
										Otherans o = new Otherans(ja1
												.getJSONObject(j).getString(
														"id"),
												ja1.getJSONObject(j).getInt(
														"qno"), "");
										dbh.addOptions_RadioC(or);
										dbh.addOtherans(o);
									} else if (tup == 7) {
										Dateans or = new Dateans(ja1
												.getJSONObject(j).getString(
														"id"),
												ja1.getJSONObject(j).getInt(
														"qno"), "");
										dbh.addDateans(or);
									}
								}
								Log.w("Tile adding..", "" + ids[i]);
								dbh.addTile(t);
							}
						}

						JSONArray pollcats = jn.getJSONArray("pollcats");
						for (int i = 0; i < pollcats.length(); i++) {
							Log.w("Check categs", "caten: "
									+ pollcats.getJSONObject(i).getInt("nrpl"));
							pollCats pc1 = new pollCats(pollcats.getJSONObject(
									i).getString("id"), pollcats.getJSONObject(
									i).getString("catnm"), pollcats
									.getJSONObject(i).getInt("nrpl"));
							dbh.addPollCat(pc1);
						}
						// System gives error that value at polls of type
						// java.lang.String cannot be converted to JSONArray
						try {
							JSONArray polls = jn.getJSONArray("polls");
							for (int i = 0; i < polls.length(); i++) {
								pollsWithin p = new pollsWithin(
										polls.getJSONObject(i).getString("id"),
										polls.getJSONObject(i).getString("qid"),
										polls.getJSONObject(i)
												.getString("ques"), polls
												.getJSONObject(i)
												.getInt("nrpl"), polls
												.getJSONObject(i).getString(
														"ops"), polls
												.getJSONObject(i).getString(
														"res"), polls
												.getJSONObject(i).getInt(
														"icreated"));
								dbh.addPollinit(p);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						/*
						 * String filled[]=jn.getString("filled").split(",");
						 * for(int i=0;i<filled.length;i++) { tilesTable
						 * t=dbh.getTile(filled[i]); t.setSts(4);
						 * dbh.updatetilesTable(t); }
						 */

						// download pics
						AccessSP.setCurPts(MainActivity.champu,
								jn.getInt("pts"));

						Log.w("IN try: Fname is", jn.getString("fname")
								+ " is the fname");
						AccessSP.setFname(MainActivity.champu,
								jn.getString("fname"));

						Intent intent = new Intent(MainActivity.champu,
								SometimesServicePics.class);
						startService(intent);
						// SearchResults.srchresl = new ArrayList<tilesTable>();
						((MainActivity) MainActivity.champu)
								.runOnUiThread(new Runnable() {
									public void run() {
										try {
											if (MainActivity.checkNull
													.equals("opine")) {
												MainActivity.transaction = MainActivity.fm
														.beginTransaction();
												Fragment fr = FragRight
														.newInstance();
												MainActivity.transaction
														.replace(R.id.f2, fr,
																"tiles");
												MainActivity.transaction
														.commit();
												Log.w("hua replae", "hee");
												((SherlockFragmentActivity) MainActivity.champu)
														.getSupportActionBar()
														.setHomeButtonEnabled(
																true);
												GlobVars.isDoing = false;

												MainActivity
														.actionBarVisiblity(1);
												MainActivity.h.setEnabled(true);
											}
										} catch (Exception e) {

										}
									}
								});

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					Log.w("Null JSON", "Null from init");
					((Activity) MainActivity.champu).finish();
				}
				// if (mPrefs.getString("fname", "").equals(""))
				// makeRequ();
			} catch (GooglePlayServicesAvailabilityException playEx) {
				finishEx("Google Play Service is not found on your Device");
			} catch (UserRecoverableAuthException e) {
				Intent i = e.getIntent();
				Log.w("Allow/Deny", "Allow/Deny");
				((MainActivity) MainActivity.champu).startActivityForResult(i,
						11);
			} catch (IOException ioEx) {

				Log.w("SometimesService", "caught in IOException: " + ioEx);
				for (int vb = 0; vb < 20; vb++) {
					try {
						Thread.sleep(vb);
					} catch (InterruptedException e1) {

						e1.printStackTrace();
					}
					try {
						if (GCMRegistrar.getRegistrationId(
								getApplicationContext()).equals("")) {
							GCMRegistrar.register(getApplicationContext(),
									getResources().getString(R.string.projno));
						}

						MainActivity.token = GoogleAuthUtil.getToken(
								MainActivity.champu, em.trim(), getResources()
										.getString(R.string.scope));

						final JSONParser jsonParser = new JSONParser();
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("token",
								MainActivity.token));
						params.add(new BasicNameValuePair("regId", GCMRegistrar
								.getRegistrationId(getApplicationContext())));
						params.add(new BasicNameValuePair("usrnm", mPrefs
								.getString("usrnm", "")));

						JSONObject jn = jsonParser.makeHttpRequest(
								getResources().getString(R.string.initdata),
								"POST", params, who);

						Log.w("called init", "called init5");

						try {
							if (!"".equals(jn.getString("fname"))) {
								Log.w("catch mein", "catch mein");
								int num = jn.getInt("no");
								JSONArray ja = jn.getJSONArray("tiles");
								if (ja != null) {
									String ids[] = new String[ja.length()];
									for (int i = 0; i < num; i++) {
										ids[i] = ja.getJSONObject(i).getString(
												"id");
										tilesTable t = new tilesTable(ja
												.getJSONObject(i).getString(
														"id"), ja
												.getJSONObject(i).getString(
														"compNm"), ja
												.getJSONObject(i)
												.getInt("nodl"),
												ja.getJSONObject(i).getInt(
														"pts"), ja
														.getJSONObject(i)
														.getInt("sts"), ja
														.getJSONObject(i)
														.getInt("no"), ja
														.getJSONObject(i)
														.getString("conven"));
										JSONArray ja1 = ja.getJSONObject(i)
												.getJSONArray("questions");
										for (int j = 0; j < ja1.length(); j++) {
											Questions q = new Questions(ja1
													.getJSONObject(j)
													.getString("id"), ja1
													.getJSONObject(j).getInt(
															"qno"), ja1
													.getJSONObject(j)
													.getString("quest"), ja1
													.getJSONObject(j).getInt(
															"typ"), ja1
													.getJSONObject(j).getInt(
															"isOp"), ja1
													.getJSONObject(j).getInt(
															"g"), ja1
													.getJSONObject(j).getInt(
															"spm"));
											dbh.addQuestion(q);
											int tup = ja1.getJSONObject(j)
													.getInt("typ");
											if (tup == 1) {
												Response_yn or = new Response_yn(
														ja1.getJSONObject(j)
																.getString("id"),
														ja1.getJSONObject(j)
																.getInt("qno"),
														0);
												dbh.addResponse_yn(or);
											}
											if (tup == 2) {
												String hx = ja1
														.getJSONObject(j)
														.getString("op");
												String hc[] = hx.split("~#%&");
												Options_Radio or = new Options_Radio(
														ja1.getJSONObject(j)
																.getString("id"),
														ja1.getJSONObject(j)
																.getInt("qno"),
														hc, 0);
												dbh.addOptions_Radio(or);
											}
											if (tup == 3) {
												String hx = ja1
														.getJSONObject(j)
														.getString("op");
												String hc[] = hx.split("~#%&");
												Log.w("" + hc.length, "Length");
												int rp[] = new int[hc.length];
												Log.w("" + Arrays.toString(rp),
														"Arrays" + " "
																+ rp.length);
												Options_Checkbox or = new Options_Checkbox(
														ja1.getJSONObject(j)
																.getString("id"),
														ja1.getJSONObject(j)
																.getInt("qno"),
														hc, rp);
												dbh.addOptions_Checkbox(or);
											}
											if (tup == 4) {
												Response_Rating or = new Response_Rating(
														ja1.getJSONObject(j)
																.getString("id"),
														ja1.getJSONObject(j)
																.getInt("qno"),
														0);
												dbh.addResponse_Rating(or);
											}
											if (tup == 5) {
												Response_FeedBack or = new Response_FeedBack(
														ja1.getJSONObject(j)
																.getString("id"),
														ja1.getJSONObject(j)
																.getInt("qno"),
														"");
												dbh.addResponse_FeedBack(or);
											}
										}
										dbh.addTile(t);
										Log.w("tile added", "tile added");
									}
								}
								Log.w("hua replaeyyyyy", "hee fname");
								/*
								 * String filled[]=jn.getString("filled")
								 * .split(","); for(int i=0;i<filled.length;i++)
								 * { tilesTable t=dbh.getTile(filled[i]);
								 * t.setSts(4); dbh.updatetilesTable(t); }
								 */

								// download pics
								AccessSP.setCurPts(MainActivity.champu,
										jn.getInt("pts"));
								Intent intent = new Intent(MainActivity.champu,
										SometimesServicePics.class);
								startService(intent);
								AccessSP.setFname(MainActivity.champu,
										jn.getString("fname"));
								MainActivity.h.setEnabled(true);
								Log.w("hua replae", "hee fname");
								// SearchResults.srchresl = new
								// ArrayList<tilesTable>();
								((MainActivity) MainActivity.champu)
										.runOnUiThread(new Runnable() {
											public void run() {
												if (MainActivity.checkNull
														.equals("opine")) {
													MainActivity.transaction = MainActivity.fm
															.beginTransaction();
													Fragment fr = FragRight
															.newInstance();
													MainActivity.transaction
															.replace(R.id.f2,
																	fr, "tiles");
													MainActivity.transaction
															.commit();
													Log.w("hua replae", "hee");
													((SherlockFragmentActivity) MainActivity.champu)
															.getSupportActionBar()
															.setHomeButtonEnabled(
																	true);
													GlobVars.isDoing = false;
													// SearchResults.srchresl =
													// new
													// ArrayList<tilesTable>();
													MainActivity
															.actionBarVisiblity(1);
													MainActivity.h
															.setEnabled(true);
												}
											}
										});

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						break;
					} catch (GoogleAuthException authEx) {

						Log.w("GoogleAuthException", "hing15");
					} catch (IOException e) {

					}
				}

				if (mPrefs.getString("usrnm", "").equals("")) {
					if (MainActivity.token.equals("uninitialised"))
						finishEx("Your Internet Connection is not working");
				}
				/*
				 * Log.w("IOException","hing"); BackOffPolicy backOffPolicy =
				 * new ExponentialBackOffPolicy(); while (true) { try {
				 * token=GoogleAuthUtil.getToken(x,em.trim(),scope); } catch
				 * (IOException e) { Log.w("IOException","hing11"); long
				 * backOffMillis=0;
				 * 
				 * backOffMillis =backOffMillis+3; if(backOffMillis ==9) {
				 * if(mPrefs.getString("fname", "").equals("")){ finishEx(
				 * "You need Internet Connection for the first few minutes" ,x);
				 * }else{ Log.w("IOException","hing12"); //TODO sochte h... } }
				 * // sleep try { Thread.sleep(backOffMillis); } catch
				 * (InterruptedException e2) { Log.w("IOException","hing13"); //
				 * ignore } } catch (GoogleAuthException authEx) {
				 * 
				 * Log.w("IOException","hing15"); } }
				 */

			}

			catch (GoogleAuthException authEx) {
				// This is likely unrecoverable.
				Log.e("Fatal ", "Unrecoverable authentication exception: "
						+ authEx.getMessage(), authEx);
				finishEx("Unknown Exception. Please try to install Application again.");

			}

		} else {
			// SearchResults.srchresl = new ArrayList<tilesTable>();
			((MainActivity) MainActivity.champu).runOnUiThread(new Runnable() {
				public void run() {
					if (MainActivity.checkNull.equals("opine")) {
						MainActivity.transaction = MainActivity.fm
								.beginTransaction();
						Fragment fr = FragRight.newInstance();
						MainActivity.transaction.replace(R.id.f2, fr, "tiles");
						MainActivity.transaction.commit();
						Log.w("hua replae", "hee");
						((SherlockFragmentActivity) MainActivity.champu)
								.getSupportActionBar().setHomeButtonEnabled(
										true);
						GlobVars.isDoing = false;

						MainActivity.actionBarVisiblity(1);
					}
				}
			});
		}

	}

	void finishEx(final String m) {

		((MainActivity) MainActivity.champu).runOnUiThread(new Runnable() {
			public void run() {
				if (MainActivity.checkNull.equals("opine")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.champu);
					builder.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle("Cannot Open Appliction!")
							.setMessage(m)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											((MainActivity) MainActivity.champu)
													.finish();
										}

									}).show();
				}
			}
		});
	}

}
