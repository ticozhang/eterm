package cn.com.gigaalaser.updater;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent newIntent = new Intent(context, UpdaterService.class);
			context.startService(newIntent);
		}
	}

}
