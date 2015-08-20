package org.milan.climax;

import java.lang.Thread.UncaughtExceptionHandler;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.R.string;
import android.app.Application;
import android.util.Log;
@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=e59b894a", formKey = "")
public class Bugsense extends Application implements UncaughtExceptionHandler {

	private static Bugsense inBugsense;
	public Bugsense(){
		inBugsense = this;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		Log.i("Error Log" , "---->"+ex.getMessage());
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ACRA.init(this);
		
		super.onCreate();
	}

}
