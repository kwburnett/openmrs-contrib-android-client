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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.visit.VisitContract;
import org.openmrs.mobile.models.VisitPhoto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitPhotoRecyclerViewAdapter
		extends RecyclerView.Adapter<VisitPhotoRecyclerViewAdapter.DownloadVisitPhotoViewHolder> {

	private VisitContract.VisitPhotoView view;
	private List<VisitPhoto> visitPhotos;
	private Map<ImageView, VisitPhoto> map = new HashMap<>();

	public VisitPhotoRecyclerViewAdapter(VisitContract.VisitPhotoView view) {
		this.view = view;
	}

	public void setVisitPhotos(List<VisitPhoto> visitPhotos) {
		this.visitPhotos = visitPhotos;
		notifyDataSetChanged();
	}

	@Override
	public DownloadVisitPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_visit_photo, parent, false);
		return new DownloadVisitPhotoViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(DownloadVisitPhotoViewHolder holder, int position) {
		VisitPhoto visitPhoto = visitPhotos.get(position);
		if (visitPhoto == null) {
			return;
		}

		byte[] photoBytes = visitPhoto.getImageColumn().getBlob();
		Bitmap bitmap = ThumbnailUtils
				.extractThumbnail(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length), 100, 100);
		holder.image.setImageBitmap(bitmap);
		holder.image.invalidate();
		map.put(holder.image, visitPhoto);

		holder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View imageView) {
				if (map.containsKey(imageView)) {
					List<String> visitPhotoUuids = new ArrayList<>();
					for (VisitPhoto visitPhoto : visitPhotos) {
						visitPhotoUuids.add(visitPhoto.getUuid());
					}
					view.viewImage(visitPhotos.get(position).getUuid(), visitPhotoUuids);
				}
			}
		});

		holder.image.setLongClickable(true);
		holder.image.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				view.deleteImage(visitPhoto);
				return true;
			}
		});
	}

	@Override
	public int getItemCount() {
		return visitPhotos != null ? visitPhotos.size() : 0;
	}

	class DownloadVisitPhotoViewHolder extends RecyclerView.ViewHolder {
		private ImageView image;

		public DownloadVisitPhotoViewHolder(View itemView) {
			super(itemView);
			image = (ImageView) ((ConstraintLayout) itemView).findViewById(R.id.visit_photo_button);
		}
	}
}
