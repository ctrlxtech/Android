package com.ctrlxtech.android.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.ctrlx.R;

public class FragmentDashboard extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	private GridView gridView;

	public FragmentDashboard() {
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, 1);
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
		View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
//		gridView = (GridView) rootView.findViewById(R.id.GridView);
//		SimpleAdapter dridAdapter = new SimpleAdapter(this.getActivity(), gridItemList, R.layout.fragment_dashboard_item, null, null);
//		gridView.setAdapter(gridAdapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		int num = getArguments().getInt(ARG_SECTION_NUMBER);
		((MainActivity) getActivity()).onSectionAttached(num);
	}
}
