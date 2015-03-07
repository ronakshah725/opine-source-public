package com.surv.ui123;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {
	private static DatabaseHandler dbh;
	public static SQLiteDatabase sld;
	public static SharedPreferences mPrefs;
	static Context who;
	static NotificationManager nm;
	static final int Uid = 156652;

	@Override
	protected void onError(Context arg0, String arg1) {

	}

	@Override
	protected String[] getSenderIds(Context context) {
		Log.w("GCM SenderId", "GCM SenderId");
		return super.getSenderIds(context);

	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.w("Service ", "Started");
		dbh = new DatabaseHandler(this);
		sld = dbh.getWritableDatabase();
		who = this;
		// final Bundle bundle = intent.getExtras();
		final String bundle = intent.getStringExtra("opinesurv");
		Log.w("bundle", "GCM" + bundle.toString());
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Uid);

		try {
			JSONObject ja = new JSONObject(bundle.toString());
			Log.w("GCM data", "GCM data:"+ja + "");
			if (ja.getInt("gcmid") == 1) {
				tilesTable t = new tilesTable(ja.getString("id"),
						ja.getString("compNm"), ja.getInt("nodl"),
						ja.getInt("pts"), ja.getInt("sts"), ja.getInt("no"),
						ja.getString("conven"));
				if (ja != null) {
					JSONArray ja1 = ja.getJSONArray("questions");

					for (int j = 0; j < ja1.length(); j++) {
						Questions q = new Questions(ja1.getJSONObject(j)
								.getString("id"), ja1.getJSONObject(j).getInt(
								"qno"),
								ja1.getJSONObject(j).getString("quest"), ja1
										.getJSONObject(j).getInt("typ"), ja1
										.getJSONObject(j).getInt("isOp"), ja1
										.getJSONObject(j).getInt("g"), ja1
										.getJSONObject(j).getInt("spm"));
						dbh.addNewQuestion(q);
						int tup = ja1.getJSONObject(j).getInt("typ");
						Log.w("The type is", tup + "");
						if (tup == 1) {
							Response_yn or = new Response_yn(ja1.getJSONObject(
									j).getString("id"), ja1.getJSONObject(j)
									.getInt("qno"), 0);
							dbh.addNewResponse_yn(or);
						} else if (tup == 2) {
							String hx = ja1.getJSONObject(j).getString("op");
							Log.w("Op.hx", hx);
							String hc[] = hx.split("~#%&");
							Log.w("" + hc.length,
									"Length" + " " + Arrays.toString(hc));
							Options_Radio or = new Options_Radio(ja1
									.getJSONObject(j).getString("id"), ja1
									.getJSONObject(j).getInt("qno"), hc, 0);
							dbh.addNewOptions_Radio(or);
						} else if (tup == 3) {
							String hx = ja1.getJSONObject(j).getString("op");
							Log.w("Op.hx", hx);
							String hc[] = hx.split("~#%&");
							Log.w("" + hc.length,
									"Length" + " " + Arrays.toString(hc));
							int rp[] = new int[hc.length];
							Log.w("" + Arrays.toString(rp), "Arrays" + " "
									+ rp.length);

							Options_Checkbox or = new Options_Checkbox(ja1
									.getJSONObject(j).getString("id"), ja1
									.getJSONObject(j).getInt("qno"), hc, rp);
							dbh.addNewOptions_Checkbox(or);
						} else if (tup == 4) {
							Response_Rating or = new Response_Rating(ja1
									.getJSONObject(j).getString("id"), ja1
									.getJSONObject(j).getInt("qno"), 0);
							dbh.addNewResponse_Rating(or);
						} else if (tup == 5) {
							Response_FeedBack or = new Response_FeedBack(ja1
									.getJSONObject(j).getString("id"), ja1
									.getJSONObject(j).getInt("qno"), "");
							dbh.addNewResponse_FeedBack(or);
						} else if (tup == 6) {
							String hx = ja1.getJSONObject(j).getString("op");
							Log.w("Op.hx", hx);
							String hc[] = hx.split("~#%&");
							Log.w("" + hc.length,
									"Length" + " " + Arrays.toString(hc));
							Options_RadioC or = new Options_RadioC(ja1
									.getJSONObject(j).getString("id"), ja1
									.getJSONObject(j).getInt("qno"), hc, 0);
							Otherans o = new Otherans(ja1.getJSONObject(j)
									.getString("id"), ja1.getJSONObject(j)
									.getInt("qno"), "");
							dbh.addNewOptions_RadioC(or);
							dbh.addNewOtherans(o);
						} else if (tup == 7) {
							Dateans or = new Dateans(ja1.getJSONObject(j)
									.getString("id"), ja1.getJSONObject(j)
									.getInt("qno"), "");
							dbh.addNewDateans(or);
						}
					}
					dbh.addNewTile(t);

					String ids[] = { t.getId() + "" };
					new DlPics(ids, 1, who);
					try {
						((MainActivity) MainActivity.champu)
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {
											if (MainActivity.checkNull
													.equals("opine")) {
												MainActivity.allTiles.clear();
												MainActivity.allTiles = dbh
														.getAllTiles(GlobVars.sort);
												FragRight.adapter
														.notifyDataSetChanged();
											}
											Log.w("Is Main Null?",
													MainActivity.checkNull);
										} catch (NullPointerException e) {
											Log.w("Exception",
													"MainActivity null");

										}
									}
								});
					} catch (NullPointerException e) {
						Log.w("Exception", "MainActivity null");

					}
				}

			} else if (ja.getInt("gcmid") == 2) {

				int TempFinalPts = ja.getInt("pts");
				AccessSP.setCurPts(who, TempFinalPts);
				try {
					GlobVars.current_pts = TempFinalPts;
					GlobVars.current_bal = TempFinalPts
							* who.getResources().getInteger(R.string.fact);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"dd-MMM-yyyy HH:mm:ss");
					String currentDateandTime = sdf.format(new Date());
					logData ld = new logData("Points updated to "
							+ GlobVars.current_pts, currentDateandTime);
					GCMIntentService.dbh.addLog(ld);
				} catch (Exception e) {

				}
			} else if (ja.getInt("gcmid") == 3) {
				String Srvid2up = ja.getString("srvid");
				tilesTable t = dbh.getTilegcm(Srvid2up);
				t.setSts(4);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MMM-yyyy HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				logData ld = new logData("Responses uploaded for " + t.getId(),
						currentDateandTime);
				GCMIntentService.dbh.addLog(ld);
				dbh.updatetilesTablegcm(t);
				addDefaultNotification(who, "Responses Uploaded",
						"Responses Uploaded Successfully");
			} else if (ja.getInt("gcmid") == 4) {
				String Sid = ja.getString("id");
				int NoDl = ja.getInt("nodl");
				tilesTable t = dbh.getTilegcm(Sid);
				t.setNodl(NoDl);
				dbh.updatetilesTablegcm(t);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MMM-yyyy HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				logData ld = new logData("New Survey " + t.getId(),
						currentDateandTime);
				GCMIntentService.dbh.addLog(ld);
				try {
					((MainActivity) MainActivity.champu)
							.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										Log.w("try mein", "try mien");
										if (MainActivity.checkNull
												.equals("opine")) {
											MainActivity.allTiles.clear();
											MainActivity.allTiles = dbh
													.getAllTiles(GlobVars.sort);

											Log.w("Notifying...",
													"Notifying...");
											FragRight.adapter
													.notifyDataSetChanged();
										}
										Log.w("Is Main Null?",
												MainActivity.checkNull);
									} catch (NullPointerException e) {
										Log.w("Exception", "MainActivity null");
									}
								}
							});
				} catch (NullPointerException e) {
					Log.w("Exception", "MainActivity null");
				}

			} else if (ja.getInt("gcmid") == 5) {
				Log.w("Getting new Voucher.. ",
						ja.getString("id") + " " + ja.getString("vcrnm") + " "
								+ ja.getString("descr") + " "
								+ ja.getInt("amt"));
				Voucher t = new Voucher(ja.getString("id"),
						ja.getString("vcrnm"), ja.getString("descr"),
						ja.getInt("amt"), 0);
				dbh.addNewVoucher(t);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MMM-yyyy HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				logData ld = new logData("New Voucher " + t.getId(),
						currentDateandTime);
				GCMIntentService.dbh.addLog(ld);
				String ids[] = { t.getId() + "" };
				new DlPics(ids, 2, who);
				try {
					((MainActivity) MainActivity.champu)
							.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										if (MainActivity.checkNull
												.equals("opine")) {
											MainActivity.allVouchers.clear();
											MainActivity.allVouchers = dbh
													.getAllVouchers();
											FragRedeem.adapter
													.notifyDataSetChanged();
										}
										Log.w("Is Main Null?",
												MainActivity.checkNull);
									} catch (NullPointerException e) {
										Log.w("Exception", "MainActivity null");

									}
								}
							});
				} catch (NullPointerException e) {
					Log.w("Exception", "MainActivity null");

				}

			} else if (ja.getInt("gcmid") == 6) {

				Log.w("Recieved GCM for Disabling tile",
						"" + ja.getString("id"));
				dbh.deletetab("tilesInfo", ja.getString("id"));
				dbh.deletetab("vt", ja.getString("id"));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MMM-yyyy HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				logData ld = new logData("Survey Disabled "
						+ ja.getString("id"), currentDateandTime);
				GCMIntentService.dbh.addLog(ld);
				try {
					((MainActivity) MainActivity.champu)
							.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										if (MainActivity.checkNull
												.equals("opine")) {
											MainActivity.allTiles.clear();
											MainActivity.allTiles = dbh
													.getAllTiles(GlobVars.sort);
											FragRight.adapter
													.notifyDataSetChanged();
										}
										Log.w("Is Main Null?",
												MainActivity.checkNull);
									} catch (NullPointerException e) {
										Log.w("Exception", "MainActivity null");
									}
								}

							});
				} catch (NullPointerException e) {
					Log.w("Exception", "MainActivity null");
				}

			} else if (ja.getInt("gcmid") == 7) {

				Log.w("Recieved GCM for Disabling Voucher",
						"" + ja.getString("id"));
				dbh.deletetab("Vouchers", ja.getString("id"));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MMM-yyyy HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				logData ld = new logData("Voucher Disabled "
						+ ja.getString("id"), currentDateandTime);
				GCMIntentService.dbh.addLog(ld);

				try {
					((MainActivity) MainActivity.champu)
							.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										if (MainActivity.checkNull
												.equals("opine")) {
											MainActivity.allVouchers.clear();
											MainActivity.allVouchers = dbh
													.getAllVouchers();
											FragRedeem.adapter
													.notifyDataSetChanged();
										}
										Log.w("Is Main Null?",
												MainActivity.checkNull);
									} catch (NullPointerException e) {
										Log.w("Exception", "MainActivity null");
									}
								}
							});
				} catch (NullPointerException e) {
					Log.w("Exception", "MainActivity null");
				}
			}
			else if(ja.getInt("gcmid")==8)
			{
				Log.w(""+ja.getString("rp"),"Poll res");
				String qid=ja.getString("qid");
				String rp=ja.getString("rp");
				int nrpl=ja.getInt("nrpl");
				GCMIntentService.dbh.updatePolls(qid,rp,nrpl);				
			}
			else if(ja.getInt("gcmid")==9)
			{				
				pollsWithin p=new pollsWithin(ja.getString("id"), ja.getString("qid"), ja.getString("ques"), ja.getInt("nrpl"), ja.getString("ops"),ja.getString("res"),1);
				dbh.addPollsnd(p);
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dbh.close();
		sld.close();

	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
		Log.w("on Registered", "Caleed now");
		Log.w(GCMRegistrar.getRegistrationId(getApplicationContext()),
				"Registration id");
		if (GCMRegistrar.getRegistrationId(getApplicationContext()) != null) {
			Intent intent = new Intent(this, SometimesService.class);
			startService(intent);
		} else {
			((MainActivity) MainActivity.champu).runOnUiThread(new Runnable() {
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.champu);
					builder.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle("Cannot Open Appliction!")
							.setMessage(
									"Google Server Busy. Please try again in some time.")
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
			});
		}

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {

	}

	@SuppressWarnings("deprecation")
	private static void addDefaultNotification(Context c, String title,
			String txt) {

		Intent in = new Intent(c, MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(c, 0, in, 0);
		if (android.os.Build.VERSION.SDK_INT <= 11) {

			Notification n = new Notification(R.drawable.coins, title,
					System.currentTimeMillis());
			n.setLatestEventInfo(c, txt, "", pi);
			n.defaults = Notification.DEFAULT_ALL;
			n.flags |= Notification.FLAG_AUTO_CANCEL;
			nm.notify(Uid, n);
		} else {
			Notification noti = new Notification.Builder(c)
					.setContentTitle(title).setContentText(txt)
					.setSmallIcon(R.drawable.coins).setContentIntent(pi)
					.build();
			noti.flags |= Notification.FLAG_AUTO_CANCEL;
			nm.notify(Uid, noti);
		}

		Log.w("Notifffffffff", "called");

	}

}