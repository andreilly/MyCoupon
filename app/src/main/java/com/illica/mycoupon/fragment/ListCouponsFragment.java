package com.illica.mycoupon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.illica.mycoupon.R;
import com.illica.mycoupon.activity.ListActivity;
import com.illica.mycoupon.activity.MainActivity;
import com.illica.mycoupon.adapter.MyAdapter;
import com.illica.mycoupon.model.CouponDescriptor;
import com.illica.mycoupon.persistence.CouponDescriptorManager;


import java.util.List;


public  class ListCouponsFragment extends Fragment {

	private ListActivity activityParent;
	private static final String TAG = "ActiveListFragment";
	private RecyclerView mRecyclerView = null;
	private LinearLayoutManager mLayoutManager = null;
	private MyAdapter mAdapter = null;
	private ImageButton addButton = null;
	private Context mContext = null;
	private List<CouponDescriptor> backupList;
	private CouponDescriptorManager couponDescriptorManager = null;

	private SearchView searchView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.history_fragment, container, false);
		searchView = rootView.findViewById(R.id.search_view);
		this.mContext = getContext();
		this.couponDescriptorManager = couponDescriptorManager.getInstance(mContext);

		activityParent = (ListActivity) getActivity();
		if(activityParent.getTypeView().equals(MainActivity.Active_Coupon)){
			observeLogDataActive();
		}else if(activityParent.getTypeView().equals(MainActivity.Expired_Coupon)){
			observeLogDataExpire();
		}else if(activityParent.getTypeView().equals(MainActivity.Used_Coupon)){
			observeLogDataUsed();
		}

		init(rootView);

		return rootView;
	}

	private void init(View rootView){

		mRecyclerView  = (RecyclerView)rootView.findViewById(R.id.my_recycler_view);

		// use a linear layout manager
		mLayoutManager  = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

		mRecyclerView.setLayoutManager(mLayoutManager);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// specify an adapter (see also next example)
		mAdapter  = new MyAdapter(mContext, activityParent.getTypeView());
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.smoothScrollToPosition(activityParent.getPos());
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String newText) {
				filter(newText);
				searchView.clearFocus();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				filter(newText);
				return true;
			}


		});

	}

	private void filter(String newText) {
		mAdapter.filterList(newText,backupList);
	}

	private void observeLogDataActive(){
			this.couponDescriptorManager.getActiveCouponLiveDataList().observe(getViewLifecycleOwner(), new Observer<List<CouponDescriptor>>() {
				@Override
				public void onChanged(List<CouponDescriptor> logDescriptors) {
					if(logDescriptors != null){
						Log.d(TAG, "Update Log List Received ! List Size: " + logDescriptors.size());
						refreshRecyclerView(logDescriptors, 0);
						backupList = logDescriptors;
					}
					else
						Log.e(TAG, "Error observing Log List ! Received a null Object !");
				}
			});
	}
	private void observeLogDataExpire(){
		this.couponDescriptorManager.getExpiringCouponLiveDataList().observe(getViewLifecycleOwner(), new Observer<List<CouponDescriptor>>() {
			@Override
			public void onChanged(List<CouponDescriptor> logDescriptors) {
				if(logDescriptors != null){
					Log.d(TAG, "Update Log List Received ! List Size: " + logDescriptors.size());
					refreshRecyclerView(logDescriptors, 0);
					backupList = logDescriptors;
				}
				else
					Log.e(TAG, "Error observing Log List ! Received a null Object !");
			}
		});
	}

	private void observeLogDataUsed(){
		this.couponDescriptorManager.getUsedCouponLiveDataList().observe(getViewLifecycleOwner(), new Observer<List<CouponDescriptor>>() {
			@Override
			public void onChanged(List<CouponDescriptor> logDescriptors) {
				if(logDescriptors != null){
					Log.d(TAG, "Update Log List Received ! List Size: " + logDescriptors.size());
					refreshRecyclerView(logDescriptors, 0);
					backupList = logDescriptors;
				}
				else
					Log.e(TAG, "Error observing Log List ! Received a null Object !");
			}
		});
	}

	private void refreshRecyclerView(List<CouponDescriptor> updatedList, int scrollPosition){
		mAdapter.setDataset(updatedList);
		mAdapter.notifyDataSetChanged();
		if(scrollPosition > 0)
			mLayoutManager.scrollToPosition(scrollPosition);
	}
	


}