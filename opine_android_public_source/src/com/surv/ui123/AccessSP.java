package com.surv.ui123;

import java.util.concurrent.Semaphore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AccessSP {
	public static SharedPreferences mPrefs;
	public static SharedPreferences.Editor editor;
	static Semaphore s = new Semaphore(1, true);

	public static void init(Context cont)// ,String a, var)
	{

		Context ctx = cont.getApplicationContext();
		mPrefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
		editor = mPrefs.edit();
	}

	public static void setUsrnm(Context cont, String usrnm) {

		try {
			Log.w(usrnm, "Accessed");
			s.acquire();
			init(cont);
			editor.putString("usrnm", usrnm);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setFname(Context cont, String fname) {

		try {
			s.acquire();
			init(cont);
			editor.putString("fname", fname);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void setFt(Context cont)
	{ try {
		s.acquire();
		init(cont);
		editor.putInt("first_tym",1);
		editor.commit();
		s.release();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

	public static void setLname(Context cont, String lname) {

		try {
			s.acquire();
			init(cont);
			editor.putString("lname", lname);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setGen(Context cont, int gen) {

		try {
			s.acquire();
			init(cont);
			editor.putInt("gen", gen);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setBday(Context cont, String bday) {

		try {
			s.acquire();
			init(cont);
			editor.putString("bday", bday);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void setDemo(Context cont, int x) {
		try {
			s.acquire();
			init(cont);
			editor.putInt("democh",1);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setFirstTym(Context cont, int x) {
		try {
			s.acquire();
			init(cont);
			editor.putInt("first_tym",x);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void setFirstTymd(Context cont, int x) {
		try {
			s.acquire();
			init(cont);
			editor.putInt("first_tymd",x);
			editor.commit();
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void setCurPts(Context cont, int cur) {

		try {
			s.acquire();
			init(cont);			
			editor.putInt("cur_pts", cur);
			int m = cur
					/ Integer.parseInt(cont.getApplicationContext()
							.getResources().getString(R.string.fact));
			editor.putInt("cur_bal", m);

			editor.commit();
			Log.w("mPrefs Current Balance Set To ", "" + mPrefs.getInt("cur_bal", 0));
			Log.w("mPrefs Current pts Set To ", "" + mPrefs.getInt("cur_pts", 0));
			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			((MainActivity) MainActivity.champu).runOnUiThread(new Runnable() {
				public void run() {
					//TextView v1 = (TextView) ((Activity) MainActivity.champu)
					//		.findViewById(R.id.ressbal);
					//TextView v2 = (TextView) ((Activity) MainActivity.champu)
						//	.findViewById(R.id.curr_pts);
					//TextView v3 = (TextView) ((Activity) MainActivity.champu)
						//	.findViewById(R.id.cur2);
					MainActivity.tvpts.setText("" + mPrefs.getInt("cur_pts", 0));
					Log.w("Pts Updated in main",""+MainActivity.tvpts.getText());
					//MainActivity.tvval.setText("" + mPrefs.getInt("cur_bal", 0));
					//v1.setText("" + mPrefs.getInt("cur_bal", 0));
					//v2.setText("" + mPrefs.getInt("cur_pts", 0));
					//v3.setText("" + mPrefs.getInt("cur_bal", 0));				
					GlobVars.current_pts = mPrefs.getInt("cur_pts", 0);
					GlobVars.current_bal = mPrefs.getInt("cur_bal", 0);					
				}
			});
		} catch (Exception e) {
		}
		
		

	}

	public static void setCurBal(Context cont, int bal) {

		try {
			s.acquire();
			init(cont);
			editor.putInt("cur_bal", bal);
			editor.putInt(
					"cur_pts",bal* Integer.parseInt(cont.getApplicationContext().getResources().getString(R.string.fact)));
			editor.commit();

			s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			((MainActivity) MainActivity.champu).runOnUiThread(new Runnable() {
				public void run() {
					//TextView v1 = (TextView) ((Activity) MainActivity.champu)
							//.findViewById(R.id.ressbal);
					//TextView v2 = (TextView) ((Activity) MainActivity.champu)
						//	.findViewById(R.id.curr_pts);
					//TextView v3 = (TextView) ((Activity) MainActivity.champu)
							//.findViewById(R.id.cur2);

					MainActivity.tvpts.setText("" + mPrefs.getInt("cur_pts", 0));
					//MainActivity.tvval.setText("" + mPrefs.getInt("cur_bal", 0));
					//v1.setText("" + mPrefs.getInt("cur_bal", 0));
					//v2.setText("" + mPrefs.getInt("cur_pts", 0));
					//v3.setText("" + mPrefs.getInt("cur_bal", 0));
				//	TextView v4=(TextView)((Activity) SearchResults.champu)
				//			.findViewById(R.id.curr_pts1);
				//			v4.setText("" + mPrefs.getInt("cur_pts", 0));
				}
			});
		} catch (Exception e) {
		}
		
		try
		{
			//TextView v4=(TextView)((Activity) SearchResults.champu)
			//		.findViewById(R.id.curr_pts1);
			//		v4.setText("" + mPrefs.getInt("cur_pts", 0));
		}
		catch(Exception e)
		{
			
		}
	}
}