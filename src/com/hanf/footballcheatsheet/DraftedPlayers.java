package com.hanf.footballcheatsheet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DraftedPlayers extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("This will have the drafted players");
		setContentView(textview);
	}
}