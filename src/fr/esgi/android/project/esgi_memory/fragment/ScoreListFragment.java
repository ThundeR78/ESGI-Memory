package fr.esgi.android.project.esgi_memory.fragment;


import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import fr.esgi.android.project.esgi_memory.R;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.db.DatabaseHandler;
import fr.esgi.android.project.esgi_memory.view.ScoreListItemAdapter;

public class ScoreListFragment extends ListFragment {
	
	public static final String TAG ="ScoreListFragment";
	
	private int level;
//	private

	public ScoreListFragment() {
		super();
	}
	
	public ScoreListFragment(int level) {
		super();
		
		this.level = level;
		Log.v(TAG, "Level="+level);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_score_list, container, false);
		
		
		
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		DatabaseHandler db = new DatabaseHandler(getActivity());
		updateListAdapter(db.getAllScores());
	}
	
	private void updateListAdapter(List<Score> inItems) {
		ArrayAdapter<Score> arrayAdapter = new ScoreListItemAdapter(getActivity(), R.layout.list_score_row, inItems);
		getListView().setAdapter(arrayAdapter);
	}

}
