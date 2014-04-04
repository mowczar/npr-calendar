package sn.nprcalendar.persistence.entity;

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
	private static final String KEY_ID = "id";
	private static final String KEY_DAY = "day";
	private static final String KEY_TEMPERATURE = "temperature";
	private static final String KEY_IS_BLEEDING = "isBleeding";
	private static final String KEY_MONTH_ID = "month_id";
	private static final String KEY_DATE_DAY = "dateDay";
	private static final String KEY_DATE_HOUR = "dateHour";
	private static final String[] KEY_TABLE = new String[] { KEY_ID, KEY_DAY,
			KEY_TEMPERATURE, KEY_IS_BLEEDING, KEY_MONTH_ID, KEY_DATE_DAY,
			KEY_DATE_HOUR };
	private SQLiteDatabase db;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		setDb();
	}

	public void setDb() {
		this.db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String TABLE = "CREATE TABLE " + TABLE_OBSERVATIONS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DAY
				+ " INTEGER NOT NULL," + KEY_TEMPERATURE + " REAL,"
				+ KEY_IS_BLEEDING + " INTEGER," + KEY_MONTH_ID
				+ " INTEGER NOT NULL," + KEY_DATE_DAY
				+ " STRING NOT NULL UNIQUE," + KEY_DATE_HOUR + " STRING,"
				+ "CHANGED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,"
				+ "CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP)";
		db.execSQL(TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
		onCreate(db);
	}

	public long addDayObservation(final DayObservation observation) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createDayObservationContentValues(observation);
		long id = db.insert(TABLE_OBSERVATIONS, null, values);
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
		values.put(KEY_MONTH_ID, observation.getMonthId());
		values.put(KEY_DATE_DAY, observation.getDateDay());
		values.put(KEY_DATE_HOUR, observation.getDateHour());
		return values;
	}

	public DayObservation getDayObservation(int id) {
		Cursor cursor = db.query(TABLE_OBSERVATIONS, KEY_TABLE, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			return createDayObservation(cursor);
		}
		return null;
	}

	public DayObservation getDayObservation(final Date date) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_OBSERVATIONS, KEY_TABLE, KEY_DATE_DAY
				+ " = '" + Constants.DATE_DAY_FORMAT.format(date) + "'", null,
				null, null, null);
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
		dayObservation.setId(cursor.getInt(0));
		dayObservation.setDay(cursor.getInt(1));
		dayObservation.setTemperature(cursor.getString(2));
		dayObservation.setBleeding(cursor.getInt(3) > 0);
		dayObservation.setMonthId(cursor.getInt(4));
		dayObservation.setDateDay(cursor.getString(5));
		dayObservation.setDateHour(cursor.getString(6));
		return dayObservation;
	}

	public int updateDayObservation(final DayObservation dayObservation) {
		// SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createDayObservationContentValues(dayObservation);
		return db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(dayObservation.getId()) });
	}

	public void deleteObservation(DayObservation dayObservation) {
		// SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OBSERVATIONS, KEY_ID + " = ?",
				new String[] { String.valueOf(dayObservation.getId()) });
		db.close();
	}

	public int getObservationCount() {
		String countQuery = "SELECT  * FROM " + TABLE_OBSERVATIONS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		return cursor.getCount();
	}

	public String calculateDayOfCycle() {
		return "";
	}

	public DayObservation getLatestObservation() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT max(" + KEY_DATE_DAY + ") FROM " + TABLE_OBSERVATIONS, null);
		if (cursor != null) {
			cursor.moveToFirst();
			String maxDate = cursor.getString(0);
			if (maxDate != null) {
				String query = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + KEY_DATE_DAY + " = ?";
				cursor = db.rawQuery(query, new String[] {maxDate});
			}
		}
//		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OBSERVATIONS, null);
		
//		Cursor cursor = db.query(TABLE_OBSERVATIONS, KEY_TABLE, KEY_DATE_DAY + "=max(" + KEY_DATE_DAY + ")",
//				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			return createDayObservation(cursor);
		}
		return null;
	}

}
