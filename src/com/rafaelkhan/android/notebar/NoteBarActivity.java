package com.rafaelkhan.android.notebar;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NoteBarActivity extends Activity {

	private final String FILE_NAME = "current_note.txt";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		EditText textbodyField = (EditText) findViewById(R.id.textbody_field);
		textbodyField.setText(readFileToString());
	}

	public String readFileToString() {
		int ch;
		StringBuilder sb = new StringBuilder("");

		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			while ((ch = fis.read()) != -1) {
				sb.append((char) ch);
			}
			fis.close();
			return sb.toString();
		} catch (Exception e) {
			try {
				FileOutputStream fos = openFileOutput(FILE_NAME,
						Context.MODE_PRIVATE);
				fos.write("".getBytes());
				fos.close();
			} catch (Exception ex) {
				Log.e("error", ex.toString());
				return "";
			}
			return "";
		}
	}

	public void saveButton(View v) {
		EditText textbodyField = (EditText) findViewById(R.id.textbody_field);
		String textbody = textbodyField.getText().toString();

		try {
			FileOutputStream fos = openFileOutput(FILE_NAME,
					Context.MODE_PRIVATE);
			fos.write(textbody.getBytes());
			fos.close();
			Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
			this.updateNotification();
		} catch (Exception e) {
			Log.e("error", e.toString());
		}
	}

	public void cancelButton(View v) {
		moveTaskToBack(true);
	}

	private boolean serviceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.rafaelkhan.android.notebar.NotificationService"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public void updateNotification() {
		if (serviceRunning()) {
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			Intent notificationIntent = new Intent(this, NoteBarActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);
			NotificationService.notification.setLatestEventInfo(this,
					"NoteBar", readFileToString(), contentIntent);
			mNotificationManager.notify(1, NotificationService.notification);
		} else {
			Context c = getApplicationContext();
			c.startService(new Intent(c, NotificationService.class));
			this.updateNotification();
		}
	}
}