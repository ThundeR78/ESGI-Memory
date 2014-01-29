package fr.esgi.android.project.esgi_memory.view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.esgi.android.project.esgi_memory.ESGIMemoryApp;
import fr.esgi.android.project.esgi_memory.fragment.ScoreListFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		
		//Return Fragment with good value
		switch (index) {
		case 0:
			return new ScoreListFragment(ESGIMemoryApp.KEY_LEVEL_EASY);
		case 1:
			return new ScoreListFragment(ESGIMemoryApp.KEY_LEVEL_NORMAL);
		case 2:
			return new ScoreListFragment(ESGIMemoryApp.KEY_LEVEL_HARD);
		}

		return null;
	}

	@Override
	public int getCount() {
		//Number of tabs
		return 3;
	}

}
