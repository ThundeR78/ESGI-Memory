package fr.esgi.android.project.esgi_memory;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.fragment.ScoreDetailFragment;
import fr.esgi.android.project.esgi_memory.view.MyPagerAdapter;

public class ScoreDetailFragmentActivity extends FragmentActivity {
	
	private static final String TAG = "DetailFragmentsSlider";

	public final static String ITEM_KEY = "ITEM_KEY";
	public static final String ITEM_LIST_KEY = "ITEM_LIST_KEY";
	public static final String STARTING_PAGE_NUMBER_KEY = "STARTING_PAGE_NUMBER_KEY";

	protected MyPagerAdapter myPagerAdapter;
	protected ArrayList<Score> scores;
	protected Score currentItem;
	protected int currentIndex;

	protected TextView title;
	protected Button navigation_previous;
	protected Button navigation_next;
	protected ViewPager pager; 
	protected LinearLayout navigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		if (intent != null) {
			scores = intent.getParcelableArrayListExtra(ITEM_LIST_KEY);
			currentItem = intent.getParcelableExtra(ITEM_KEY);
			currentIndex = intent.getIntExtra(STARTING_PAGE_NUMBER_KEY, 0);
		}
		
		setContentView(R.layout.activity_score_detail);
		
		navigation = (LinearLayout) findViewById(R.id.navigation);
		navigation_previous = (Button) findViewById(R.id.navigation_previous);
		navigation_previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentIndex--;
				pager.setCurrentItem(currentIndex);
			}
		});
		navigation_next = (Button) findViewById(R.id.navigation_next);
		navigation_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentIndex++;
				pager.setCurrentItem(currentIndex);				
			}
		});
		

		List<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < scores.size(); i++) {
			ScoreDetailFragment scoreDetailFragment = (ScoreDetailFragment) Fragment.instantiate(this, ScoreDetailFragment.class.getName());
			if (scores != null && scores.get(i) != null)
				scoreDetailFragment.setCurrentItem((Score) scores.get(i));
			fragments.add(scoreDetailFragment);
		}

		this.myPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

		pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.myPagerAdapter);
		pager.setCurrentItem(currentIndex);
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentItem = scores.get(position);
				currentIndex = position;
				
				ScoreDetailFragment fragment = (ScoreDetailFragment) myPagerAdapter.getItem(position);
				fragment.setUserVisibleHint(true);

				updateNavigation();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				switch (state) {
					case ViewPager.SCROLL_STATE_IDLE:
						// Indicates that the pager is in an idle, settled state. The current page is fully in view and no animation is in progress. 
						break;
					case ViewPager.SCROLL_STATE_DRAGGING:
						// Indicates that the pager is currently being dragged by the user.
						break;
					case ViewPager.SCROLL_STATE_SETTLING:
						// Indicates that the pager is in the process of settling to a final position. 
						break;
				}
			}
		});
		
		updateNavigation();
	}
	
	protected void updateNavigation() {
		if (scores.size() < 2) 
			navigation.setVisibility(View.GONE);
		
		navigation_previous.setEnabled(true);
		navigation_next.setEnabled(true);
		
		if (currentIndex == 0)
			navigation_previous.setEnabled(false);
		if (currentIndex+1 == scores.size())
			navigation_next.setEnabled(false);
	}
}