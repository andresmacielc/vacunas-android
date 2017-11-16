package com.vacunas.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vacunas.R;


public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		//TextView textView = (TextView) findViewById(R.id.version);
		//textView.setText(getVersionName(getApplicationContext()));

		final Context mContext = this;

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
					//startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					startNextActivity();
					finish();
				}
			}
		};

		welcomeThread.start();

		//cargarPromociones();
	}


	private void startNextActivity() {
		Intent i = new Intent(getBaseContext(), LoginActivity.class);
		startActivity(i);
		finish();
	}


}