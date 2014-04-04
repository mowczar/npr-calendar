package sn.nprcalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import sn.nprcalendar.adapter.TabsPagerAdapter;
import sn.nprcalendar.fragment.OverrideObservationDialogFragment;
import sn.nprcalendar.persistence.entity.DatabaseHandler;
import sn.nprcalendar.persistence.entity.DayObservation;
import sn.nprcalendar.util.Constants;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String OBSERVATION = "Observation";
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private DayObservation dayObservation = new DayObservation();
	private String[] tabs = { "Obserwacja", "Wykres", "Historia" };
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		db = new DatabaseHandler(this);
		putDayValue();
	}

	private void putDayValue() {
		EditText dayText = (EditText) findViewById(R.id.dayPicker);
		dayText.setText(db.calculateDayOfCycle());
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		// NumberPicker dayNbrPck = (NumberPicker) findViewById(R.id.dayPicker);
		// dayNbrPck.setMinValue(1);
		// dayNbrPck.setMaxValue(50);
		// dayNbrPck.setWrapSelectorWheel(false);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public void saveButtonClicked(View v) {
		updateOneObservationDTO();
		Date compareDate = dayObservation.getDate();

		DayObservation existingObservation = db
				.getDayObservation(dayObservation.getDate());
		if (existingObservation == null) {
			saveObservation();
		} else {
			dayObservation.setId(existingObservation.getId());
			DialogFragment dialog = new OverrideObservationDialogFragment();
			dialog.show(getSupportFragmentManager(),
					"OverrideObservationDialogFragment");
		}
	}

	public void saveObservation() {
		Log.d(OBSERVATION,
				"Trying to save observation" + dayObservation.toString());
		long id = db.addDayObservation(dayObservation);
		if (id != -1) {
			Log.d(OBSERVATION, "Record with Id: " + id + " saved"); //$NON-NLS-1$ //$NON-NLS-2$
			showToastMessage("Temperature " //$NON-NLS-1$
					+ dayObservation.getTemperature().toPlainString()
					+ " saved."); //$NON-NLS-1$
		} else {
			Log.e(OBSERVATION, "Error vhile saving observation"); //$NON-NLS-1$
			showToastMessage("Unexpected error occured while saving observation"); //$NON-NLS-1$
		}
	}

	public void clearDatabase(View v) {
		Log.d(STORAGE_SERVICE, "Trying to clear database");
		db.onUpgrade(db.getWritableDatabase(), 3, 3);
	}

	private void updateOneObservationDTO() {
		EditText dayText = (EditText) findViewById(R.id.dayPicker);
		EditText temperatureText = (EditText) findViewById(R.id.temperatureTxtEdt);
		CheckBox isBleedingChkBox = (CheckBox) findViewById(R.id.periodChkBox);

		dayObservation.setDay(dayText.getText().toString());
		dayObservation.setTemperature(temperatureText.getText().toString());
		dayObservation.setBleeding(isBleedingChkBox.isChecked());
	}

	public void selectDate(View view) {
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getSupportFragmentManager(), "DatePicker");
	}

	public void populateSetDate(final Date date) {
		EditText mEdit = (EditText) findViewById(R.id.dateTxtEdt);
		mEdit.setText(Constants.DATE_FORMAT.format(date));
	}

	@SuppressLint("ValidFragment")
	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			Calendar cal = new GregorianCalendar(yy, mm, yy);
			dayObservation.setDate(cal.getTime());
			populateSetDate(cal.getTime());
		}
	}

	private void showToastMessage(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void updateObservation() {
		Log.d(OBSERVATION,
				"Trying to update observation" + dayObservation.toString());
		long id = db.updateDayObservation(dayObservation);
		if (id != -1) {
			Log.d(OBSERVATION,
					"Record with Id: " + dayObservation.getId() + " updated"); //$NON-NLS-1$ //$NON-NLS-2$
			showToastMessage("Temperature " //$NON-NLS-1$
					+ dayObservation.getTemperature().toPlainString()
					+ " saved."); //$NON-NLS-1$
		} else {
			Log.e(OBSERVATION, "Error vhile saving observation"); //$NON-NLS-1$
			showToastMessage("Unexpected error occured while saving observation"); //$NON-NLS-1$
		}
	}

}
