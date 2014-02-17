package cn.com.gigaalaser.launcher;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {
	
	public final static int UPDATE_TIME = 200;

	private SerialCommand serialCmd;
	private CommandHandler handler;
	private MediaPlayer player;
	
	private Timer timer;

	OnCaptureButtonClick onCaptureButtonClick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// set full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		// disable screen off
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// set brightness to high
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 1.0f;
		getWindow().setAttributes(lp);

		Button bt = (Button) findViewById(R.id.button_video_cap);
		onCaptureButtonClick = new OnCaptureButtonClick();
		bt.setOnClickListener(onCaptureButtonClick);

		CheckBox cb = (CheckBox) findViewById(R.id.checkbox_busy);
		cb.setOnCheckedChangeListener(new OnBusyCheckChangeListener());

		handler = new CommandHandler(this);
		try {
			serialCmd = new SerialCommand(handler);
			serialCmd.start();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//set audio to max volume
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
		
		//MediaPlayer
		player = MediaPlayer.create(this,R.raw.morning);
		player.setLooping(true);
		
		ToggleButton tb = (ToggleButton) findViewById(R.id.button_play);
		tb.setOnCheckedChangeListener(new OnAudioCheckedChangeListener());
		
		
		timer = new Timer();
		TimerTask task = new TimerTask(){
	        public void run() {
//	        	int year = calendar.get(Calendar.YEAR);
//				int month = calendar.get(Calendar.MONTH) + 1;
//				int day = calendar.get(Calendar.DAY_OF_MONTH);
//				int hour = calendar.get(Calendar.HOUR_OF_DAY);
//				int minute = calendar.get(Calendar.MINUTE);
//				int second = calendar.get(Calendar.SECOND);
//				TextView tv = (TextView) findViewById(R.id.time);
//				tv.setText(""+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second);
				handler.sendEmptyMessage(UPDATE_TIME);
	        }
	    };
		timer.schedule(task, 1000, 1000);
	}
	
	@Override
	protected void onDestroy() {
		//player.stop();
		player.release();
		timer.cancel();
		if (this.serialCmd != null) {
			this.serialCmd.stopMe();
			try {
				this.serialCmd.join();
			} catch (Exception e) {

			}
		}
		super.onDestroy();
	}

	class OnCaptureButtonClick implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Button bt = (Button) findViewById(R.id.button_video_cap);
			LauncherSurfaceView lsv = (LauncherSurfaceView) findViewById(R.id.surfaceview_video);
			RadioButton pal = (RadioButton) findViewById(R.id.radio_pal);
			if (bt.getText().equals("Capture")) {
				bt.setEnabled(false);
				if (pal.isChecked()) {
					lsv.setVideoFormat(LauncherSurfaceView.V_PAL);
				} else {
					lsv.setVideoFormat(LauncherSurfaceView.V_NTSC);
				}

				if (lsv.setCapture(true)) {
					bt.setText("Stop");
					RadioGroup rg = (RadioGroup) findViewById(R.id.videoformat_group);
					for (int i = 0; i < rg.getChildCount(); i++) {
						rg.getChildAt(i).setEnabled(false);
					}
				} else {
					Toast.makeText(MainActivity.this, "Start Capture Fail",
							Toast.LENGTH_SHORT).show();
				}

				bt.setEnabled(true);
			} else {
				lsv.setCapture(false);
				bt.setText("Capture");
				RadioGroup rg = (RadioGroup) findViewById(R.id.videoformat_group);
				for (int i = 0; i < rg.getChildCount(); i++) {
					rg.getChildAt(i).setEnabled(true);
				}
			}
		}

	}

	class OnBusyCheckChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			CheckBox cb = (CheckBox) findViewById(R.id.checkbox_busy);
			if (cb.isChecked()) {
				BusyIoPin.setPinValue(1);
			} else {
				BusyIoPin.setPinValue(0);
			}
		}

	}
	
	class OnAudioCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			ToggleButton tb = (ToggleButton) findViewById(R.id.button_play);
			if(tb.isChecked()){
				AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
				player.start();
			}else{
				player.pause();
			}
		}
		
	}

}

class CommandHandler extends Handler {
	WeakReference<MainActivity> activity;
	//private Calendar calendar;

	CommandHandler(MainActivity activity) {
		WeakReference<MainActivity> ref = new WeakReference<MainActivity>(
				activity);
		this.activity = ref;
		//calendar = Calendar.getInstance();
	}

	public void handleMessage(Message msg) {
		MainActivity act = (MainActivity) this.activity.get();
		super.handleMessage(msg);

		Button bt = (Button) act.findViewById(R.id.button_video_cap);
		switch (msg.what) {
		case SerialCommand.CMD_CAPTURE:
			if (bt.getText().equals("Capture")) {
				act.onCaptureButtonClick.onClick(null);
			}
			break;
		case SerialCommand.CMD_STOP_CAPTURE:
			if (bt.getText().equals("Stop")) {
				act.onCaptureButtonClick.onClick(null);
			}
			break;
		case MainActivity.UPDATE_TIME:
//			int year = calendar.get(Calendar.YEAR);
//			int month = calendar.get(Calendar.MONTH) + 1;
//			int day = calendar.get(Calendar.DAY_OF_MONTH);
//			int hour = calendar.get(Calendar.HOUR_OF_DAY);
//			int minute = calendar.get(Calendar.MINUTE);
//			int second = calendar.get(Calendar.SECOND);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZ", Locale.US);
			Date now = new Date();
			
			TextView tv = (TextView) act.findViewById(R.id.time);
			//tv.setText(""+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second);
			tv.setText(sdf.format(now));
			break;
		}

	}
}
