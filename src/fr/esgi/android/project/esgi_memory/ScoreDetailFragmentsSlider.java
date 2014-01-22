package fr.esgi.android.project.esgi_memory;

import android.os.Parcelable;
import fr.esgi.android.project.esgi_memory.fragment.ScoreDetailFragment;

public class ScoreDetailFragmentsSlider<Item extends Parcelable> extends DetailFragmentsSlider<Item> {
	
	private static final String TAG = "ScoreDetailFragmentsSlider";

	@Override
	protected String getFragmentClassname() {
		return ScoreDetailFragment.class.getName();
	}

}