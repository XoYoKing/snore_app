package com.mobilex.demo;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


public class Utils {

	public static String getFilesPath(Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/Android/data/" + context.getPackageName() + "/files/"
					+ context.getString(R.string.app_name);
		} else {
			return context.getFilesDir().toString();
		}
	}

	public static File[] getPhotoFiles(Context context) {
		try {
			File appDirectory = context.getFilesDir();
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				appDirectory = new File(getFilesPath(context));
			}
			FilenameFilter pngFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {

					return name.endsWith(".mp3");
				}
			};
			return appDirectory.listFiles(pngFilter);
		} catch (Exception e) {
			Log.e("Utils", "Error occured getting Snore files:", e);
		}
		return null;
	}

	public static String getFilename(Context context) {
		File file = new File(getFilesPath(context));
		if (!file.exists()) {
			file.mkdirs();
		}

		return getFilesPath(context) + "/" + System.currentTimeMillis()
				+ ".mp3";
	}
}
