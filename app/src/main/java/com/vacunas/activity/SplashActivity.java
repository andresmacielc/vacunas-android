package com.vacunas.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.vacunas.R;


public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);



		Thread welcomeThread = new Thread() {
			int wait = 0;

			public void run() {
				try {
					super.run();
					while (wait < 3000) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {
					System.out.println(e);
				} finally {
					startNextActivity();
					finish();
				}
			}
		};

		welcomeThread.start();
	}


	private void startNextActivity() {
		Intent i = new Intent(getBaseContext(), LoginActivity.class);
		startActivity(i);
		finish();
	}


}