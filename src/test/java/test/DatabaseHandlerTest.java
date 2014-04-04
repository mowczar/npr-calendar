package test;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import sn.nprcalendar.persistence.entity.DatabaseHandler;
import sn.nprcalendar.persistence.entity.DayObservation;

@RunWith(MyTestRunner.class)
public class DatabaseHandlerTest extends TestCase {

	private DatabaseHandler handler = new DatabaseHandler(
			Robolectric.application);

	@Before
	public void setUp() throws Exception {
		super.setUp();
		if (handler.getWritableDatabase() == null) {
			handler.setDb();
		}
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void addSingleObservation() {
		Date date = new GregorianCalendar(2014, 1, 1, 7, 0).getTime();
		DayObservation observationEntry = createOneObservation(date);
		handler.addDayObservation(observationEntry);
		DayObservation savedObservation = handler.getDayObservation(date);
		assertThat(savedObservation).isNotNull();
		assertThat(savedObservation).isEqualToIgnoringGivenFields(
				observationEntry, "id");
	}

	@Test
	public void addMonthOfObservation() {
		List<DayObservation> observationList = createMonthObservation();
		for (DayObservation o : observationList) {
			handler.addDayObservation(o);
		}
		List<DayObservation> savedList = handler.getAllDayObservations();
		assertThat(savedList).isNotEmpty();
		assertThat(observationList.size()).isEqualTo(savedList.size());
	}
	
	@Test
	public void calculateDayOfCycle() {
		Date date = new GregorianCalendar(2014, 1, 1, 7, 0).getTime();
		DayObservation observationEntry = createOneObservation(date);
		handler.addDayObservation(observationEntry);
		DayObservation lastObservation = handler.getLatestObservation();
		
		assertThat(lastObservation).isNotNull();
		assertThat(lastObservation).isEqualToIgnoringGivenFields(
				observationEntry, "id");
	}

	private List<DayObservation> createMonthObservation() {
		Random r = new Random();
		List<DayObservation> monthObservation = new ArrayList<DayObservation>(
				30);
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2013);
			cal.set(Calendar.HOUR, 7);
			cal.set(Calendar.MINUTE, 0);
			for (int i = 1; i < 31; i++) {
				DayObservation observation = new DayObservation();
				observation.setMonthId(1);
				observation.setDay(i);
				observation.setBleeding((i < 7) ? true : false);
				cal.set(Calendar.DAY_OF_YEAR, i);
				observation.setDate(cal.getTime());
				BigDecimal temperature = new BigDecimal("3"
						+ (5 + r.nextInt(4)) + "." + r.nextInt(9));
				observation.setTemperature(temperature);
				monthObservation.add(observation);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
		return monthObservation;
	}

	private DayObservation createOneObservation(final Date date) {
		DayObservation dayObservation = new DayObservation();
		dayObservation.setDay(2);
		dayObservation.setBleeding(true);
		dayObservation.setTemperature("36.5");
		dayObservation.setDate(date);
		dayObservation.setMonthId(1);
		return dayObservation;
	}

}
