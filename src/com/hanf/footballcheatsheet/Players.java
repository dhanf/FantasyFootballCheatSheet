package com.hanf.footballcheatsheet;

import java.util.List;
import java.util.Vector;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Players extends ListActivity {
	private LayoutInflater mInflater;
	private Vector<PlayerData> playerList;
	
	private CheatSheetDbAdapter mDbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		// Setup adapter to cheat sheet database
		mDbHelper = new CheatSheetDbAdapter(this);
        mDbHelper.open();
        
		// Get players from database for display	
        getPlayers();		
		
		// Tie our player adapter to the list view
		PlayerAdapter adapter = new PlayerAdapter(this, R.layout.player_list_item, R.id.PlayerName, playerList);		
		setListAdapter(adapter);
		getListView().setTextFilterEnabled(true);
	}

	/*public void onListItemClick(ListView parent, View v, int position, long id) {
		PlayerAdapter adapter = (PlayerAdapter) parent.getAdapter();
		PlayerData player = (PlayerData)adapter.getItem(position);
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(player.playerName);
		builder.setMessage(player.playerName + " -> " + position);
		builder.setPositiveButton("ok", null);
		builder.show();
	}*/

	private class PlayerData {
		protected long id;
		protected String name;
		protected String rank;
		protected String team;
		protected String position;
		protected String byeWeek;

		PlayerData(int Id, String Name, String Rank, String Team, String Position, String ByeWeek) {			
			id = Id;
			name = Name;
			rank = Rank;
			team = Team;
			position = Position;
			byeWeek = ByeWeek;
		}

		@Override
		public String toString() {
			return name + " " + team + " " + position;
		}
	}

	private class PlayerAdapter extends ArrayAdapter<PlayerData> {

		public PlayerAdapter(Context context, int resource,	int textViewResourceId, List<PlayerData> objects) {
			super(context, resource, textViewResourceId, objects);
		}    
		
		public long getItemId(int position) {            
			return getItem(position).id;        
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			// Recycle convertView if possible into our efficient ViewHolder
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.player_list_item, null);
				
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.PlayerName);
				holder.rank = (TextView) convertView.findViewById(R.id.PlayerRank);
				holder.team = (TextView) convertView.findViewById(R.id.PlayerTeam);
				holder.position = (TextView) convertView.findViewById(R.id.PlayerPosition);
				holder.byeWeek = (TextView) convertView.findViewById(R.id.PlayerByeWeek);
				
				convertView.setTag(holder);
			}else{				
				holder = (ViewHolder) convertView.getTag();
			}
			
			// Call getItem from the array adapter to get the PlayerData object at position
			PlayerData player = getItem(position);
			
			// Set view text using player object
			holder.name.setText(player.name);
			holder.rank.setText("Rank - " + player.rank);
			holder.team.setText(player.team);
			holder.position.setText(player.position);
			holder.byeWeek.setText("Bye Week: " + player.byeWeek);
			
			return convertView;
		}
	}

	static class ViewHolder {
		TextView name;
		TextView rank;
		TextView team;
		TextView position;
		TextView byeWeek;
	}
	
	/** Retrieve all players from database */
	private void getPlayers() {
        // Get all of the players into cursor
        Cursor c = mDbHelper.fetchAllPlayers();
        startManagingCursor(c);
        
        // (Re)Initialize player list
        playerList = new Vector<PlayerData>();
        
        // Put players into our playerData object
        if(c.moveToFirst())
        {
        	do{
        		int id = c.getInt(c.getColumnIndex(CheatSheetDbAdapter.KEY_PLAYER_ROWID)); 
                String name = c.getString(c.getColumnIndex(CheatSheetDbAdapter.KEY_PLAYER_NAME));
                String rank = c.getString(c.getColumnIndex(CheatSheetDbAdapter.KEY_PLAYER_RANK));
                String team = c.getString(c.getColumnIndex(CheatSheetDbAdapter.KEY_PLAYER_TEAM));
                String position = c.getString(c.getColumnIndex(CheatSheetDbAdapter.KEY_PLAYER_POSITION));
                String byeWeek = c.getString(c.getColumnIndex(CheatSheetDbAdapter.KEY_PLAYER_BYEWEEK));
                
                // Add to local player list
                playerList.add(new PlayerData(id, name, rank, team, position, byeWeek)); 
        	}while(c.moveToNext());
        }
    }
}
