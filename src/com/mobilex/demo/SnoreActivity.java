package com.mobilex.demo;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SnoreActivity extends FragmentActivity {
    final static String TAG = "CHINEDU";
    final int ACTIVITY_RECORD_SOUND = 0;
    final String AUDIO_RECORDER_FOLDER = "snore";
    private Button mSnoreButton;
    private ProgressBar mProgressBar;
    private ProgressHandler mProgressHandler;
    private ProgressThread mProgressThread;
    private TextView mDurationLabel;
    private FragmentManager mFragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate called");


        if (savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            //FragmentTransaction transaction = mFragmentManager.beginTransaction();


        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_snore);
        initializeViews();

        if (getIntent() != null) {
            boolean isStartService = getIntent().getBooleanExtra(
                    "Start_Service", false);
            if (isStartService) {
                Log.d(TAG, "A new Intent arrived in onCreate to startSnore Service!");
                startSnoreService();
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "OnNew Intent was fired!");
        if (intent != null) {
            boolean isStartService = intent.getBooleanExtra("Start_Service",
                    false);
            if (isStartService) {
                Log.d(TAG, "Now starting Snore Service");
                startSnoreService();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case Constants.RECORDING_SCHEDULING_REQUEST:
//                if (resultCode == RESULT_OK) {
//
//                    mSnoreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                    mSnoreButton.setTypeface(Typeface.DEFAULT);
//                    mSnoreButton.setText(Html.fromHtml(String.format(getString(R.string.message), SnoreData.getInstance().getHourValue(), SnoreData.getInstance().getMinuteValue())));
//                }
//                break;
//        }
//    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume called.");
        // mProgressBar.setMax(SnoreData.getInstance().getMaxRecordingDuration());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy called.");
    }

    private void initializeViews() {
        mDurationLabel = (TextView) findViewById(R.id.max_seek);
        mSnoreButton = (Button) findViewById(R.id.snore_button);
        mSnoreButton.setOnClickListener(mListener);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressHandler = new ProgressHandler(this);

        mDurationLabel.setText(SnoreData.getInstance()
                .getMaxRecordingDuration() + "min.");
    }

    private void kickOffProgressBar() {
        mProgressThread = new ProgressThread(mProgressHandler);
        mProgressThread.setProgressBarSpeed((SnoreData.getInstance()
                .getMaxRecordingDuration() * 1000 * 60) / 100);
        mProgressBar.setProgress(0);
        mProgressThread.start();
    }

    private void configureProgressBar() {
        if (mProgressThread != null) {
            Log.d(TAG, "What is ProgressBar State:"
                    + mProgressThread.getProgressState().name());
            switch (mProgressThread.getProgressState()) {
                case NOT_RUNNING:
                case DONE:
                case INTERRUPTED:
                    kickOffProgressBar();
                    break;
            }

        } else {
            Log.d(TAG, "About to kick off progress bar");
            kickOffProgressBar();
        }
    }

    private void stopProgressBar() {
        // if (mProgressThread != null && mProgressThread.isAlive()) {
        // mProgressThread.setState(ProgressThread.DONE);
        // mProgressBar.setProgress(0);
        // mProgressBar.invalidate();
        // mProgressThread = null;
        // }
        if (mProgressThread != null) {
            mProgressThread.setProgressState(ProgressThread.PROGRESS_STATE.DONE);
        }

    }
    private void startSnoreService() {
        View view = (LinearLayout) findViewById(R.id.progress_bar_section);
        view.setVisibility(View.VISIBLE);
        startService(new Intent(SnoreActivity.this, SnoreService.class));
        configureProgressBar();
        mSnoreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        mSnoreButton.setTypeface(null, Typeface.BOLD);
        mSnoreButton.setText(getString(R.string.stop_snoring));
        mDurationLabel.setText(SnoreData.getInstance()
                .getMaxRecordingDuration() + "min.");
    }
    private void stopSnoreService() {
        stopProgressBar();
        View view = (LinearLayout) findViewById(R.id.progress_bar_section);
        view.setVisibility(View.GONE);
        Button recorderButton  = (Button)findViewById(R.id.snore_button);
        recorderButton.setText(getString(R.string.snore_button));
        stopService(new Intent(SnoreActivity.this, SnoreService.class));
    }

    private OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Button recorderButton = ((Button) v);
            String cs = recorderButton.getText().toString();
            if (cs.equalsIgnoreCase(getString(R.string.snore_button))) {
                //startSnoreService();

                SnoreAlarmManager.setSnoringAlarm(SnoreActivity.this);
                mSnoreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mSnoreButton.setTypeface(Typeface.DEFAULT);
                mSnoreButton.setText(Html.fromHtml(String.format(getString(R.string.message), SnoreData.getInstance().getHourValue(), SnoreData.getInstance().getMinuteValue())));
            } else if (cs.equalsIgnoreCase(getString(R.string.stop_snoring))) {
                //recorderButton.setText(getString(R.string.snore_button));
                stopSnoreService();
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.snore, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_app_settings: {
                Intent intent = new Intent(this, Settings.class);
               // startActivityForResult(intent, Constants.RECORDING_SCHEDULING_REQUEST);
                startActivity(intent);
                return true;
            }
            case R.id.action_snore_media: {
                Intent intent = new Intent(this, SnoreLibrary.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class ProgressHandler extends Handler {
        WeakReference<SnoreActivity> mActivity;
        int limit = 100;

        ProgressHandler(SnoreActivity activity) {
            mActivity = new WeakReference<SnoreActivity>(activity);
        }

        // void setLimit(final int progressLimit) {
        // limit = progressLimit;
        // }

        @Override
        public void handleMessage(Message message) {
            try {
                if (mActivity != null) {
                    SnoreActivity currentActivity = mActivity.get();

                    int counter = message.getData().getInt("counter");

                    if (counter == limit) {
                        if (currentActivity.mProgressThread != null) {
                            currentActivity.mProgressThread
                                    .setProgressState(ProgressThread.PROGRESS_STATE.DONE);
                            currentActivity.stopSnoreService();
                        }
                    }
                    currentActivity.mProgressBar.setProgress(counter);
                    // currentActivity.mProgressBar.invalidate();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error occurred in ProgressBar Handler:", e);

            }
        }
    }

}
