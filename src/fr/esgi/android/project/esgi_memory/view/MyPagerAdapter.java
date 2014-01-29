package fr.esgi.android.project.esgi_memory.view;


import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = "MyPagerAdapter";
	
	private final List<Fragment> fragments;
	
	public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}
	
	@Override
	public int getCount() {
		return this.fragments.size();
	}
	
	public List<Fragment> getFragments() {
		return fragments;
	}
}