package com.mobilex.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

public class Settings extends FragmentActivity {
	private TextView mTriggerTime;
	private TextView mMaxSeekTime;
	private SnorePreferences mPreferences;
	private SeekBar mSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		// ActionBar actionBar = getSupportActionBar();
		// actionBar.hide();
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPreferences = new SnorePreferences(this);
		initializeViews();
	}

	private void initializeViews() {
		mTriggerTime = (TextView) findViewById(R.id.trigger_time);
		mTriggerTime.setOnClickListener(mListener);

		mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
		mSeekBar.setMax(10);
		mSeekBar.setProgress(SnoreData.getInstance().getMaxRecordingDuration());
		// mSeekBar.setProgressDrawable(getResources().getDrawable(
		// R.drawable.seekbar_drawable));
		mSeekBar.setOnSeekBarChangeListener(mSeekBarListener);
		mMaxSeekTime = (TextView) findViewById(R.id.max_seek);
		// mMaxSeekTime.setText(mPreferences.getUserSeekTime() + "min.");
		mMaxSeekTime.setText(SnoreData.getInstance().getMaxRecordingDuration()
				+ "min.");
		Button cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(mListener);

		Button okButton = (Button) findViewById(R.id.ok_button);
		okButton.setOnClickListener(mListener);
	}

	private void setMaxSeekTime(final int maxTime) {
		mMaxSeekTime.setText(maxTime + "min.");
	}


	private View.OnClickListener mListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.trigger_time:
				showTimePickerDialog(v);
				break;
			case R.id.cancel_button:
				finish();
				break;
			case R.id.ok_button:
				SnoreData.getInstance().setMaxRecordingDuration(
						mSeekBar.getProgress());

				//SnoreAlarmManager.setSnoringAlarm(Settings.this);
//                Intent intent = new Intent();
//                setResult(RESULT_OK,intent);
				finish();
				break;
			default:
			}

		}
	};
	private OnSeekBarChangeListener mSeekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// mPreferences.setUserSeekTime(progress);
			setMaxSeekTime(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

	};

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",
				Locale.getDefault());

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			// setView(hour, minute);
			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// setView(hourOfDay, minute);
			TextView textView = (TextView) getActivity().findViewById(
					R.id.trigger_time);
			final Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);
			textView.setText(sdf.format(c.getTime()));
			SnoreData.getInstance().setCalendar(c);
		}

	}
}
