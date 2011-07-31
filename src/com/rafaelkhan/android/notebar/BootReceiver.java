package com.rafaelkhan.android.notebar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//This class is for notifying the user
public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context c, Intent i) {
		c.startService(new Intent(c, NotificationService.class));
	}
}