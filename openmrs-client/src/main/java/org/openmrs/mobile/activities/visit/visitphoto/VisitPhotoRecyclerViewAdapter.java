/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.visit.visitphoto;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.visit.VisitContract;
import org.openmrs.mobile.data.DataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitPhotoRecyclerViewAdapter
		extends RecyclerView.Adapter<VisitPhotoRecyclerViewAdapter.DownloadVisitPhotoViewHolder> {

	private Activity context;
	private VisitContract.VisitPhotoView view;
	private List<String> items;
	private Map<ImageView, Bitmap> map = new HashMap<>();

	public VisitPhotoRecyclerViewAdapter(Activity context,
			List<String> items, VisitContract.VisitPhotoView view) {
		this.context = context;
		this.items = items;
		this.view = view;
	}

	@Override
	public DownloadVisitPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_photo_row, parent, false);
		return new DownloadVisitPhotoViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(DownloadVisitPhotoViewHolder holder, int position) {
		String obsUuid = items.get(position);
		if (obsUuid == null)
			return;

		view.downloadImage(obsUuid, new DataService.GetCallback<Bitmap>() {
			@Override
			public void onCompleted(Bitmap entity) {
				holder.image.setImageBitmap(entity);
				holder.image.invalidate();
				map.put(holder.image, entity);
			}

			@Override
			public void onError(Throwable t) {
				holder.image.setVisibility(View.GONE);
			}
		});

		holder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View imageView) {
				if (map.containsKey(imageView)) {
					Dialog settingsDialog = new Dialog(context);
					settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1000, 800);
					lp.addRule(RelativeLayout.CENTER_IN_PARENT);
					ImageView expandImage = new ImageView(context);
					expandImage.setLayoutParams(lp);
					expandImage.setImageResource(R.drawable.ic_male);
					expandImage.setImageBitmap(map.get(imageView));
					settingsDialog.addContentView(expandImage, lp);
					settingsDialog.show();
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	class DownloadVisitPhotoViewHolder extends RecyclerView.ViewHolder {
		private LinearLayout rowLayout;
		private ImageView image;
		private TextView fileCaption;

		public DownloadVisitPhotoViewHolder(View itemView) {
			super(itemView);
			rowLayout = (LinearLayout)itemView;
			fileCaption = (TextView)itemView.findViewById(R.id.visitPhotoFileCaption);
			image = (ImageView)itemView.findViewById(R.id.visitPhoto);
		}
	}
}