package sn.nprcalendar.fragment;

import sn.nprcalendar.MainActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class OverrideObservationDialogFragment extends DialogFragment {

	// public interface NoticeDialogListener {
	// public void onDialogPositiveClick(DialogFragment dialog);
	// }
	//
	// NoticeDialogListener mListener;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		alertDialogBuilder.setTitle("Temperature for the day exists");

		// set dialog message
		alertDialogBuilder
				.setMessage("Do you want to override the observation?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((MainActivity) getActivity())
										.updateObservation();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		return alertDialogBuilder.create();
	}
}
