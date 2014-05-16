package com.mobilex.demo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SnoreService extends Service {
    final private String TAG = "CHINEDU";

    // private Looper mServiceLooper;
    // private ServiceHandler mServiceHandler;
    private MediaRecorder recorder;

    // private final class ServiceHandler extends Handler {
    // public ServiceHandler(Looper looper) {
    // super(looper);
    // }
    //
    // @Override
    // public void handleMessage(Message msg) {
    //
    // }
    // }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SnoreService OnCreate method called to initialize the recording API");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // recorder.setOnErrorListener(errorListener);
        // recorder.setOnInfoListener(infoListener);

        // HandlerThread thread = new HandlerThread("ServiceStartArguments",
        // android.os.Process.THREAD_PRIORITY_BACKGROUND);
        // thread.start();
        //
        // mServiceLooper = thread.getLooper();
        // mServiceHandler = new ServiceHandler(mServiceLooper);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the
        // job
        // Message msg = mServiceHandler.obtainMessage();
        // msg.arg1 = startId;
        // mServiceHandler.sendMessage(msg);
        Log.d(TAG, "Service OnStartCommand called...");
        recorder.setOutputFile(Utils.getFilename(this));
        startRecording();
        // If we get killed, after returning from here, restart
        return START_STICKY;

    }

    private void startRecording() {
        try {

            if (recorder != null) {
                recorder.prepare();
                recorder.start();
                Log.d(TAG, "Snore Service has started voice recording");
                // mProgressThread.start();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error occurred @ StartRecording():" + e.getMessage());
        }
    }

    private void stopRecording() {
        try {
            if (recorder != null) {
                Log.d(TAG, "SnoreService received a call to stop recording the snoring");
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            }


        } catch (Exception e) {
            Log.e(TAG, "Error occurred @ StopRecording:" + e.getMessage());
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.sphere_24)
                .setContentTitle("1 Recording available")
                .setContentText("Snore Recording");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent notifyIntent =
                new Intent(this, Settings.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(R.string.notification_id, builder.build());


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Snoring Service is being destroyed");
        stopRecording();

    }

}
