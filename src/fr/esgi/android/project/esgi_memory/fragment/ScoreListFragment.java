package fr.esgi.android.project.esgi_memory.fragment;


import fr.esgi.android.project.esgi_memory.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScoreListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_score_list, container, false);
		
		return rootView;
	}

}
