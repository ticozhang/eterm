package cn.com.gigaalaser.launcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

public class LauncherSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {
	public static final int V_NTSC = 1;
	public static final int V_PAL = 2;
	private int IMG_HEIGHT = 576;
	private int IMG_WIDTH = 720;
	private final int NTSC_HEIGHT = 480;
	private final int NTSC_WIDTH = 720;
	private final int PAL_HEIGHT = 576;
	private final int PAL_WIDTH = 720;
	private Bitmap bitmap = null;
	private boolean capruning = false;
	private boolean enablecap = false;
	private SurfaceHolder holder;
	private Thread mainloop;
	private boolean shoudstop = false;
	private int video_format = V_PAL;

	// JNI
	public native void pixeltobmp(Bitmap paramBitmap);

	public native int prepareCamera(int videoid, int format, int width,
			int height);

	public native void processCamera();

	public native void stopCamera();

	static {
		System.loadLibrary("ImageProc");
	}

	public LauncherSurfaceView(Context context) {
		super(context);
		
		this.holder = getHolder();
		this.holder.addCallback(this);
	}

	public LauncherSurfaceView(Context context,
			AttributeSet attri) {
		super(context, attri);

		this.holder = getHolder();
		this.holder.addCallback(this);
	}

	public void run() {
		while (true) {
			if (enablecap) {
				if (!capruning) {
					capruning = true;
				}
				processCamera();
				pixeltobmp(bitmap);
				Canvas canvas = getHolder().lockCanvas();
				if (canvas != null) {
					canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
					getHolder().unlockCanvasAndPost(canvas);
				}
			} else {
				capruning = false;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (shoudstop) {
				if (capruning) {
					stopCamera();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				shoudstop = false;
				capruning = false;
				enablecap = false;
				break;
			}
		}
	}

	public boolean setCapture(boolean start) {
		if (start) {
			if (prepareCamera(0, video_format, IMG_WIDTH, IMG_HEIGHT) == 0) {
				enablecap = true;
				return true;
			} else {
				return false;
			}
		} else {
			enablecap = false;
			while (capruning) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stopCamera();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	public void setVideoFormat(int format) {
		video_format = format;
		switch (video_format) {
		case V_NTSC:
			IMG_WIDTH = NTSC_WIDTH;
			IMG_HEIGHT = NTSC_HEIGHT;
			break;
		case V_PAL:
			IMG_WIDTH = PAL_WIDTH;
			IMG_HEIGHT = PAL_HEIGHT;
			break;
		default:
			video_format = V_PAL;
			IMG_WIDTH = PAL_WIDTH;
			IMG_HEIGHT = PAL_HEIGHT;
			break;
		}

		bitmap = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT,
				Bitmap.Config.ARGB_8888);

		LayoutParams layoutParams = getLayoutParams();
		layoutParams.width = IMG_WIDTH;
		layoutParams.height = IMG_HEIGHT;
		setLayoutParams(layoutParams);
	}

	public void surfaceChanged(SurfaceHolder holder, int format,
			int width, int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT,
					Bitmap.Config.ARGB_8888);
			;
		}
		LayoutParams layoutParams = getLayoutParams();
		layoutParams.width = IMG_WIDTH;
		layoutParams.height = IMG_HEIGHT;
		setLayoutParams(layoutParams);

		this.mainloop = new Thread(this);
		this.mainloop.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		shoudstop = true;
		while (shoudstop) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
