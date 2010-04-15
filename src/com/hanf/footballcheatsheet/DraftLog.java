package com.hanf.footballcheatsheet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DraftLog extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("This will have the draft log");
		setContentView(textview);
	}
}