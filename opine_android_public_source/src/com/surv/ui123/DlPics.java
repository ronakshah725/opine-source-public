package com.surv.ui123;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.util.Log;

public class DlPics {
	Context who;
	String idx[];

	public DlPics(String idx[], int which, Context who) {
		this.who = who;
		this.idx = idx;
		
		switch (which) {
		case 1:
			dlnow(who.getResources().getString(R.string.tilepics),which);
			break;
		case 2:
			dlnow(who.getResources().getString(R.string.voucpics),which);
			break;
		}
		
		Log.w("Downloaded","dld");
	}

	public void dlnow(String path,int which) {

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			
			
			//folder.mkdirs();
			//String extStorageDirectory = Environment.getExternalStorageDirectory()
				//	.toString() + who.getResources().getString(R.string.opineimages) + path+"/";
			URL url = null;
			

			
			
				for (int i = 0; i < idx.length; i++) {
					try {
						File extStore = Environment.getExternalStorageDirectory();
						File myFile = new File(extStore.getAbsolutePath() + who.getResources().getString(R.string.opineimages)+path+"/"+idx[i]
								+ who.getResources().getString(R.string.picextjpg));
						Log.w("Dlding pic",""+extStore.getAbsolutePath() + who.getResources().getString(R.string.opineimages)+path+"/"+idx[i]
								+ who.getResources().getString(R.string.picextjpg));

						if(!myFile.exists()){		
					try {
						Log.w("File not found","");
						if(which==1)
						{
						url = new URL(who.getResources().getString(R.string.urltodl)+idx[i]
								+ who.getResources().getString(R.string.picextjpg));
						}
						else
						{
							url = new URL(who.getResources().getString(R.string.urltodl1)+idx[i]
									+ who.getResources().getString(R.string.picextjpg));
						}
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.w("Downloading for",idx[i]);
					URLConnection connection = url.openConnection();
					connection.connect();					
					InputStream input = new BufferedInputStream(url.openStream());					
					OutputStream output = new FileOutputStream(extStore.getAbsolutePath() + who.getResources().getString(R.string.opineimages)+path+"/"+idx[i]
							+ who.getResources().getString(R.string.picextjpg));
					

		            byte data[] = new byte[1024];		           
		            int count;
		            while ((count = input.read(data)) != -1) {
		                             
		                output.write(data, 0, count);
		            }
					output.flush();
					output.close();
					input.close();
					
						}
					
				}catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					try {
						((MainActivity) MainActivity.champu)
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {
										if(MainActivity.checkNull.equals("opine"))
										{
										MainActivity.updtTiles();
										}
										Log.w("Is Main Null?",MainActivity.checkNull);
										} catch (NullPointerException e) {
											Log.w("Exception", "MainActivity null");

										}
									}
								});
					} catch (NullPointerException e) {
						Log.w("Exception", "MainActivity null");

					}

				}
		
			

		}
		
		Log.w("Dlnow","Dlnow");/*else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Toast.makeText(who, "sdCard Read Only", Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(who, "sdCard is not writable and readable",
					Toast.LENGTH_SHORT).show();

		}*/
	}
}