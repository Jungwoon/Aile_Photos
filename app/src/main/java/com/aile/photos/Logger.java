package com.aile.photos;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

/**
 * API for sending log output.
 * 
 * @see SHOW_LOGS if true, then logs are appeared,
 */

public class Logger {

	/* 배포시 SHOW_LOGS = false 로 변경 할 것 */
	private static boolean SHOW_LOGS = false;

	private Logger() {
	}

	/* d */
	public static void d(String tag, String msg1, String msg2) {
		if (SHOW_LOGS) {
			Log.d(tag, msg1 + " : " + msg2);
		}
	}

	public static void d(String tag, String msg1, IOException msg2) {
		if (SHOW_LOGS) {
			Log.d(tag, msg1 + " : " + msg2);
		}
	}

	public static void d(String tag, String msg1, JSONException msg2) {
		if (SHOW_LOGS) {
			Log.d(tag, msg1 + " : " + msg2);
		}
	}

	public static void d(String tag, String msg1, InterruptedException msg2) {
		if (SHOW_LOGS) {
			Log.d(tag, msg1 + " : " + msg2);
		}
	}

	/* e */
	public static void e(String tag, String msg1, String msg2) {
		if (SHOW_LOGS) {
			Log.e(tag, msg1 + " : " + msg2);
		}
	}

	public static void e(String tag, String msg1, IOException msg2) {
		if (SHOW_LOGS) {
			Log.e(tag, msg1 + " : " + msg2);
		}
	}

	public static void e(String tag, String msg1, JSONException msg2) {
		if (SHOW_LOGS) {
			Log.e(tag, msg1 + " : " + msg2);
		}
	}

	public static void e(String tag, String msg1, InterruptedException msg2) {
		if (SHOW_LOGS) {
			Log.e(tag, msg1 + " : " + msg2);
		}
	}

	/* i */
	public static void i(String tag, String msg1, String msg2) {
		if (SHOW_LOGS) {
			Log.i(tag, msg1 + " : " + msg2);
		}
	}

	public static void i(String tag, String msg1, IOException msg2) {
		if (SHOW_LOGS) {
			Log.i(tag, msg1 + " : " + msg2);
		}
	}

	public static void i(String tag, String msg1, JSONException msg2) {
		if (SHOW_LOGS) {
			Log.i(tag, msg1 + " : " + msg2);
		}
	}

	public static void i(String tag, String msg1, InterruptedException msg2) {
		if (SHOW_LOGS) {
			Log.i(tag, msg1 + " : " + msg2);
		}
	}

	/* w */
	public static void w(String tag, String msg1, String msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}

	public static void w(String tag, String msg1, IOException msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}

	public static void w(String tag, String msg1, JSONException msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}

	public static void w(String tag, String msg1, InterruptedException msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}

	/* v */
	public static void v(String tag, String msg1, String msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}

	public static void v(String tag, String msg1, IOException msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}

	public static void v(String tag, String msg1, JSONException msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}

	public static void v(String tag, String msg1, InterruptedException msg2) {
		if (SHOW_LOGS) {
			Log.w(tag, msg1 + " : " + msg2);
		}
	}
}
