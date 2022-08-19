package com.illica.mycoupon.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.illica.mycoupon.Listener.ItemClickListener;
import com.illica.mycoupon.Other.Utils;
import com.illica.mycoupon.R;
import com.illica.mycoupon.activity.DetailActivity;
import com.illica.mycoupon.activity.ListActivity;
import com.illica.mycoupon.activity.MainActivity;
import com.illica.mycoupon.definition.CouponType;
import com.illica.mycoupon.model.CouponDescriptor;
import com.illica.mycoupon.persistence.CouponDescriptorManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

	private List<CouponDescriptor> mDataset;
	private Context mContext = null;
	private ListActivity activityParent;
	private String TypeList;
	private Integer Pos;



	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private View v = null;
		private ItemClickListener itemClickListener;
		public ViewHolder(View v) {
			super(v);
			this.v = v;
			v.setOnClickListener(this);
			v.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					showDeleteConfirmation(getLayoutPosition());
					return false;
				}
			});
		}

		public void setTextCompanyName(String text){
			TextView tView = (TextView)v.findViewById(R.id.companyName);
			tView.setText(text);
		}
		public void setCouponTypeImage(Bitmap bitmap){
			ImageView image = v.findViewById(R.id.couponImageList);
			image.setImageBitmap(bitmap);
		}
		public void setCouponTypeImage(Drawable draw){
			ImageView image = v.findViewById(R.id.couponImageList);
			image.setImageDrawable(draw);

		}
		public void setReusableImage(Drawable drawable){
			ImageView image = v.findViewById(R.id.visible);
			image.setImageDrawable(drawable);
		}
		@RequiresApi(api = Build.VERSION_CODES.O)
		public void setExpirationDateText(String date){
			TextView tView = (TextView)v.findViewById(R.id.expiringDate);

			if(date != "-"){
				LocalDate dateLocal = LocalDate.parse(date);
				tView.setText(Utils.convertInITAFormat(dateLocal));
			}
			else{
				tView.setText(date);

			}

		}
		public void setDescription(String description){
			TextView tView = (TextView) v.findViewById(R.id.description);
			tView.setText(description);
		}

		@Override
		public void onClick(View v) {
			this.itemClickListener.onItemClick(v,getLayoutPosition());
		}
		public void setItemClickListener(ItemClickListener ic){
			this.itemClickListener = ic;
		}
	}

	private void showDeleteConfirmation(final int position){

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(R.string.remove_element_message);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				CouponDescriptorManager.getInstance(mContext).removeCoupon(mDataset.get(position));
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public MyAdapter(List<CouponDescriptor> myDataset, Context context) {
		mDataset = myDataset;
		mContext  = context;
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public MyAdapter(Context context, String typeList) {
		mDataset = new ArrayList<>();
		mContext = context;
		TypeList = typeList;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {


		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_element, parent, false);
		// set the view's size, margins, paddings and layout parameters

		ViewHolder vh = new ViewHolder(v);

		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		CouponDescriptor element;
		element = mDataset.get(position);

		String datee = element.getExpiryDate();

		popoluteElement(holder,element);



		//Coupon reusable image
		if(element.getReusable() == true){
			Drawable myDrawable = mContext.getResources().getDrawable(R.drawable.check_img);
			holder.setReusableImage(myDrawable);
		}else if(element.getReusable() == false){
			Drawable myDrawable = mContext.getResources().getDrawable(R.drawable.x_img);
			holder.setReusableImage(myDrawable);
		}




		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onItemClick(View v, int pos) {

				Intent i = new Intent(mContext, DetailActivity.class);
				i.putExtra(MainActivity.TypeList, TypeList);
				i.putExtra(ListActivity.CouponObject,mDataset.get(pos));
				i.putExtra(ListActivity.Position,pos);
				mContext.startActivity(i);

			}
		});

	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mDataset.size();
	}

	public List<CouponDescriptor> getmDataset() {
		return mDataset;
	}

	public void setDataset(List<CouponDescriptor> mDataset) {
		this.mDataset = mDataset;
	}
	public void filterList(String text, List<CouponDescriptor> backupList){
		List<CouponDescriptor> filteredList = new ArrayList<>();
		for(CouponDescriptor cd : backupList){
			if(cd.getCompanyName().toLowerCase().contains(text.toLowerCase())){
				filteredList.add(cd);
			}
		}
		this.setDataset(filteredList);
		notifyDataSetChanged();
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public void popoluteElement(ViewHolder holder, CouponDescriptor element)
	{
		// Company name
		holder.setTextCompanyName(element.getCompanyName());

		// Coupon type image
		if(element.getCouponType() == CouponType.QRCode.ordinal()){
			Drawable myDrawable = mContext.getResources().getDrawable(R.drawable.qrcode_img);
			holder.setCouponTypeImage(myDrawable);
		}else if(element.getCouponType() == CouponType.Barcode.ordinal()){
			Drawable myDrawable = mContext.getResources().getDrawable(R.drawable.barcode_img);
			holder.setCouponTypeImage(myDrawable);
		}

		// Description
		if(element.getDescription().equals("")){
			holder.setDescription("-");
		}else{
			holder.setDescription(element.getDescription());
		}

		// Date
		if(element.getExpiryDate().equals("")){
			holder.setExpirationDateText("-");
		}else
		{
			holder.setExpirationDateText(element.getExpiryDate());
		}


	}

}