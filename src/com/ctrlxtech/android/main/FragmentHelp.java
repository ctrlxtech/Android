package com.ctrlxtech.android.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHelp extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	public FragmentHelp() {
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, 6);
		setArguments(args);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		int num = getArguments().getInt(ARG_SECTION_NUMBER);
		((MainActivity) getActivity()).onSectionAttached(num);
	}
}
