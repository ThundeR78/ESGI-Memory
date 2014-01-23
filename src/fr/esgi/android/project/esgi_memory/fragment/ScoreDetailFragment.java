package fr.esgi.android.project.esgi_memory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.esgi.android.project.esgi_memory.R;
import fr.esgi.android.project.esgi_memory.business.Score;

public class ScoreDetailFragment extends Fragment {
	private static final String TAG = "ScoreDetailFragment";
	
	private Score score;
	
	private TextView textScore;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_score_detail, container, false);
		
		textScore = (TextView) contentView.findViewById(R.id.score);
		
		Log.v("DETAIL", "SCORE");
		
		return contentView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (score != null)
			displayItem(score);
	}

	protected void displayItem(Score inItem) {
		if (inItem != null && this.isAdded()) {
			textScore.setText(inItem.getPoint()+"");
			Log.v("DETAIL SCORE", inItem.getPoint()+"");
		}
	}
	
	public void setCurrentItem(Score score2) {
		this.score = score2;
	}
}
