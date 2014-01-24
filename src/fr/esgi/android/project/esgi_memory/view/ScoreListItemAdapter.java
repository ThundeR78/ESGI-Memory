package fr.esgi.android.project.esgi_memory.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.esgi.android.project.esgi_memory.R;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.util.FormatValue;

public class ScoreListItemAdapter extends ArrayAdapter<Score> {
	private static final String TAG = "ScoreListItemAdapter";

	private int _rowRessourceID;
	private LayoutInflater _layoutInflater = null;

	/**
	 * Constructor
	 * @param context
	 * @param viewResourceId : Layout row to inflate
	 * @param inItems
	 */
	public ScoreListItemAdapter(Context in_context, int in_viewResourceId, List<Score> inItems) {
		super(in_context, in_viewResourceId, inItems);

		this._layoutInflater = (LayoutInflater) in_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this._rowRessourceID = in_viewResourceId;
	}

	@Override
	public View getView(int in_position, View convertView, ViewGroup in_parent) {
		ViewHolder holder = null;

		Score currentItem = getItem(in_position);
		if (currentItem != null) {
			if (convertView == null) {
				convertView = _layoutInflater.inflate(_rowRessourceID, null);
				holder = new ViewHolder();
				holder.username = (TextView) convertView.findViewById(R.id.score_row_username);
				holder.score = (TextView) convertView.findViewById(R.id.score_row_score);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
		}
		
		holder.username.setText(currentItem.getUsername());
		String points = FormatValue.formatBigNumber(currentItem.getPoint());
		holder.score.setText(points);

		return convertView;
	}

	public static class ViewHolder {
		public TextView username;
		public TextView score;
	}

}
