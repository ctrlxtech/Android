package com.ctrlxtech.android.adapter;


import com.ctrlxtech.android.welcome.IntroScreenSlideActivity;
import com.ctrlxtech.android.welcome.IntroScreenSlidePage1Fragment;
import com.ctrlxtech.android.welcome.IntroScreenSlidePage2Fragment;
import com.ctrlxtech.android.welcome.IntroScreenSlidePage3Fragment;
import com.ctrlxtech.android.welcome.IntroScreenSlidePage4Fragment;
import com.ctrlxtech.android.welcome.IntroScreenSlidePage5Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class IntroScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

	private final Fragment[] fragments = new Fragment[] {new IntroScreenSlidePage1Fragment(),
												   new IntroScreenSlidePage2Fragment(),
												   new IntroScreenSlidePage3Fragment(),
												   new IntroScreenSlidePage4Fragment(),
												   new IntroScreenSlidePage5Fragment()};


	public IntroScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		return fragments[index];
	}

	@Override
	public int getCount() {
		return IntroScreenSlideActivity.NUM_PAGES;
	}


}
