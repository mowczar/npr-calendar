package sn.nprcalendar.util;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

public class Constants {
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat DATE_DAY_FORMAT = new SimpleDateFormat(
			"dd.MM.yyyy");
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm");
}
