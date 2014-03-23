package sn.nprcalendar.persistence.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sn.nprcalendar.util.Constants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "observationManager";
	private static final String TABLE_OBSERVATIONS = "day_observations";
	public static final String KEY_ID = "id";
	public static final String KEY_DAY = "day";
	public static final String KEY_TEMPERATURE = "temperature";
	public static final String KEY_IS_BLEEDING = "isBleeding";
	public static final String KEY_TIME = "time";
	private SQLiteDatabase db;

	public DatabaseHandler() {
		super(null, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getReadableDatabase();
	}

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getReadableDatabase();
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String TABLE = "CREATE TABLE " + TABLE_OBSERVATIONS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DAY + " INTEGER,"
				+ KEY_TEMPERATURE + " REAL," + KEY_IS_BLEEDING + " INTEGER,"
				+ KEY_TIME + " STRING,"
				+ "CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP)";
		db.execSQL(TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);

		// Create tables again
		onCreate(db);
	}

	// code to add the new contact
	public long addDayObservation(final DayObservation observation) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = createDayObservationContentValues(observation);

		// Inserting Row
		long id = db.insert(TABLE_OBSERVATIONS, null, values);
		// 2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection

		return id;
	}

	private ContentValues createDayObservationContentValues(
			final DayObservation observation) {
		ContentValues values = new ContentValues();
		values.put(KEY_DAY, observation.getDay()); // observation day
		values.put(KEY_TEMPERATURE, observation.getTemperature()
				.toPlainString()); // temperature
		values.put(KEY_IS_BLEEDING, observation.isBleeding());
		values.put(KEY_TIME, getDateTime(observation.getObservationDate()));
		return values;
	}

	private String getDateTime(final Date date) {
		return Constants.DATE_FORMAT.format(date);
	}

	public DayObservation getDayObservation(int id) {
		Cursor cursor = db.query(TABLE_OBSERVATIONS, new String[] { KEY_ID,
				KEY_DAY, KEY_TEMPERATURE, KEY_IS_BLEEDING }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			return new DayObservation(cursor);
		}
		return null;
	}

	public DayObservation getDayObservation(final Date date) {
		Cursor cursor = db.query(TABLE_OBSERVATIONS, new String[] { KEY_ID,
				KEY_DAY, KEY_TEMPERATURE, KEY_IS_BLEEDING, KEY_TIME }, KEY_TIME
				+ " LIKE '" + Constants.DATE_DAY_FORMAT.format(date) + "%'",
				null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			DayObservation o = createDayObservation(cursor);
			Log.d("DATABASE", "Observation found: " + o.toString());
			return o;
		}
		return null;
	}

	public List<DayObservation> getAllDayObservations() {
		List<DayObservation> dayObservationList = new ArrayList<DayObservation>();

		String selectQuery = "SELECT  * FROM " + TABLE_OBSERVATIONS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				dayObservationList.add(createDayObservation(cursor));
			} while (cursor.moveToNext());
		}

		return dayObservationList;
	}

	private DayObservation createDayObservation(Cursor cursor) {
		DayObservation dayObservation = new DayObservation();
		dayObservation.setId(Integer.parseInt(cursor.getString(0)));
		dayObservation.setDay(Integer.parseInt(cursor.getString(1)));
		dayObservation.setTemperature(new BigDecimal(cursor.getString(2)));
		dayObservation.setBleeding(Boolean.getBoolean(cursor.getString(3)));
		return dayObservation;
	}

	public int updateDayObservation(final DayObservation dayObservation) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = createDayObservationContentValues(dayObservation);

		// updating row
		return db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(dayObservation.getId()) });
	}

	// Deleting single contact
	public void deleteContact(DayObservation dayObservation) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OBSERVATIONS, KEY_ID + " = ?",
				new String[] { String.valueOf(dayObservation.getId()) });
		db.close();
	}

	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_OBSERVATIONS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}

}
