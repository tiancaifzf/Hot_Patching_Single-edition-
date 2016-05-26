package com.loveplusplus.update.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class ExitActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_exit);
		int pid=android.os.Process.myPid();
		android.os.Process.killProcess(pid);
		finish();;
	}

}
