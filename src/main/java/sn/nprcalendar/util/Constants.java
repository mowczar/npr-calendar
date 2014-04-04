package sn.nprcalendar.util;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

public class Constants {
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat DATE_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat DATE_HOUR_FORMAT = new SimpleDateFormat("HH:mm");
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
}
