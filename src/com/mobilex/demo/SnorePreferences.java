package com.mobilex.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SnorePreferences {
	private Context mContext;
	private SharedPreferences mPref;
	private final static String PREF_USER_SEEK_TIME = "userseektime";

	public SnorePreferences(Context context, String prefsId) {
		mContext = context;
		if (prefsId == null) {
			mPref = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			mPref = context.getSharedPreferences(prefsId, Context.MODE_PRIVATE);
		}
	}

	public SnorePreferences(Context context) {
		this(context, null);
	}

	public SharedPreferences getShared() {
		return mPref;
	}

	public Context getContext() {
		return mContext;
	}

//	public void setUserSeekTime(int seekTime) {
//		getShared().edit().putInt(PREF_USER_SEEK_TIME, seekTime).commit();
//	}
//
//	public int getUserSeekTime() {
//		return mPref.getInt(PREF_USER_SEEK_TIME, getContext().getResources()
//				.getInteger(R.integer.default_recording_duration));
//	}

}
