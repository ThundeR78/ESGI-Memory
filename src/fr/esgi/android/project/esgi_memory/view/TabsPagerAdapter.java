package fr.esgi.android.project.esgi_memory.view;


import fr.esgi.android.project.esgi_memory.ESGIMemoryApp;
import fr.esgi.android.project.esgi_memory.fragment.ScoreListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ScoreListFragment(ESGIMemoryApp.KEY_LEVEL_EASY);
		case 1:
			// Games fragment activity
			return new ScoreListFragment(ESGIMemoryApp.KEY_LEVEL_NORMAL);
		case 2:
			// Movies fragment activity
			return new ScoreListFragment(ESGIMemoryApp.KEY_LEVEL_HARD);
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
