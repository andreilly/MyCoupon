package com.illica.mycoupon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.illica.mycoupon.R;
import com.illica.mycoupon.adapter.MyAdapter;
import com.illica.mycoupon.definition.CouponType;
import com.illica.mycoupon.model.CouponDescriptor;
import com.illica.mycoupon.persistence.CouponDescriptorManager;


import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Marco Picone (picone.m@gmail.com) 20/03/2020
 * Fragment for Logs History
 */
public  class HistoryFragment extends Fragment {

	private static final String TAG = "HistoryFragment";

	private RecyclerView mRecyclerView = null;
	private LinearLayoutManager mLayoutManager = null;
	private MyAdapter mAdapter = null;
	private ImageButton addButton = null;

	private Context mContext = null;

	private CouponDescriptorManager couponDescriptorManager = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.history_fragment, container, false);

		this.mContext = getContext();
		this.couponDescriptorManager = couponDescriptorManager.getInstance(mContext);

		init(rootView);
		observeLogData();

		return rootView;
	}

	private void init(View rootView){

		mRecyclerView  = (RecyclerView)rootView.findViewById(R.id.my_recycler_view);

		// use a linear layout manager
		mLayoutManager  = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mLayoutManager.scrollToPosition(0);

		mRecyclerView.setLayoutManager(mLayoutManager);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// specify an adapter (see also next example)
		mAdapter  = new MyAdapter(mContext);
		mRecyclerView.setAdapter(mAdapter);

	}

	private void observeLogData(){
		this.couponDescriptorManager.getCouponLiveDataList().observe(getViewLifecycleOwner(), new Observer<List<CouponDescriptor>>() {
			@Override
			public void onChanged(List<CouponDescriptor> logDescriptors) {
				if(logDescriptors != null){
					Log.d(TAG, "Update Log List Received ! List Size: " + logDescriptors.size());
					refreshRecyclerView(logDescriptors, 0);
				}
				else
					Log.e(TAG, "Error observing Log List ! Received a null Object !");
			}
		});
	}

	private void refreshRecyclerView(List<CouponDescriptor> updatedList, int scrollPosition){
		mAdapter.setDataset(updatedList);
		mAdapter.notifyDataSetChanged();
		if(scrollPosition >= 0)
			mLayoutManager.scrollToPosition(scrollPosition);
	}
	


}