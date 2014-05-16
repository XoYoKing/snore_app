package com.mobilex.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ProgressThread extends Thread {
	enum PROGRESS_STATE {
		RUNNING, NOT_RUNNING, INTERRUPTED, DONE
	}

	private final String TAG = "ProgressThread";
	private Handler mHandler;
	private PROGRESS_STATE mState= PROGRESS_STATE.NOT_RUNNING;
	private int counter;
	private long speed;
	ProgressThread(Handler handler) {
		mHandler = handler;
	}

	void setProgressBarSpeed(int velocity) {
		speed = velocity;
	}

	@Override
	public void run() {
		mState = PROGRESS_STATE.RUNNING;
		counter = 0;
		while (mState == PROGRESS_STATE.RUNNING) {
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				mState = PROGRESS_STATE.INTERRUPTED;
				Log.e(TAG, e.getMessage());
			}
			Message message = mHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putInt("counter", counter);
			message.setData(bundle);
			mHandler.sendMessage(message);
			counter++; // up counter
		}
	}

	public void setProgressState(PROGRESS_STATE state) {
		mState = state;
	}

	public PROGRESS_STATE getProgressState() {
		return mState;
	}
}
