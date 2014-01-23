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
	
	// public Fragment getPreviousItem() {
	// int position = this.getItemPosition(this);
	// if (position > 0)
	// return (Fragment) this.fragments.get(position-1);
	// else
	// return getItem(position);
	// }
	
	// public Fragment getNextItem() {
	// int position = this.getItemPosition(this);
	// if (position < getCount()-1)
	// return (Fragment) this.fragments.get(position+1);
	// else
	// return getItem(position);
	// }
	
	@Override
	public int getCount() {
		return this.fragments.size();
	}
	
	public List<Fragment> getFragments() {
		return fragments;
	}
	
	// @Override
	// public void startUpdate(ViewGroup container) {
	// Log.v(TAG, "startUpdate");
	// super.startUpdate(container);
	// }
	//
	// @Override
	// public void finishUpdate(ViewGroup container) {
	// Log.v(TAG, "finishUpdate");
	// super.finishUpdate(container);
	// }
	//
	//
	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	// Log.v(TAG, "instantiateItem position: "+position);
	// return super.instantiateItem(container, position);
	// }
	//
	// @Override
	// public void destroyItem(ViewGroup container, int position, Object object) {
	// Log.v(TAG, "destroyItem position: "+position);
	// super.destroyItem(container, position, object);
	// }
}