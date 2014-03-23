package sn.nprcalendar.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import android.database.Cursor;

public class DayObservation {

	private int id;

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
		return "DayObservation [id=" + id + ", day=" + day + ", temperature="
				+ temperature + ", bleeding=" + bleeding + ", observationDate="
				+ observationDate + "]";
	}

	public void setTemperature(final String temperatureString) {
		setTemperature(new BigDecimal(temperatureString));
	}

	public void setDay(final String day) {
		setDay(Integer.parseInt(day));
	}

}
