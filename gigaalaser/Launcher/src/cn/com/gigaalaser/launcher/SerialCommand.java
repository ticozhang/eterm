package cn.com.gigaalaser.launcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Handler;

public class SerialCommand extends Thread {
	public static final int CMD_CAPTURE = 100;
	public static final int CMD_STOP_CAPTURE = 101;

	private final int BAUDRATE = 115200;
	private final String DEVICE = "/dev/ttyS1";

	private final char In_BACK = 8;
	private final char In_DELETE = 127;
	private final char In_EOF = 26;
	private final char In_EOL = 13;
	private final byte[] Out_DELETE = { 8, 32, 8 };
	private final byte[] Out_EOF = { 26 };

	private Handler cmdHandler = null;
	private int cmd_ptr = 0;
	private byte[] commandline = new byte[1024];
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private SerialPort serial = null;
	private boolean stopme = false;

	public SerialCommand(Handler handler) throws SecurityException, IOException {

		cmdHandler = handler;
		serial = new SerialPort(new File(DEVICE), BAUDRATE, 0);
		outputStream = serial.getOutputStream();
		inputStream = serial.getInputStream();
	}

	public void checkCommand(byte[] cmd) {
		String str = new String(cmd);
		if (str.contains("help")) {
			putMessage("This is a help command line\r\n".getBytes());
		} else if (str.contains("capture")) {
			putMessage("Start video capture\r\n".getBytes());
			cmdHandler.sendEmptyMessage(CMD_CAPTURE);
		} else if (str.contains("stop")) {
			putMessage("Stop video capture\r\n".getBytes());
			cmdHandler.sendEmptyMessage(CMD_STOP_CAPTURE);
		} else {
			putMessage("Unknown command!!!\r\n".getBytes());
		}
		putMessage("LA>".getBytes());

	}

	//this getChar is low performance
	//just a demo
	public char getChar() {
		try {
			if(inputStream.available()>0){
				byte[] buffer = new byte[1];
				inputStream.read(buffer);
				return (char) buffer[0];
			}else{
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void putByte(byte data) {
		byte[] buf = new byte[1];
		buf[0] = data;
		try {
			this.outputStream.write(buf);
			this.outputStream.flush();
		} catch (IOException e) {
		}
	}

	public void putMessage(byte[] msg) {
		try {
			this.outputStream.write(msg);
			this.outputStream.flush();
		} catch (IOException e) {
		}
	}

	public void run() {
		putMessage("\r\nWellcome to LA command line.\r\n".getBytes());
		putMessage("\r\nLA>".getBytes());

		while (!stopme) {
			char c = getChar();
			if ((c == In_EOF) && (cmd_ptr == 0)) {
				putMessage(Out_EOF);
				continue;
			}

			if (c == In_DELETE || c == In_BACK) {
				if (cmd_ptr != 0) {
					cmd_ptr--;
					putMessage(Out_DELETE);
				}
			} else if (c == In_EOL) {
				putByte((byte) '\r');
				commandline[cmd_ptr++] = '\r';
				putByte((byte) '\n');
				commandline[cmd_ptr++] = '\n';
				commandline[cmd_ptr] = 0;
				cmd_ptr = 0;

				checkCommand(commandline);
			} else if (cmd_ptr < (commandline.length - 1)) {
				if (c >= ' ') {
					commandline[cmd_ptr++] = (byte) c;
					putByte((byte) c);
				}

			} else {
				putByte((byte) '\7');
			}
		}

	}

	public void stopMe() {
		this.stopme = true;
	}
}
