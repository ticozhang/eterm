package cn.com.gigaalaser.launcher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BusyIoPin {
	private static final String BUSY_IO_PIN = "/sys/class/gpio/gpio214/value";
	private static FileOutputStream fos = null;

	
	public static boolean setPinValue(int value) {
		if (fos == null) {
			try {
				fos = new FileOutputStream(BUSY_IO_PIN);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		if (fos != null) {
			byte buffer[] = new byte[1];
			if(value == 0){
				buffer[0] = '0';
			}else{
				buffer[0] = '1';
			}
			try {
				fos.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
