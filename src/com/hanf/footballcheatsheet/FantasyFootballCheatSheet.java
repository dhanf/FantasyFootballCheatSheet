package com.hanf.footballcheatsheet;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FantasyFootballCheatSheet extends TabActivity {
	
	private CheatSheetDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		

    	// Setup adapter to player database
		mDbHelper = new CheatSheetDbAdapter(this);
        mDbHelper.open();
		
        // TODO: this should be driven by user rather than happening every time application loads
		refreshPlayers();
		
        // Get resources for tabs/icons
		Resources res = getResources();
		
		// Setup tabs
		TabHost mTabHost = getTabHost();
		mTabHost.addTab(mTabHost.newTabSpec("tab_players").setIndicator("Players", res.getDrawable(R.drawable.ic_tab_players))
				.setContent(new Intent().setClass(this, Players.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab_drafted_players").setIndicator("Drafted Players", res.getDrawable(R.drawable.ic_tab_drafted_players))
				.setContent(new Intent().setClass(this, DraftedPlayers.class)));	
		mTabHost.addTab(mTabHost.newTabSpec("tab_draft_log").setIndicator("Draft Log", res.getDrawable(R.drawable.ic_tab_draft_log))
				.setContent(new Intent().setClass(this, DraftLog.class)));
		mTabHost.setCurrentTab(0);
    }
    
    /** Load players into database */
    private void refreshPlayers(){
        // Clean out database
        mDbHelper.deleteAllPlayers();
        
        // TODO: pull from Internet - hard-coding test data for now
        mDbHelper.createPlayer("Chris Johnson", "1", "TEN", "RB", "9");
        mDbHelper.createPlayer("Drew Brees", "2", "NO", "QB", "8");
        mDbHelper.createPlayer("Maurice Jones-Drew", "3", "JAC", "RB", "5");
        mDbHelper.createPlayer("Matt Forte", "4", "CHI", "RB", "5");
        mDbHelper.createPlayer("Michael Turner", "5", "ATL", "RB", "7");
        mDbHelper.createPlayer("Peyton Manning", "6", "IND", "QB", "4");
        mDbHelper.createPlayer("Larry Fitzgerald", "7", "ARI", "WR", "9");
        mDbHelper.createPlayer("Adrian Peterson", "8", "MIN", "RB", "6");
        mDbHelper.createPlayer("LaDainian Tomlinson", "9", "SD", "RB", "4");
        mDbHelper.createPlayer("Dallas Clark", "10", "IND", "TE", "4");
    }
}