package com.mobilex.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class SnoreLibrary extends FragmentActivity {
    private final String TAG = "SnoreLibrary";
    private GridView mGridView;
    //private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.snore_library);
        //intializeMedia();
        mGridView = (GridView) findViewById(R.id.snore_grid);
        final SnoreAdapter adapter = new SnoreAdapter(this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ImageView imageView = (ImageView) view.findViewById(R.id.media_image);
                File file = (File) imageView.getTag();
                playSnoreFile(file);
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                 @Override
                                                 public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                     AlertDialog dialog;
                                                     ImageView imageView = (ImageView) view.findViewById(R.id.media_image);
                                                     Log.d(TAG, "Long item button clicked...");
                                                     final File file = (File) imageView.getTag();
                                                     if (file != null) {
                                                         Log.d(TAG, "What is file name?" + file.getAbsolutePath());
                                                         AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                                         builder.setMessage(getString(R.string.want_to_delete))
                                                                 .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         file.delete();
                                                                         ((SnoreAdapter) adapter).refreshData(SnoreLibrary.this);
                                                                         adapter.notifyDataSetChanged();
                                                                     }
                                                                 })
                                                                 .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         dialog.dismiss();
                                                                     }
                                                                 });
                                                         dialog = builder.create();
                                                         dialog.show();
                                                     }
                                                     return true;
                                                 }
                                             }
        );


        // MediaMetadataRetriever m = new MediaMetadataRetriever();
        // m.setDataSource("/mnt/sdcard/Android/data/com.mobilex.demo/files/Snore/1395502197158.mp3");
        // byte [] b = m.getEmbeddedPicture();
        // Log.d("CHINEDU","What is B:"+b);
    }

//	private void intializeMedia() {
//		mMediaPlayer = new MediaPlayer();
//		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		
//	}

    protected void playSnoreFile(final File file) {

        try {
            //Uri uri = Uri.fromFile(file);
//			mMediaPlayer.setDataSource(getApplicationContext(), uri);
//			mMediaPlayer.prepare();
//			mMediaPlayer.start();
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error occurred @ PlaySnoreFile", e);
        }
    }

    protected void stopPlaying() {
//		try{
//			if (mMediaPlayer!=null){
//				mMediaPlayer.stop();
//			}
//		}catch (Exception e){
//			Log.e(TAG,"Error occurred while stopping:",e);
//		}
    }

    @Override
    protected void onStop() {
//		try{
//			if (mMediaPlayer!=null){
//				mMediaPlayer.release();
//				mMediaPlayer=null;
//			}
//		}catch (Exception e){
//			Log.e(TAG,"Error releasing Media Player:",e);
//		}
        super.onStop();
    }

    static class ViewHolder {
        ImageView imageView;
        TextView labelView;
    }

    public class SnoreAdapter extends BaseAdapter {

        //private Context mContext;
        private File[] mMediaFiles = null;
        private LayoutInflater mInflater;

        public SnoreAdapter(Context c) {
            //mContext = c;
            mInflater = LayoutInflater.from(c);
            mMediaFiles = Utils.getPhotoFiles(c);
        }

        public void refreshData(Context context) {
            mMediaFiles = Utils.getPhotoFiles(context);
        }

        public int getCount() {
            if (mMediaFiles == null) {
                return 0;
            }
            return mMediaFiles.length;
        }

        public Object getItem(int position) {
            if (mMediaFiles == null) {
                return null;
            }
            return mMediaFiles[position];
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.grid_cell, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.media_image);
                viewHolder.labelView = (TextView) convertView.findViewById(R.id.label);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            File tag = mMediaFiles[position];
            viewHolder.imageView.setTag(tag);
            int pos = position + 1;
            viewHolder.labelView.setText(String.valueOf(pos));
            return convertView;
        }
    }
}
