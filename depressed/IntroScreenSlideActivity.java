package com.ctrlxtech.android.welcome;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ctrlxtech.android.adapter.IntroScreenSlidePagerAdapter;
import com.example.ctrlx.R;

public class IntroScreenSlideActivity extends FragmentActivity implements OnPageChangeListener {
	public static final int NUM_PAGES = 5;

	private ViewPager mPager;

	private PagerAdapter mPagerAdapter;

	private ImageView[] mPoints;

	//the position of current point
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_screen_slide_intro);

		mPager = (ViewPager) findViewById(R.id.intro_pager);

		FragmentManager fm = getSupportFragmentManager();

		mPagerAdapter = new IntroScreenSlidePagerAdapter(fm);

		mPager.setAdapter(mPagerAdapter);

		initPoints();

		mPager.setOnPageChangeListener(this);
	}

	private void initPoints() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.intro_points);

		mPoints = new ImageView[NUM_PAGES];

		for (int i = 0; i < NUM_PAGES; i++) {
			mPoints[i] = (ImageView) linearLayout.getChildAt(i);
			mPoints[i].setEnabled(true);
			mPoints[i].setTag(i);
		}

		currentIndex = 0;

		mPoints[currentIndex].setEnabled(false);
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		int currPosition = mPager.getCurrentItem();
		Log.i("aaa", currPosition+"");
		setCurDot(currPosition);
	}

	private void setCurDot(int position) {
		if (position < 0 || position > NUM_PAGES - 1 || currentIndex == position) {
			return ;
		}
		mPoints[position].setEnabled(false);
		mPoints[currentIndex].setEnabled(true);
		currentIndex = position;
	}






}
