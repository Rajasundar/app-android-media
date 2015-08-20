package org.milan.climax;


import org.milan.climax.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent1 = getIntent();
			    String action = intent1.getAction();
			    String type = intent1.getType();
			    String roomNameFrmNav = "";
			    if (Intent.ACTION_SEND.equals(action) && type != null) {
			        if ("text/plain".equals(type)) {
			        	String sharedText = intent1.getStringExtra(Intent.EXTRA_TEXT);
			            if (sharedText != null) {
			            	roomNameFrmNav = sharedText;
			            	Log.i("splash---screen" , "------->"+roomNameFrmNav);
			            }
			        }
			    }
				Intent intent = new Intent(SplashScreen.this , MainActivity.class);
				intent.putExtra("Room Name", roomNameFrmNav);
				startActivity(intent);
				finish();
				
			}
		}, 1000);
	}

}
