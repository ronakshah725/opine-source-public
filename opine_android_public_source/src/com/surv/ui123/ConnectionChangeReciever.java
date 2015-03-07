package com.surv.ui123;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionChangeReciever extends BroadcastReceiver
{
  @Override
  public void onReceive( Context context, Intent intent )
  {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE );
    boolean isConnected=false;
    if ( activeNetInfo != null )
    {
     isConnected=true;
     Log.w("internettt","internet");
    }
    if( mobNetInfo != null )
    {
     Log.w("internettt","iejwknternet");
     isConnected=true;
    }
        
    Context ctx = context.getApplicationContext();
    SharedPreferences prefs = ctx.getSharedPreferences("ui123", Context.MODE_PRIVATE);
    
    Log.w(""+prefs.getString("usrnm", ""),"It can access");
   
    
    if(!prefs.getString("usrnm", "").equals("")&&isConnected)
    {    	
    	Intent inx = new Intent(context, SometimesServiceV.class);
    		inx.putExtra("which","4");
    		context.startService(inx);
          
    		 Intent iny = new Intent(context, SometimesServiceR.class);
    			iny.putExtra("which","3");
    		context.startService(iny);
    			
    Intent in = new Intent(context, SometimesServiceU.class);
	in.putExtra("which","2");
	context.startService(in);
	if(prefs.getInt("democh", 0)==1)
	{
		 Intent inz = new Intent(context, SometimesServiceSD1.class);
			context.startService(inz);		
	}
	
    }
    else
    {
    Log.w("bhajjjungs","hehehhehehaha");
    }
  }
}