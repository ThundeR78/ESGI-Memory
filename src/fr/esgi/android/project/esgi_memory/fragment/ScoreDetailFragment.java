package fr.esgi.android.project.esgi_memory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import fr.esgi.android.project.esgi_memory.R;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.db.DatabaseHandler;
import fr.esgi.android.project.esgi_memory.util.FormatValue;

public class ScoreDetailFragment extends Fragment implements OnClickListener {
	private static final String TAG = "ScoreDetailFragment";
	
	DatabaseHandler db;
	private Score score;
	
	private EditText editUsername;
	private TextView textDate, textLevel, textTime, textMove, textBonus, textPoints;
	private Button buttonDelete;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_score_detail, container, false);
		
		editUsername = (EditText) contentView.findViewById(R.id.username);
		textDate = (TextView) contentView.findViewById(R.id.date);
		textLevel = (TextView) contentView.findViewById(R.id.level);
		textTime = (TextView) contentView.findViewById(R.id.time);
		textMove = (TextView) contentView.findViewById(R.id.move);
		textBonus = (TextView) contentView.findViewById(R.id.bonus);
		textPoints = (TextView) contentView.findViewById(R.id.points);
		buttonDelete = (Button) contentView.findViewById(R.id.buttonDelete);
		buttonDelete.setOnClickListener(this);
		
		return contentView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		db = new DatabaseHandler(getActivity());
		score = db.getScore(score.getId());
		
		displayItem(score);
	}

	protected void displayItem(Score inItem) {
		if (inItem != null && inItem.getDate() != null && this.isAdded()) {
			editUsername.setText(inItem.getUsername());
			textDate.setText(FormatValue.datetimeLabelFormat2.format(inItem.getDate()));
			textLevel.setText(inItem.getLevel()+"");
			textTime.setText(FormatValue.millisecondFormat(inItem.getTime()));
			textMove.setText(inItem.getMove()+"");
			textBonus.setText(FormatValue.formatBigNumber(inItem.getBonus()));
			textPoints.setText(FormatValue.formatBigNumber(inItem.getPoint()));
		}
	}
	
	public void setCurrentItem(Score inScore) {
		this.score = inScore;
	}
	
	public void deleteScore() {
		if (getActivity() != null && this.isAdded() && score != null && score.getId() >0) {
			db.deleteScore(score);
			getActivity().finish();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonDelete) {
			deleteScore();
		}
	}
}
