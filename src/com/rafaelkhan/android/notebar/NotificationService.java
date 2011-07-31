package com.rafaelkhan.android.notebar;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

	public static Notification notification;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.notificationInitialize();
		return START_STICKY;
	}

	/*
	 * notification stuff
	 */

	public void notificationInitialize() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.status_bar_icon;
		CharSequence tickerText = "NoteBar...";
		long when = System.currentTimeMillis();

		notification = new Notification(icon, tickerText, when);

		CharSequence contentTitle = "NoteBar";
		String contentText = this.readFileToString();

		Intent notificationIntent = new Intent(this, NoteBarActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.flags = Notification.FLAG_NO_CLEAR;

		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(1, notification);
	}

	public String readFileToString() {
		int ch;
		StringBuilder sb = new StringBuilder("");

		try {
			FileInputStream fis = openFileInput("current_note.txt");
			while ((ch = fis.read()) != -1) {
				sb.append((char) ch);
			}
			fis.close();
			return sb.toString();
		} catch (Exception e) {
			try {
				FileOutputStream fos = openFileOutput("current_note.txt",
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
}