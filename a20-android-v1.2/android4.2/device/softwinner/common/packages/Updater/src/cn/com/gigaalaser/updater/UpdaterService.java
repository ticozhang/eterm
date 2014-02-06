package cn.com.gigaalaser.updater;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
//import android.widget.Toast;
import android.app.Service;

//public class UpdaterService extends android.app.Service {
//
//	@Override
//	public IBinder onBind(Intent arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}

public class UpdaterService extends Service {
	
	//ExternalStorageListener exStorageListener = null;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	UpdaterService getService() {
            return UpdaterService.this;
        }
    }

    @Override
    public void onCreate() {
    	Log.i("UpdaterServer", "onCreate");
    	//Toast.makeText(this, R.string.local_service_created, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("UpdaterServer", "Received start id " + startId + ": " + intent);
        //Toast.makeText(this, R.string.local_service_started, Toast.LENGTH_SHORT).show();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        //Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
        Log.i("UpdaterServer", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        //return mBinder;
    	return null;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    //private final IBinder mBinder = new LocalBinder();

}
