package sn.nprcalendar.persistence.entity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import sn.nprcalendar.util.Constants;
import android.database.Cursor;
import android.util.Log;

public class DayObservation {

	private int id;

	private int monthId;

	private int day;

	private BigDecimal temperature;

	private boolean bleeding;

	private Date observationDate;

	public DayObservation(final int theId, final int theDay,
			final BigDecimal theTemperature, final boolean isBleeding) {
		setId(theId);
		setDay(theDay);
		temperature = theTemperature;
		bleeding = isBleeding;
	}

	public DayObservation() {
	}

	public DayObservation(final Cursor cursor) {
		new DayObservation(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), new BigDecimal(
						cursor.getString(2)), Boolean.getBoolean(cursor
						.getString(3)));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMonthId() {
		return monthId;
	}

	public void setMonthId(int monthId) {
		this.monthId = monthId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public boolean isBleeding() {
		return bleeding;
	}

	public void setBleeding(boolean bleeding) {
		this.bleeding = bleeding;
	}

	public Date getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}

	@Override
	public String toString() {
		return "DayObservation [id=" + id + ", monthId=" + monthId + ", day="
				+ day + ", temperature=" + temperature + ", bleeding="
				+ bleeding + ", observationDate=" + observationDate + "]";
	}

	public void setTemperature(final String temperatureString) {
		setTemperature(new BigDecimal(temperatureString));
	}

	public void setDay(final String day) {
		setDay(Integer.parseInt(day));
	}

	public void setObservationDate(final String string) {
		try {
			setObservationDate(Constants.DATE_FORMAT.parse(string));
		} catch (ParseException e) {
			Log.w(getClass().getSimpleName(), "Cannot convert string " + string
					+ " to date!");
		}
	}

}
