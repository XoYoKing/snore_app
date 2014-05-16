package com.mobilex.demo;

import java.util.Calendar;

public class SnoreData {
	private static SnoreData instance = null;
	private int mMaxRecordingDuration = 0;
	private Calendar mCalendar;
    private int mHour;
    private int mMinute;
	private SnoreData() {

	}

	public static SnoreData getInstance() {
		if (instance == null) {
			instance = new SnoreData();
		}
		return instance;
	}

	public int getMaxRecordingDuration() {
		if (mMaxRecordingDuration == 0) {
			mMaxRecordingDuration = 2;
		}
		return mMaxRecordingDuration;
	}

	public void setMaxRecordingDuration(int duration) {
		mMaxRecordingDuration = duration;
	}

	public void setCalendar(Calendar calendar) {
		mCalendar = calendar;
	}

	public Calendar getCalendar() {
		return mCalendar;
	}

    public void setHourValue (int hourValue){
        mHour=hourValue;
    }
    public int getHourValue(){
        return mHour;
    }
    public void setMinuteValue(int minuteValue){
        mMinute=minuteValue;
    }
    public int getMinuteValue(){
        return mMinute;
    }
}
