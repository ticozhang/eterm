package cn.com.gigaalaser.home2;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Home2Activity extends Activity {
	
	private final String launcher_pkg_name = "cn.com.gigaalaser.launcher";
	
	private List<ResolveInfo> apps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		//hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//set full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home2);
		//disable screen off
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//set brightness to high
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 1.0f;
		getWindow().setAttributes(lp);
	}
	
	@Override
	protected void onStart (){
		super.onStart();
		
		loadApps();
		
		for(int i=0; i<apps.size(); i++){
			ResolveInfo info = apps.get(i);
			//package name
			String pkg = info.activityInfo.packageName;
			Log.i("HomeActivity", "PKG: " + pkg);
			if(pkg.equals(launcher_pkg_name)){
				//package main activity
	            String cls = info.activityInfo.name;  
	            ComponentName componet = new ComponentName(pkg, cls);
	            
	            Intent intent = new Intent();  
	            intent.setComponent(componet);  
	            startActivity(intent);
	            break;
			}
		}
	}
	
	private void loadApps() {  
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
  
        apps = getPackageManager().queryIntentActivities(mainIntent, 0);  
    }

}
