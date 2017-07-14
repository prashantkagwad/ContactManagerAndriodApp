/**
 * @Author - Prashant Kagwad
 * @Date - 10/01/2014
 * @Project Description : This application helps a user to save, edit and
 * delete a contact. It has a multiple screens on user interface side to accomplish this.
 * Along with contact name (first name & last name) a user can store phone number and email address of the contact.
 */

package com.app.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * @info : LandingPage class to display the opening screen of the application.
 */
public class LandingPage extends Activity {

	private static String TAG = LandingPage.class.getName();
	private static long SLEEP_TIME = 2; // Sleep for some time

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar.
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar.
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_landing);

		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	/**
	 * Sleep for some time and than start new activity.
	 */
	private class IntentLauncher extends Thread {

		@Override
		public void run() {

			try {

				// Sleeping
				Thread.sleep(SLEEP_TIME * 1000);

			} catch (Exception e) {

				// TODO Auto-generated method stub
				Log.e(TAG, e.getMessage());
			}

			// Start main activity
			Intent intent = new Intent(LandingPage.this, MainActivity.class);
			LandingPage.this.startActivity(intent);
			LandingPage.this.finish();
		}
	}
}
