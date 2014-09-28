package com.ctrlxtech.android.welcome;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.ctrlxtech.android.main.MainActivity;
import com.example.ctrlx.R;

public class IntroScreenSlidePage5Fragment extends Fragment implements OnClickListener {
	public static final String ARG_PAGE = "page";

	public Button mTourBtn;

	public Button mConfigBtn;


//	private int mPageNumber;

	public static Fragment newInstance(int pageNumber) {
		Fragment fragment = new IntroScreenSlidePage5Fragment();
//		Bundle bundle = new Bundle();
//		bundle.putInt(ARG_PAGE, pageNumber);
//		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


//		mPageNumber = this.getArguments().getInt(ARG_PAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_screen_slide_intro_page5, container, false);
		mTourBtn = (Button) rootView.findViewById(R.id.btn_tour);
		mTourBtn.setOnClickListener(this);

		mConfigBtn = (Button) rootView.findViewById(R.id.btn_config);
		mConfigBtn.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_tour:
			Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;
		case R.id.btn_config:
			Intent intent2 = new Intent(getActivity().getApplicationContext(), ConfigActivity.class);
			getActivity().startActivity(intent2);
			getActivity().finish();
			break;
		}
	}


}
