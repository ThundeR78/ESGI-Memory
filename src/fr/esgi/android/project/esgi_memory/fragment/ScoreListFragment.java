package fr.esgi.android.project.esgi_memory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.esgi.android.project.esgi_memory.R;

public class ScoreListFragment extends Fragment {
	
	public static final String TAG ="ScoreListFragment";
	
	private int level;

	public ScoreListFragment() {
		super();
	}
	
	public ScoreListFragment(int level) {
		super();
		
		this.level = level;
		Log.v(TAG, "Level="+level);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_score_list, container, false);
		TextView t = (TextView) rootView.findViewById(R.id.section_label);
		t.setText("Level is "+level);
		
		return rootView;
	}

}
