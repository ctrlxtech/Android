package com.ctrlxtech.android.welcome;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ctrlx.R;

public class IntroScreenSlidePage1Fragment extends Fragment {
	public static final String ARG_PAGE = "page";
//	private int mPageNumber;

	public static Fragment newInstance(int pageNumber) {
		Fragment fragment = new IntroScreenSlidePage1Fragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mPageNumber = this.getArguments().getInt(ARG_PAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_screen_slide_intro_page1, container, false);
		return rootView;
	}




}
