package fr.esgi.android.project.esgi_memory.fragment;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.esgi.android.project.esgi_memory.R;
import fr.esgi.android.project.esgi_memory.ScoreDetailFragmentActivity;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.db.DatabaseContract;
import fr.esgi.android.project.esgi_memory.db.DatabaseHandler;
import fr.esgi.android.project.esgi_memory.view.ScoreListItemAdapter;

public class ScoreListFragment extends ListFragment {
	
	public static final String TAG ="ScoreListFragment";
	
	private int level;
	private String orderBy;
	private List<Score> listScore;
	private DatabaseHandler db;

	public ScoreListFragment() {
		super();
	}
	
	public ScoreListFragment(int level) {
		super();
		
		this.level = level;
		Log.v(TAG, "Level="+level);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		db = new DatabaseHandler(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_score_list, container, false);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		orderBy = DatabaseContract.ScoreBase.COLUMN_NAME_POINT;
		listScore = db.getAllScoresByLevel(level, orderBy);
		
		updateListAdapter(listScore);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		db.close();
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_score, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	//Handle item selection
    	switch (item.getItemId()) {
      	case R.id.action_sort_name:
      		
      		orderBy = DatabaseContract.ScoreBase.COLUMN_NAME_USERNAME +","+ DatabaseContract.ScoreBase.COLUMN_NAME_POINT;
            break;
      	case R.id.action_sort_points:
      		orderBy = DatabaseContract.ScoreBase.COLUMN_NAME_POINT;
            break;
      	default:
      		return super.onOptionsItemSelected(item);
    	}
    	
    	item.setChecked(!item.isChecked());
    	
    	listScore = db.getAllScoresByLevel(level, orderBy);
  		updateListAdapter(listScore);
  		
    	return true;
    }   
	
	private void updateListAdapter(List<Score> inItems) {
		ArrayAdapter<Score> arrayAdapter = new ScoreListItemAdapter(getActivity(), R.layout.list_score_row, inItems);
		getListView().setAdapter(arrayAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent(getActivity(), ScoreDetailFragmentActivity.class);
		intent.putParcelableArrayListExtra(ScoreDetailFragmentActivity.ITEM_LIST_KEY, new ArrayList<Score>(listScore));
		intent.putExtra(ScoreDetailFragmentActivity.STARTING_PAGE_NUMBER_KEY, position);
		intent.putExtra(ScoreDetailFragmentActivity.ITEM_KEY, listScore.get(position));
    	startActivity(intent);
	}

}
