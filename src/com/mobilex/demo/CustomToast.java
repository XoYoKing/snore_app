package com.mobilex.demo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {
	private Toast mToast;	
	public static CustomToast makeText(Activity activity, String message){
		return new CustomToast (activity,message);
		
	}
	private CustomToast(Activity context,String message) {
		LayoutInflater inflater = context.getLayoutInflater();
		View mLayout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup)context.findViewById(R.id.toast_layout_root));
		mToast = new Toast(context);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setView(mLayout);
		TextView text = (TextView) mLayout.findViewById(R.id.text);
		text.setText(message);
	}
	public void show() {
		mToast.show();
	}
}
