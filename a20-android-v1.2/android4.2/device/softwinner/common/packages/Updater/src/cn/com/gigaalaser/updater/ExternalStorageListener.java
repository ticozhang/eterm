package cn.com.gigaalaser.updater;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class ExternalStorageListener extends BroadcastReceiver {
	
	private final String MAIN_APK = "Launcher.apk";
	private final String KEY_APK = "/system/vendor/key.apk";
	
	private boolean checkSignature(Context context, String key, String apk){
		PackageManager pm = context.getPackageManager();
		
		PackageInfo hpi = pm.getPackageArchiveInfo(key, PackageManager.GET_SIGNATURES);
		if(hpi != null){
			Log.i("Signature PackageInfo", hpi.toString());
			if(hpi.signatures != null){
				for(int i=0; i<hpi.signatures.length; i++){
					Log.i("Signature PackageInfo", hpi.signatures[i].toString());
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
		
		
		PackageInfo pi = pm.getPackageArchiveInfo(apk, PackageManager.GET_SIGNATURES);
		if(pi != null){
			Log.i("Signature PackageInfo", pi.toString());
			if(pi.signatures != null){
				for(int i=0; i<pi.signatures.length; i++){
					Log.i("Signature PackageInfo", pi.signatures[i].toString());
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
		
		if((hpi.signatures != null) && (pi.signatures != null)){
			if(hpi.signatures[0].equals(pi.signatures[0])){
				Log.i("Signature Equals", "" + hpi.signatures[0].hashCode() + " : " + pi.signatures[0].hashCode());
				return true;
			}else{
				Log.i("Signature not Equals", "" + hpi.signatures[0].hashCode() + " : " + pi.signatures[0].hashCode());
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
			Log.i("ExternalStorageListener", "Received ACTION_MEDIA_MOUNTED");
			Log.i("ExternalStorageListener", intent.getData().getPath());
			File mainapk = new File(intent.getData().getPath(), MAIN_APK);
			if(mainapk.exists() && mainapk.isFile()){
				if(checkSignature(context, KEY_APK, mainapk.getAbsolutePath())){
					Uri uri = Uri.fromFile(mainapk);
					Intent apkIntent = new Intent(Intent.ACTION_VIEW);
					apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
					apkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(apkIntent);
				}
			}
		}
	}

}
