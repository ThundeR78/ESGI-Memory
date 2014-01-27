package fr.esgi.android.project.esgi_memory;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import fr.esgi.android.project.esgi_memory.util.MyTabFactory;
import fr.esgi.android.project.esgi_memory.view.TabsPagerAdapter;

public class ScoreListFragmentActivity extends ActionBarActivity implements OnTabChangeListener, OnPageChangeListener {

	private TabsPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Tab Initialization
        initialiseTabHost();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        // Fragments and ViewPager Initialization
       
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(ScoreListFragmentActivity.this);
    }

    // Method to add a TabHost
    private static void AddTab(ScoreListFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    @Override
	public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
    }

    @Override
        public void onPageSelected(int arg0) {
    }

  
    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        //Put here our Tabs
        ScoreListFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("EasyTab").setIndicator(getResources().getString(R.string.level_easy)));
        ScoreListFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("NormalTab").setIndicator(getResources().getString(R.string.level_normal)));
        ScoreListFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("HardTab").setIndicator(getResources().getString(R.string.level_hard)));

        mTabHost.setOnTabChangedListener(this);
    }
}

//http://www.oodlestechnologies.com/blogs/Tabs-with-swipe-effect-on-Android