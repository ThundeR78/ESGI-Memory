package fr.esgi.android.project.esgi_memory;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.adenclassifieds.android.cadremploi.widget.NavBar;
import com.adenclassifieds.android.common.activity.ServerParcelable;
import com.adenclassifieds.android.common.fragment.RequestDetailFragment;
import com.adenclassifieds.android.common.net.AdenRequest;
import com.adenclassifieds.android.common.view.MyPagerAdapter;
import com.coboltforge.slidemenu.SlideMenu;

public abstract class DetailFragmentsSlider<Item extends Parcelable> extends FragmentActivity {
	
	private static final String TAG = "DetailFragmentsSlider";

	public final static String ITEM_KEY = "ITEM_KEY";
	public static final String ITEM_LIST_KEY = "ITEM_LIST_KEY";
	public static final String STARTING_PAGE_NUMBER_KEY = "STARTING_PAGE_NUMBER_KEY";

	protected MyPagerAdapter myPagerAdapter;
	protected ArrayList<Item> items;
	protected Item currentItem;
	protected int currentIndex;

	protected NavBar navbar;
	protected TextView title;
	protected Button navigation_previous;
	protected Button navigation_next;
	protected ViewPager pager; 
	
	
	protected abstract String getFragmentClassname();
	protected abstract void updateTitle();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		if (intent != null) {
			items = intent.getParcelableArrayListExtra(ITEM_LIST_KEY);
			currentItem = intent.getParcelableExtra(ITEM_KEY);
			currentIndex = intent.getIntExtra(STARTING_PAGE_NUMBER_KEY, 0);
		}
		
		setContentView(R.layout.detail);
		
		NavBar navbar = (NavBar) findViewById(R.id.navbar);
		navbar.setSlideMenu((SlideMenu) findViewById(R.id.slideMenu));

		title = (TextView) findViewById(R.id.title);
		
		navigation_previous = (Button) findViewById(R.id.navigation_previous);
		navigation_previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentIndex--;
				
				pager.setCurrentItem(currentIndex);
				
				updateTitle();
			}
		});
		navigation_next = (Button) findViewById(R.id.navigation_next);
		navigation_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentIndex++;

				pager.setCurrentItem(currentIndex);
				
				updateTitle();
			}
		});
		

		List<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < items.size(); i++) {
			RequestDetailFragment<ServerParcelable, AdenRequest<?>> requestDetailFragment = (RequestDetailFragment<ServerParcelable, AdenRequest<?>>) Fragment.instantiate(this, getFragmentClassname());
			if (items != null && items.get(i) != null)
				requestDetailFragment.setCurrentItem((ServerParcelable) items.get(i));
			fragments.add(requestDetailFragment);
		}

		this.myPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

		pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.myPagerAdapter);
		pager.setCurrentItem(currentIndex);
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentItem = items.get(position);
				currentIndex = position;
				
				RequestDetailFragment fragment = (RequestDetailFragment)myPagerAdapter.getItem(position);
				fragment.setUserVisibleHint(true);
				fragment.refreshUI();

				updateTitle();

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
		
		updateTitle();
		
		updateNavigation();
	}
	
	protected void updateNavigation() {
		navigation_previous.setEnabled(true);
		navigation_next.setEnabled(true);
		
		if (currentIndex == 0) {
			navigation_previous.setEnabled(false);
		}
		if (currentIndex+1 == items.size()) {
			navigation_next.setEnabled(false);
		}
	}
}