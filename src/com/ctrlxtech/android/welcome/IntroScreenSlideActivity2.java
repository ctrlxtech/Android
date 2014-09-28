package com.ctrlxtech.android.welcome;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ctrlxtech.android.adapter.IntroScreenSlidePagerAdapter2;
import com.ctrlxtech.android.config.ConfigActivity;
import com.ctrlxtech.android.main.MainActivity;
import com.example.ctrlx.R;

public class IntroScreenSlideActivity2 extends Activity implements OnPageChangeListener, OnClickListener {

	private ViewPager mPager;

	private IntroScreenSlidePagerAdapter2 mAdapter;

	private ArrayList<View> mViews;

	private static final int[] pics = {R.drawable.intro1, R.drawable.intro2, R.drawable.intro3,
										R.drawable.intro4, R.drawable.intro5};

	private ImageView[] mPoints;

	public static final int NUM_PAGES = 5;

	private int currentIndex;

	private Button mBtnConfig;
	private Button mBtnTour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide_intro);

		mBtnConfig = (Button)this.findViewById(R.id.intro_btn_config);
		mBtnTour = (Button) this.findViewById(R.id.intro_btn_tour);

		mBtnConfig.setOnClickListener(this);
		mBtnTour.setOnClickListener(this);

		mViews = new ArrayList<View>();
		mPager = (ViewPager) findViewById(R.id.intro_pager);
		mAdapter = new IntroScreenSlidePagerAdapter2(mViews);

		for(int i=0; i<pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(pics[i]);
			mViews.add(iv);
		}

		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);

		initPoints();
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
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		int currPosition = mPager.getCurrentItem();
		Log.i("aaa", currPosition+"");
		setCurDot(currPosition);
		if (position == NUM_PAGES - 1) {
			mBtnConfig.setVisibility(View.VISIBLE);
			mBtnTour.setVisibility(View.VISIBLE);

		}
	}

	private void setCurDot(int position) {
		if (position < 0 || position > NUM_PAGES - 1 || currentIndex == position) {
			return ;
		}
		mPoints[position].setEnabled(false);
		mPoints[currentIndex].setEnabled(true);
		currentIndex = position;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.intro_btn_tour:
			Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.intro_btn_config:
			Intent intent2 = new Intent(this.getApplicationContext(), ConfigActivity.class);
			startActivity(intent2);
			this.finish();
			break;
		}
	}
}
