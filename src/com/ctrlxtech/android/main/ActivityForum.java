package com.ctrlxtech.android.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.ctrlx.R;

public class ActivityForum extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	private WebView mWebView;

	public ActivityForum() {
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, 5);
		setArguments(args);
	}

//	public FragmentForum(Bundle args) {
//		setArguments(args);
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_forum, container, false);
		mWebView = (WebView) rootView.findViewById(R.id.webview);
//		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
		mWebView.loadUrl("http://www.google.com");
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		int num = getArguments().getInt(ARG_SECTION_NUMBER);
		((MainActivity) getActivity()).onSectionAttached(num);
	}
}
